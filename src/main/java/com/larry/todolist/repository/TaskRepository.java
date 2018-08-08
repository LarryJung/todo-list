package com.larry.todolist.repository;

import com.larry.todolist.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Long, Task>{
}
