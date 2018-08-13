package com.larry.todolist.domain.repository;

import com.larry.todolist.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>{

    Optional<Task> findByTodo(String Todo);
    List<Task> findAllByTodoContains(String todo);
//    List<Task> findAllByCompletedDateIsNotNull();
//    List<Task> findAllByCompletedDateIsNull();

    Page<Task> findAllByCompletedDateIsNotNull(Pageable pageable);
    Page<Task> findAllByCompletedDateIsNull(Pageable pageable);

    long countByCompletedDateIsNotNull();
    long countByCompletedDateIsNull();
}
