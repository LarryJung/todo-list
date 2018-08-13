package com.larry.todolist.acceptanceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.larry.todolist.domain.RelationRepository;
import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.requestDto.ReferenceTaskDto;
import com.larry.todolist.dto.requestDto.TaskRequestDto;
import com.larry.todolist.dto.responseDto.TaskResponseDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.larry.todolist.domain.support.TaskType.SUB;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskAcceptanceTest {

    private final Logger log = LoggerFactory.getLogger(TaskAcceptanceTest.class);

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    public void registerTask_pass() throws IOException {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("할일목록페이지만들기");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        log.info("response body : {}", response.getBody());
        TaskResponseDto dto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        assertThat(dto.getTodo(), is("할일목록페이지만들기"));
    }

    @Test
    public void registerTask_validation_fail() {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("a");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        log.info("response body : {}", response.getBody());
    }

    @Test
    public void registerTask_with_subTaskList() throws IOException {
        Long cleaningRoom = registerTask("방청소");
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("청소");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, cleaningRoom));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        log.info("response json : {}", response.getBody());
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        relationRepository.findAll().forEach(r -> log.info("객체 관계는? : {}", r));
    }

    @Test
    public void registerTask_with_referenceInfo() throws IOException {
        Long subTask1 = registerTask("김치먹기");
        Long subTask2 = registerTask("콩나물국먹기");
        Long subTask3 = registerTask("반찬먹기");
        Long masterTask = registerTask("밥먹기");
        ReferenceTaskDto dto = new ReferenceTaskDto(SUB, new Long[]{subTask3, subTask2, subTask1});
        ResponseEntity<String> response = restTemplate.postForEntity(String.format("/api/tasks/%d", masterTask), dto, String.class);
        log.info("response body : {}", response.getBody());
        relationRepository.findAll().forEach(r -> log.info("객체 관계는? : {}", r));
    }

    @Test
    public void completeTest_for_one_task_pass() throws IOException {
        Long sampleTask = registerTask("숨쉬기");
        ResponseEntity<String> response = restTemplate.getForEntity(String.format("/api/tasks/%s/complete", sampleTask), String.class);
        log.info("response body : {}", response.getBody());
        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertNotNull(taskDto.getCompletedDate());
    }

    @Test
    public void completeTest_two_task_pass() throws IOException {
        Long subTask = registerTask("책상정리");
        restTemplate.getForEntity(String.format("/api/tasks/%s/complete", subTask), String.class);

        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("방정리");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, subTask));
        Long cleaning = registerTask(taskRequestDto);

        ResponseEntity<String> cleaningResponse = restTemplate.getForEntity(String.format("/api/tasks/%s/complete", cleaning), String.class);
        log.info("response body : {}", cleaningResponse.getBody());

        TaskResponseDto taskDto = mapper.readValue(cleaningResponse.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertNotNull(taskDto.getCompletedDate());
    }

    @Test
    public void completeTest_two_task_fail() throws IOException {
        Long subTask = registerTask("집에서놀기");

        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("놀기");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, subTask));
        Long masterTask = registerTask(taskRequestDto);

        ResponseEntity<String> response = restTemplate.getForEntity(String.format("/api/tasks/%s/complete", masterTask), String.class);
        log.info("response body : {}", response.getBody());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void completeTest_all_task_pass() throws IOException {
        Long sub1 = registerTask("테스트구현");
        Long sub2 = registerTask("서버구현");
        Long sub3 = registerTask("프론트구현");
        Long master = registerTask("과제만들기");

        // 참조 관계 맵핑
        ReferenceTaskDto dtoForChores = new ReferenceTaskDto(SUB, new Long[]{sub3, sub2, sub1});
        restTemplate.postForEntity(String.format("/api/tasks/%d", master), dtoForChores, String.class);
        ReferenceTaskDto dtoForCleaning = new ReferenceTaskDto(SUB, new Long[]{sub1});
        restTemplate.postForEntity(String.format("/api/tasks/%d", sub2), dtoForCleaning, String.class);

        // careful to order
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", sub1), String.class);
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", sub2), String.class);
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", sub3), String.class);

        ResponseEntity<String> response = restTemplate.getForEntity(String.format("/api/tasks/%d/complete", master), String.class);
        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        assertNotNull(taskDto.getCompletedDate());
        relationRepository.findAll().forEach(r -> log.info("객체 관계는? : {}", r));
    }

    @Test
    public void completeTest_one_not_complete() {
        Long sub1 = registerTask("테스트구현");
        Long notCompleted = registerTask("완료 안될 대상");
        Long sub3 = registerTask("프론트구현");
        Long master = registerTask("과제만들기");

        // 참조 관계 맵핑
        ReferenceTaskDto dtoForChores = new ReferenceTaskDto(SUB, new Long[]{sub3, notCompleted, sub1});
        restTemplate.postForEntity(String.format("/api/tasks/%d", master), dtoForChores, String.class);
        ReferenceTaskDto dtoForCleaning = new ReferenceTaskDto(SUB, new Long[]{sub1});
        restTemplate.postForEntity(String.format("/api/tasks/%d", notCompleted), dtoForCleaning, String.class);

        // careful to order
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", sub1), String.class);
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", sub3), String.class);

        ResponseEntity<String> response = restTemplate.getForEntity(String.format("/api/tasks/%d/complete", master), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

//    @Test
//    public void updateTest_pass() throws IOException {
//        Long today = registerTask("오늘할일");
//        final String updateContent = "수정내용";
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<String> requestEntity = new HttpEntity<>(updateContent, headers);
//        ResponseEntity<String> response = restTemplate.exchange(String.format("/api/tasks/%d", today), HttpMethod.PUT, requestEntity, String.class);
//        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
//        assertThat(taskDto.getTodo(), is(updateContent));
//    }
//
//    @Test
//    public void updateTest_unique_fail() {
//        Long task1 = registerTask("잠자기"); registerTask("잠안자기");
//        final String updateContent = "잠안자기";
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<String> requestEntity = new HttpEntity<>(updateContent, headers);
//        ResponseEntity<String> response = restTemplate.exchange(String.format("/api/tasks/%d", task1), HttpMethod.PUT, requestEntity, String.class);
//        log.info("response body {}", response.getBody());
//        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
//    }

    public Long registerTask(String todo) {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo(todo);
        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, Task.class);
        return response.getBody().getId();
    }


    @Test
    public void pagingTest() {
        for (int i = 0; i < 20; i++) {
            registerTask(String.format("%d%d%d%d", i,i,i,i));
        }
        ResponseEntity<String> response = restTemplate.getForEntity("/api/tasks?page=1&size=5&complete=false", String.class);
        log.info("response body {}:", response.getBody());
    }

    private Long registerTask(TaskRequestDto taskRequestDto) throws IOException {
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        return mapper.readValue(response.getBody(), TaskResponseDto.class).getId();
    }

}
