package com.larry.todolist.service;


import com.larry.todolist.domain.Task;
import com.larry.todolist.dto.TaskRequestDto;
import com.larry.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskService {

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
        Task newTask = dto.toEntity();
        if (dto.hasReferences()) {
            Arrays.stream(dto.getReferences()).forEach(ref -> findById(ref).addReferenceTask(newTask));
        }
        return taskRepository.save(newTask);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void registerReferences(Long taskId, Long ... references) {
        Task presentTask = findById(taskId);
        Arrays.stream(references).forEach(l -> findById(l).addReferenceTask(presentTask));
    }
}
