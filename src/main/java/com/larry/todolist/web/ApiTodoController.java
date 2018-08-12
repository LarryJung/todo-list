package com.larry.todolist.web;

import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.requestDto.ReferenceTaskDto;
import com.larry.todolist.dto.requestDto.TaskRequestDto;
import com.larry.todolist.dto.responseDto.IdTodoPair;
import com.larry.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RequestMapping("/api/tasks")
@Controller
public class ApiTodoController {

    private final Logger log = LoggerFactory.getLogger(ApiTodoController.class);

    @Resource(name = "taskService")
    private TaskService taskService;

    @PostMapping("")
    public ResponseEntity<Task> registerTask(@RequestBody TaskRequestDto dto) {
        Task newTask = taskService.registerTask(dto);
        log.info("new Task : {}", newTask);
        URI url = URI.create(String.format("/api/tasks/%d", newTask.getId()));
        log.info("created task url : {}", url);
        return ResponseEntity.created(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(newTask);
    }

    @PostMapping("/{presentTaskId}")
    public ResponseEntity<Task> registerReferences(@PathVariable Long presentTaskId, @RequestBody ReferenceTaskDto dto) {
        log.info("참조 거는 기능 수행!!");
        Task presentTask = taskService.findById(presentTaskId);
        Task updatedTask = taskService.registerReferences(presentTask, dto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(updatedTask);
    }

    @PutMapping("/{presentTaskId}")
    public ResponseEntity<Task> updateTaskContent(@PathVariable Long presentTaskId, @RequestBody String todo) {
        Task presentTask = taskService.findById(presentTaskId);
        Task updatedTask = taskService.update(presentTask, todo);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(updatedTask);
    }

    @GetMapping("/{presentTaskId}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long presentTaskId) {
        log.info("일을 끝내러 왔습니다. {}", presentTaskId);
        Task updatedTask = taskService.complete(presentTaskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(updatedTask);
    }

    @GetMapping("/autoComplete")
    public ResponseEntity<List<Task>> autoComplete(@RequestParam String candidateParam) {
        log.info("candidata param : {}", candidateParam);
        List<Task> canditates = taskService.findCandidatesByTodo(candidateParam);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(canditates);
    }

    @GetMapping("")
    public ResponseEntity<List<Task>> findAllByComplete(@RequestParam boolean complete) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(taskService.findAll(complete));
    }



}
