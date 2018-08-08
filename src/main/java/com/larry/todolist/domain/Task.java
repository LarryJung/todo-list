package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@NoArgsConstructor
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "TODO", nullable = false)
    private String todo;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime completedDate;

    @Embedded
    private References references;

    public static Task of(String todo) {
        return new Task(todo);
    }

    private Task (String todo) {
        this.todo = todo;
        this.references = new References();
    }

    public Task addReferenceTask(Task referenceTask) {
        this.references = references.addTask(referenceTask, this);
        return this;
    }

    public Task addReferenceTask(Task ... tasks) {
        this.references = references.addAll(Arrays.asList(tasks), this);
        return this;
    }

    public boolean wasCompleted() {
        return completedDate != null;
    }

    public Task completeTask() {
        if (references.isAllCompleted()) {
            this.completedDate = LocalDateTime.now();
            return this;
        }
        throw new RuntimeException("아직 끝나지 않은 일들이 있습니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(createdDate, task.createdDate) &&
                Objects.equals(modifiedDate, task.modifiedDate) &&
                Objects.equals(todo, task.todo) &&
                Objects.equals(references, task.references);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, modifiedDate, todo, references);
    }
}
