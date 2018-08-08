package com.larry.todolist.acceptanceTest;

import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.ReferenceTaskDto;
import com.larry.todolist.dto.TaskRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static com.larry.todolist.domain.support.TaskType.SUB;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskAcceptanceTest {

    private final Logger log = LoggerFactory.getLogger(TaskAcceptanceTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registerTask_first() {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("집안일");
        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, Task.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getTodo(), is("집안일"));
    }

    @Test
    public void registerTask_with_subTaskList() {
        Long cleaningRoom = registerTask("방청소");
        log.info("방청소 Id : {}", cleaningRoom);
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo("청소");
        taskRequestDto.setSubTasksDto(new ReferenceTaskDto(SUB, cleaningRoom));
        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, Task.class);
        assertThat(response.getBody().getTodo(), is("청소"));
        assertThat(response.getBody().getSubTasks().toString(), is("방청소"));
    }

    @Test
    public void registerTask_with_referenceInfo() {
        Long cleaningRoom = registerTask("방청소");
        Long cleaning = registerTask("청소");
        Long laundry = registerTask("빨래");
        Long chores = registerTask("집안일");
        ReferenceTaskDto dto = new ReferenceTaskDto(SUB, new Long[]{laundry, cleaning, cleaningRoom});
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ReferenceTaskDto> requestEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<Task> response = restTemplate.exchange(String.format("/api/tasks/%d", chores), HttpMethod.PUT, requestEntity, Task.class);
        assertThat(response.getBody().getTodo(), is("집안일"));
        assertThat(response.getBody().getSubTasks().toString(), is("빨래,청소,방청소"));
    }

    public Long registerTask(String todo) {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTodo(todo);
        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", taskRequestDto, Task.class);
        return response.getBody().getId();
    }
}
