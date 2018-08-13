package com.larry.todolist.domain.repository;

import com.larry.todolist.domain.Relation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findAllBySubId(Long subId);
    List<Relation> findAllByMasterId(Long masterId);

}
