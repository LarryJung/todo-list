package com.larry.todolist.service;

import com.larry.todolist.domain.*;
import com.larry.todolist.dto.requestDto.TaskRequestDto;
import com.larry.todolist.dto.requestDto.UpdateDto;
import com.larry.todolist.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Resource(name = "taskRepository")
    private TaskRepository taskRepository;

    @Resource(name = "relationRepository")
    private RelationRepository relationRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Task findByTodo(String todo) {
        return taskRepository.findByTodo(todo).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Task registerTask(Task presentTask, TaskRequestDto dto) {
        log.info("input dto : {}", dto);
        List<IdRelation> newRelations = dto.makeIdRelations(presentTask.getId());
        return registerReferences(presentTask, newRelations);
    }

    @Transactional
    public Task registerTask(TaskRequestDto dto) {
        log.info("input dto : {}", dto);
        Task newTask = taskRepository.save(dto.toEntity());
        List<IdRelation> newRelations = dto.makeIdRelations(newTask.getId());
        return registerReferences(newTask, newRelations);
    }

    public Task registerReferences(Task presentTask, List<IdRelation> idRelations) {
        idRelations.forEach(this::checkRelation);
        idRelations.forEach(r -> r.registerRelation(taskRepository, relationRepository));
        return presentTask;
    }

    private void checkRelation(IdRelation r) {
        relationRepository.findAllBySubId(r.getMasterId())
                .forEach(ref -> relationRepository.findAllBySubId(ref.getMaster().getId())
                        .forEach(rr -> {
                            if (rr.getMaster().equals(findById(r.getSubId()))) {
                                throw new RuntimeException("참조관계가 이상합니다.");
                            }
                        }));
    }

    @Transactional
    public Task complete(Long presentTaskId) {
        return findById(presentTaskId).completeTask();
    }

//    public List<Task> findCandidatesByTodo(String param) {
//        return taskRepository.findAllByTodoContains(param);
//    }
//
//    public List<IdTodoPair> findAllIdAndTodo() {
//        return findAll().stream().map(t -> t.toIdTodoPairDto()).collect(Collectors.toList());
//
//    }

    public List<Task> findAll(boolean complete) {
        if (complete) {
            return taskRepository.findAllByCompletedDateIsNotNull();
        }
        return taskRepository.findAllByCompletedDateIsNull();
    }

    @Transactional
    public Task update(UpdateDto updateDto) {
        return findById(updateDto.getPk()).updateTodo(updateDto.getValue());
    }
}
