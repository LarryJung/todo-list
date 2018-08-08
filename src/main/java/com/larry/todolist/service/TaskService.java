package com.larry.todolist.service;

import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.ReferenceTaskDto;
import com.larry.todolist.dto.TaskRequestDto;
import com.larry.todolist.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Resource(name = "taskRepository")
    private TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Task findByTodo(String todo) {
        return taskRepository.findByTodo(todo).orElseThrow(EntityNotFoundException::new);
    }

    // tansactional??
    public Task save(TaskRequestDto dto) {
        Task task = registerReferences(dto.toEntity(), dto.getMasterTasksDto());
        log.info("masterTask 저장했음 id : {}", task.getId());
        return registerReferences(task, dto.getSubTasksDto());
    }

    public Task registerReferences(Task presentTask, ReferenceTaskDto references) {
        if (references == null) {
            log.info("reference is null");
            return save(presentTask);
        }
        Method method = references.getTaskType().retrieveMethod(presentTask);
        log.info("this method : {}", method.toString());
        Arrays.stream(references.getReferenceTasks()).forEach(l -> {
            try {
                method.invoke(findById(l), presentTask);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return save(presentTask);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
