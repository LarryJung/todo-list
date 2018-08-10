package com.larry.todolist.acceptanceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.requestDto.ReferenceTaskDto;
import com.larry.todolist.dto.requestDto.TaskRequestDto;
import com.larry.todolist.dto.responseDto.TaskResponseDto;
import com.larry.todolist.repository.TaskRepository;
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
        log.info("response body : {}", response.getBody());
    }

    @Test
    public void registerTask_with_subTaskList() throws IOException {
        Long cleaningRoom = registerTask("방청소");
        log.info("방청소 Id : {}", cleaningRoom);
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("청소");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, cleaningRoom));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        log.info("response json : {}", response.getBody());
        TaskResponseDto dto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        log.info("dto : {}", dto);
        assertThat(dto.getTodo(), is("청소"));
        assertThat(dto.getSubTasks().toString(), is("[방청소]"));
    }

    @Test
    public void registerTask_with_referenceInfo() throws IOException {
        Long subTask1 = registerTask("김치먹기");
        Long subTask2 = registerTask("콩나물국먹기");
        Long subTask3 = registerTask("반찬먹기");
        Long masterTask = registerTask("밥먹기");
        ReferenceTaskDto dto = new ReferenceTaskDto(SUB, new Long[]{subTask3, subTask2, subTask1});
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ReferenceTaskDto> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("/api/tasks/%d", masterTask), HttpMethod.POST, requestEntity, String.class);
        log.info("response body : {}", response.getBody());
        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertThat(taskDto.getTodo(), is("밥먹기"));
        assertThat(taskDto.getSubTasks().toString(), is("[반찬먹기, 콩나물국먹기, 김치먹기]"));
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
        // 방청소 등록 및 완료하기
        Long subTask = registerTask("책상정리");
        restTemplate.getForEntity(String.format("/api/tasks/%s/complete", subTask), String.class);

        // 청소 할일 객체 만들고 방청소 하위 참조 등록
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("방정리");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, subTask));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        Long cleaning = mapper.readValue(response.getBody(), TaskResponseDto.class).getId();

        // 청소 완료하기
        ResponseEntity<String> cleaningResponse = restTemplate.getForEntity(String.format("/api/tasks/%s/complete", cleaning), String.class);
        log.info("response body : {}", cleaningResponse.getBody());

        TaskResponseDto taskDto = mapper.readValue(cleaningResponse.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertNotNull(taskDto.getCompletedDate());
    }

    @Test
    public void completeTest_two_task_fail() throws IOException {
        // 이것은 따로 예외 처리를 해서 responseEntity로 받아오기.

        // 방청소 등록 및 완료하기
        Long subTask = registerTask("집에서놀기");
//        restTemplate.getForEntity(String.format("/api/tasks/%s/complete", cleaningRoom), String.class);

        // 청소 할일 객체 만들고 방청소 하위 참조 등록
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("놀기");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, subTask));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        Long masterTask = mapper.readValue(response.getBody(), TaskResponseDto.class).getId();

        // 청소 완료하기
        ResponseEntity<String> cleaningResponse = restTemplate.getForEntity(String.format("/api/tasks/%s/complete", masterTask), String.class);
        log.info("response body : {}", cleaningResponse.getBody());

        TaskResponseDto taskDto = mapper.readValue(cleaningResponse.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
//        assertNotNull(taskDto.getCompletedDate());
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
    }

    @Test
    public void completeTest_one_not_complete() throws IOException {
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
        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        assertNotNull(taskDto.getCompletedDate());
    }

    @Test
    public void updateTest_pass() throws IOException {
        Long today = registerTask("오늘할일");
        final String updateContent = "수정내용";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(updateContent, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("/api/tasks/%d", today), HttpMethod.PUT, requestEntity, String.class);
        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        assertThat(taskDto.getTodo(), is(updateContent));
    }

    // 예외 처리 대상
    @Test
    public void updateTest_unique_fail() throws IOException {
        Long task1 = registerTask("잠자기"); registerTask("잠안자기");
        final String updateContent = "잠안자기";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(updateContent, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("/api/tasks/%d", task1), HttpMethod.PUT, requestEntity, String.class);
        log.info("response body : {}", response.getBody());
//        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
//        assertThat(taskDto.getTodo(), is(updateContent));
    }

    public Long registerTask(String todo) {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo(todo);
        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, Task.class);
        return response.getBody().getId();
    }

}
