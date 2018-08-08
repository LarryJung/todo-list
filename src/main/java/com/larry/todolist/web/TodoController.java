package com.larry.todolist.web;

import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.ReferenceTaskDto;
import com.larry.todolist.dto.TaskRequestDto;
import com.larry.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;

@RequestMapping("/api/tasks")
@Controller
public class TodoController {

    private final Logger log = LoggerFactory.getLogger(TodoController.class);

    @Resource(name = "taskService")
    private TaskService taskService;

    @PostMapping("")
    public ResponseEntity<Task> registerTask(@RequestBody TaskRequestDto dto) {
        log.info("new task : {}", dto);
        Task newTask = taskService.save(dto);
        URI url = URI.create(String.format("/api/tasks/%d", newTask.getId()));
        log.info("created task url : {}", url);
        return ResponseEntity.created(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(newTask);
    }

    @PostMapping("/{presentTaskId}")
    public ResponseEntity<Task> registerReferences(@PathVariable Long presentTaskId, @RequestBody ReferenceTaskDto dto) {
        Task presentTask = taskService.findById(presentTaskId);
        Task updateTask = taskService.registerReferences(presentTask, dto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(updateTask);
    }



}
