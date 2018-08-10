package com.larry.todolist.service;

import com.larry.todolist.domain.Task;
import com.larry.todolist.domain.support.TaskType;
import com.larry.todolist.dto.requestDto.ReferenceTaskDto;
import com.larry.todolist.dto.requestDto.TaskRequestDto;
import com.larry.todolist.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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

    public Task registerTask(TaskRequestDto dto) {
        Task afterRegisterMaster = registerReferences(dto.toEntity(), dto.getMasterTasksDto());
        return registerReferences(afterRegisterMaster, dto.getSubTasksDto());
    }

    public Task registerReferences(Task presentTask, ReferenceTaskDto references) {
        if (references == null) {
            log.info("reference is null");
            return save(presentTask);
        }
        if (references.getTaskType().equals(TaskType.SUB)) {
            Arrays.stream(references.getReferenceTasks()).forEach(r -> presentTask.addSubTask(findById(r)));
        }
        if (references.getTaskType().equals(TaskType.MASTER)) {
            Arrays.stream(references.getReferenceTasks()).forEach(r -> findById(r).addSubTask(presentTask));
        }
        return save(presentTask);

//
//        Method method = references.getTaskType().retrieveMethod(presentTask);
//        log.info("this method : {}", method.toString());
//        Arrays.stream(references.getReferenceTasks()).forEach(l -> {
//            try {
//                method.invoke(findById(l), presentTask);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        });
//        return save(presentTask);
    }

    private Task save(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task complete(Long presentTaskId) {
        return findById(presentTaskId).completeTask();
    }

    @Transactional // unique 제약조건 때문에 겹치면 에러가 나겠지?
    public Task update(Task presentTask, String todo) {
        return presentTask.updateTodo(todo);
    }
}
