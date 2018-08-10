package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.*;
import com.larry.todolist.exceptionHandle.CannotCompleteException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
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

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime completedDate;

    @Size(min = 2, max = 20)
    @Column(name = "TODO", nullable = false, unique = true)
    private String todo;

    @AssociationOverride(
            name = "references",
            joinTable = @JoinTable(
                    name = "MASTER_has_SUB",
                    joinColumns = @JoinColumn(name = "MASTER_ID"),
                    inverseJoinColumns = @JoinColumn(name = "SUB_ID")
            )
    )
    @Embedded
    @JsonUnwrapped(prefix = "sub_")
    private References subTasks = new References();

    // DB에 등록하지 않음.
    @Transient
    @JsonUnwrapped(prefix = "master_")
    private References masterTasks = new References();

    public static Task of(String todo) {
        return new Task(todo);
    }

    public static Task of(Long id, String todo) {
        return new Task(id, todo);
    }

    private Task(String todo) {
        this.todo = todo;
    }

    private Task(Long id, String todo) {
        this.id = id;
        this.todo = todo;
    }

    public Task addSubTask(Task subTask) {
        subTasks = subTasks.addTask(subTask);
        subTask.addMasterTask(this);
        return this;
    }

    private Task addMasterTask(Task masterTask) {
        masterTasks = masterTasks.addTask(masterTask);
        return this;
    }

    public boolean wasCompleted() {
        return completedDate != null;
    }

    public Task completeTask() {
        if (subTasks.isEmpty()) {
            this.completedDate = LocalDateTime.now();
            return this;
        }
        if (!subTasks.isAllCompleted()) {
            throw new CannotCompleteException(String.format("아직 끝나지 않은 일들이 있습니다. Id : %s", subTasks.getNotCompletedList()));
        }
        this.completedDate = LocalDateTime.now();
        return this;
    }

    public Task updateTodo(String todo) {
        this.todo = todo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(createdDate, task.createdDate) &&
                Objects.equals(modifiedDate, task.modifiedDate) &&
                Objects.equals(completedDate, task.completedDate) &&
                Objects.equals(todo, task.todo) &&
                Objects.equals(subTasks, task.subTasks) &&
                Objects.equals(masterTasks, task.masterTasks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, createdDate, modifiedDate, completedDate, todo, subTasks, masterTasks);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", completedDate=" + completedDate +
                ", todo='" + todo + '\'' +
                ", subTasks=" + subTasks +
                ", masterTasks=" + masterTasks +
                '}';
    }
}
