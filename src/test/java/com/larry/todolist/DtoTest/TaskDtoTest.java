package com.larry.todolist.DtoTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.responseDto.TaskResponseDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class TaskDtoTest {

    private final Logger log = LoggerFactory.getLogger(TaskDtoTest.class);

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    public void simpleTask() throws IOException {
        Task task = Task.of("밥먹기");
        task.completeTask();

        String jsonArray = mapper.writeValueAsString(task);
        log.info("json : {}", jsonArray);

        TaskResponseDto responseDto = mapper.readValue(jsonArray, TaskResponseDto.class);
        log.info("deserialized dto : {}", responseDto);
    }

    @Test
    public void twoDependenciesTask() throws IOException {
        Task cleaningRoom = Task.of("방청소");
        cleaningRoom.completeTask();
        Task cleaning = Task.of("청소").addSubTask(cleaningRoom);
        cleaning.completeTask();

        String jsonArray = mapper.writeValueAsString(cleaning);
        log.info("json : {}", jsonArray);

        TaskResponseDto responseDto = mapper.readValue(jsonArray, TaskResponseDto.class);
        log.info("deserialized dto : {}", responseDto);

        String serializedJson = mapper.writeValueAsString(responseDto);
        log.info("serialized json : {}", serializedJson);
    }

    @Test
    public void taskRequestDtoSerializeTest() throws JsonProcessingException {
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(1L);
        responseDto.setCompletedDate(LocalDateTime.now());
        responseDto.setTodo("청소");
        responseDto.setSubTasks(Arrays.asList(new TaskResponseDto.RefTask(2L, "방청소"), new TaskResponseDto.RefTask(3L, "거실청소")));

        String jsonArray = mapper.writeValueAsString(responseDto);
        log.info("json : {}", jsonArray);
    }
}
