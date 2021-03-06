package com.larry.todolist.web;

import com.larry.todolist.domain.Task;
import com.larry.todolist.domain.paging.PageResult;
import com.larry.todolist.domain.dto.requestDto.ReferenceTaskDto;
import com.larry.todolist.domain.dto.requestDto.TaskRequestDto;
import com.larry.todolist.domain.dto.requestDto.UpdateDto;
import com.larry.todolist.domain.dto.responseDto.ReferenceShowDto;
import com.larry.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;

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
        Task updatedTask = taskService.registerTask(presentTask, TaskRequestDto.of(dto));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(updatedTask);
    }

    @PutMapping(value = "/{presentTaskId}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Task> updateTaskContent(@PathVariable Long presentTaskId, UpdateDto updateDto) {
        log.info("update 입니다 : {}", updateDto);
        Task updatedTask = taskService.update(updateDto);
        return ResponseEntity.ok()
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

    @GetMapping("{presentTaskId}/references")
    public ResponseEntity<ReferenceShowDto> findRelations(@PathVariable Long presentTaskId) {
        log.info("show reference : {}", presentTaskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(taskService.findRelations(presentTaskId));
    }

    @GetMapping("")
    public ResponseEntity<PageResult> pageRequestByComplete(Pageable pageable, @RequestParam boolean complete) {
        log.info("complete {}", complete);
        PageResult pageResult = taskService.findPageByComplete(pageable, complete);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(pageResult);
    }

    @DeleteMapping("{presentTaskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long presentTaskId) {
        taskService.delete(presentTaskId);
        return ResponseEntity.ok().build();
    }

}
