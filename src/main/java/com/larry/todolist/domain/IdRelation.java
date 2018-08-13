package com.larry.todolist.domain;

import com.larry.todolist.domain.repository.RelationRepository;
import com.larry.todolist.domain.repository.TaskRepository;
import lombok.Data;
import lombok.Getter;

import javax.persistence.EntityNotFoundException;

@Data
@Getter
public class IdRelation {

    private Long masterId;

    private Long subId;

    public IdRelation(Long masterId, Long subId) {
        this.masterId = masterId;
        this.subId = subId;
    }

    // I think it's somewhat weird...
    public void registerRelation(TaskRepository taskRepository, RelationRepository relationRepository) {
        Task master = taskRepository.findById(masterId).orElseThrow(EntityNotFoundException::new);
        Task sub = taskRepository.findById(subId).orElseThrow(EntityNotFoundException::new);
        Relation relation = Relation.masterAndSub(master, sub);
        master.registerRelation(relation);
        sub.registerRelation(relation);
        relationRepository.save(relation);
    }
}
