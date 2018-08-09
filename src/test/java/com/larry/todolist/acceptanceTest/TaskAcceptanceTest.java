package com.larry.todolist.acceptanceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.requestDto.ReferenceTaskDto;
import com.larry.todolist.dto.requestDto.TaskRequestDto;
import com.larry.todolist.dto.responseDto.TaskResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskAcceptanceTest {

    private final Logger log = LoggerFactory.getLogger(TaskAcceptanceTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    public void registerTask_first() throws IOException {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("집안일");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        log.info("response body : {}", response.getBody());

        TaskResponseDto dto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        assertThat(dto.getTodo(), is("집안일"));
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
        Long cleaningRoom = registerTask("방청소");
        Long cleaning = registerTask("청소");
        Long laundry = registerTask("빨래");
        Long chores = registerTask("집안일");
        ReferenceTaskDto dto = new ReferenceTaskDto(SUB, new Long[]{laundry, cleaning, cleaningRoom});
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ReferenceTaskDto> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("/api/tasks/%d", chores), HttpMethod.PUT, requestEntity, String.class);
        log.info("response body : {}", response.getBody());
        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertThat(taskDto.getTodo(), is("집안일"));
        assertThat(taskDto.getSubTasks().toString(), is("[빨래, 청소, 방청소]"));
    }

    @Test
    public void completeTest_for_one_task_pass() throws IOException {
        Long cleaningRoom = registerTask("방청소");
        ResponseEntity<String> response = restTemplate.getForEntity(String.format("/api/tasks/%s/complete", cleaningRoom), String.class);
        log.info("response body : {}", response.getBody());
        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertNotNull(taskDto.getCompletedDate());
    }

    @Test
    public void completeTest_two_task_pass() throws IOException {
        // 방청소 등록 및 완료하기
        Long cleaningRoom = registerTask("방청소");
        restTemplate.getForEntity(String.format("/api/tasks/%s/complete", cleaningRoom), String.class);

        // 청소 할일 객체 만들고 방청소 하위 참조 등록
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("청소");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, cleaningRoom));
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
        Long cleaningRoom = registerTask("방청소");
//        restTemplate.getForEntity(String.format("/api/tasks/%s/complete", cleaningRoom), String.class);

        // 청소 할일 객체 만들고 방청소 하위 참조 등록
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("청소");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, cleaningRoom));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, String.class);
        Long cleaning = mapper.readValue(response.getBody(), TaskResponseDto.class).getId();

        // 청소 완료하기
        ResponseEntity<String> cleaningResponse = restTemplate.getForEntity(String.format("/api/tasks/%s/complete", cleaning), String.class);
        log.info("response body : {}", cleaningResponse.getBody());

        TaskResponseDto taskDto = mapper.readValue(cleaningResponse.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
//        assertNotNull(taskDto.getCompletedDate());
    }

    @Test
    public void completeTest_all_task_pass() throws IOException {
        Long cleaningRoom = registerTask("방청소");
        Long cleaning = registerTask("청소");
        Long laundry = registerTask("빨래");
        Long chores = registerTask("집안일");

        HttpHeaders headers = new HttpHeaders();
        ReferenceTaskDto dtoForChores = new ReferenceTaskDto(SUB, new Long[]{laundry, cleaning, cleaningRoom});
        HttpEntity<ReferenceTaskDto> requestEntityForChores = new HttpEntity<>(dtoForChores, headers);
        restTemplate.exchange(String.format("/api/tasks/%d", chores), HttpMethod.PUT, requestEntityForChores, String.class);

        ReferenceTaskDto dtoForCleaning = new ReferenceTaskDto(SUB, new Long[]{cleaningRoom});
        HttpEntity<ReferenceTaskDto> requestEntityForCleaning = new HttpEntity<>(dtoForCleaning, headers);
        restTemplate.exchange(String.format("/api/tasks/%d", cleaning), HttpMethod.PUT, requestEntityForCleaning, String.class);

        // careful to order
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", cleaningRoom), String.class);
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", cleaning), String.class);
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", laundry), String.class);

        ResponseEntity<String> response = restTemplate.getForEntity(String.format("/api/tasks/%d/complete", chores), String.class);
        log.info("response body : {}", response.getBody());

        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertNotNull(taskDto.getCompletedDate());
    }

    @Test
    public void completeTest_one_not_complete() throws IOException {
        Long cleaningRoom = registerTask("방청소");
        Long cleaning = registerTask("청소");
        Long laundry = registerTask("빨래");
        Long chores = registerTask("집안일");

        HttpHeaders headers = new HttpHeaders();
        ReferenceTaskDto dtoForChores = new ReferenceTaskDto(SUB, new Long[]{laundry, cleaning, cleaningRoom});
        HttpEntity<ReferenceTaskDto> requestEntityForChores = new HttpEntity<>(dtoForChores, headers);
        restTemplate.exchange(String.format("/api/tasks/%d", chores), HttpMethod.PUT, requestEntityForChores, String.class);

        ReferenceTaskDto dtoForCleaning = new ReferenceTaskDto(SUB, new Long[]{cleaningRoom});
        HttpEntity<ReferenceTaskDto> requestEntityForCleaning = new HttpEntity<>(dtoForCleaning, headers);
        restTemplate.exchange(String.format("/api/tasks/%d", cleaning), HttpMethod.PUT, requestEntityForCleaning, String.class);

        // careful to order
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", cleaningRoom), String.class);
//        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", cleaning), String.class);
        restTemplate.getForEntity(String.format("/api/tasks/%d/complete", laundry), String.class);

        ResponseEntity<String> response = restTemplate.getForEntity(String.format("/api/tasks/%d/complete", chores), String.class);
        log.info("response body : {}", response.getBody());

        TaskResponseDto taskDto = mapper.readValue(response.getBody(), TaskResponseDto.class);
        log.info("dto : {}", taskDto);
        assertNotNull(taskDto.getCompletedDate());
    }

    public Long registerTask(String todo) {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo(todo);
        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, Task.class);
        return response.getBody().getId();
    }

    // 수정 기능 구현
}
