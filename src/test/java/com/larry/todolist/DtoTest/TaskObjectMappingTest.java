//package com.larry.todolist.DtoTest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.larry.todolist.domain.Task;
//import com.larry.todolist.dto.responseDto.TaskResponseDto;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//public class TaskObjectMappingTest {
//
//    private final Logger log = LoggerFactory.getLogger(TaskObjectMappingTest.class);
//
//    private ObjectMapper mapper = new ObjectMapper()
//            .registerModule(new JavaTimeModule())
//            .enable(SerializationFeature.INDENT_OUTPUT);
//
//    @Test
//    public void simpleTask() throws IOException {
//        Task task = Task.of(1L, "밥먹기");
//        task.completeTask();
//        String jsonArray = mapper.writeValueAsString(task);
//        String serializedJson = jsonThroughMapper(jsonArray);
//        log.info("json : {}", jsonArray);
//        log.info("serializedJson : {}", serializedJson);
//        assertThat(jsonArray, is(serializedJson));
//    }
//
//    @Test
//    public void twoDependenciesTask() throws IOException {
//        Task cleaningRoom = Task.of(1L, "방청소");
//        cleaningRoom.completeTask();
//        Task cleaning = Task.of(2L, "청소").addSubTask(cleaningRoom);
//        cleaning.completeTask();
//        String jsonArray = mapper.writeValueAsString(cleaning);
//        String serializedJson = jsonThroughMapper(jsonArray);
//        log.info("json : {}", jsonArray);
//        log.info("serialized json : {}", serializedJson);
//        assertThat(jsonArray, is(serializedJson));
//    }
//
//    @Test
//    public void taskRequestDtoSerializeTest() throws IOException {
//        TaskResponseDto responseDto = new TaskResponseDto();
//        responseDto.setId(1L);
//        responseDto.setCompletedDate(LocalDateTime.now());
//        responseDto.setTodo("청소");
//        responseDto.setSubTasks(Arrays.asList(new TaskResponseDto.RefTask(2L, "방청소"), new TaskResponseDto.RefTask(3L, "거실청소")));
//
//        String jsonArray = mapper.writeValueAsString(responseDto);
//        String serializedJson = jsonThroughMapper(jsonArray);
//        log.info("json : {}", jsonArray);
//        log.info("serialized json : {}", serializedJson);
//        assertThat(jsonArray, is(serializedJson));
//    }
//
//    private String jsonThroughMapper(String jsonArray) throws IOException {
//        TaskResponseDto responseDto = mapper.readValue(jsonArray, TaskResponseDto.class);
//        return mapper.writeValueAsString(responseDto);
//    }
//}
