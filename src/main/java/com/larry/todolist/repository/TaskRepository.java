package com.larry.todolist.repository;

import com.larry.todolist.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>{

    Optional<Task> findByTodo(String Todo);

}
