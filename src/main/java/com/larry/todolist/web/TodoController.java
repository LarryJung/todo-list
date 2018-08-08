package com.larry.todolist.web;

import com.larry.todolist.domain.Task;
import com.larry.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.net.URI;

@RequestMapping("/api/tasks")
@Controller
public class TodoController {

    private final Logger log = LoggerFactory.getLogger(TodoController.class);

    @Resource(name = "taskService")
    private TaskService taskService;

    @PostMapping("")
    public ResponseEntity<Task> registerTask(String todo) {
        log.info("new task : {}", todo);
        Task newTask = taskService.save(Task.of(todo));
        URI url = URI.create(String.format("/api/tasks/%d", newTask.getId());
        return ResponseEntity.created(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(newTask);
    }


}
