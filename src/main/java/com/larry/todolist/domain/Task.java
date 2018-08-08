package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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

    @AssociationOverride(
            name = "references",
            joinTable = @JoinTable(
                    name = "MASTER_has_SUB",
                    joinColumns = @JoinColumn(name = "MASTER_ID"),
                    inverseJoinColumns = @JoinColumn(name = "SUB_ID")
            )
    )
    @Embedded
    private References subTasks;

    @AssociationOverride(
            name = "references",
            joinTable = @JoinTable(
                    name = "SUB_has_MASTER",
                    joinColumns = @JoinColumn(name = "SUB_ID"),
                    inverseJoinColumns = @JoinColumn(name = "MASTER_ID")
            )
    )
    @Embedded
    @JsonIgnore
    private References masterTasks;

    public static Task of(String todo) {
        return new Task(todo);
    }

    private Task(String todo) {
        this.todo = todo;
    }

    public Task addSubTask(Task subTask) {
        if (this.subTasks == null) {
            this.subTasks = new References();
        }
        this.subTasks = subTasks.addSubTask(subTask, this);
        return this;
    }

    public Task addMasterTask(Task masterTask) {
        if (this.masterTasks == null) {
            this.masterTasks = new References();
        }
        this.masterTasks = masterTasks.addMasterTask(masterTask, this);
        return this;
    }

    public boolean wasCompleted() {
        return completedDate != null;
    }

    public Task completeTask() {
        if (subTasks == null) {
            this.completedDate = LocalDateTime.now();
            return this;
        }
        if (subTasks.isAllCompleted()) {
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
                Objects.equals(completedDate, task.completedDate) &&
                Objects.equals(subTasks, task.subTasks) &&
                Objects.equals(masterTasks, task.masterTasks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, createdDate, modifiedDate, todo, completedDate, subTasks, masterTasks);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", todo='" + todo + '\'' +
                ", completedDate=" + completedDate +
                ", subTasks=" + subTasks +
                ", masterTasks=" + masterTasks +
                '}';
    }
}
