package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
//        if (this.subTasks == null) {
//            System.out.println("서브 테스크가 널이었으므로 초기화합니다.");
//            this.subTasks = new References();
//        }
        this.subTasks = subTasks.addTask(subTask);
        subTask.addMasterTask(this);
        return this;
    }

    private Task addMasterTask(Task task) {
//        if (masterTasks == null) {
//            this.masterTasks = new References();
//        }
        this.masterTasks.addTask(task);
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
            throw new RuntimeException(String.format("아직 끝나지 않은 일들이 있습니다. Id : ", subTasks.getNotCompletedList()));
        }
        this.completedDate = LocalDateTime.now();
        return this;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public String getTodo() {
        return todo;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public References getSubTasks() {
        return subTasks;
    }

    public References getMasterTasks() {
        return masterTasks;
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
                Objects.equals(todo, task.todo) &&
                Objects.equals(completedDate, task.completedDate) &&
                Objects.equals(subTasks, task.subTasks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, createdDate, modifiedDate, todo, completedDate, subTasks);
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
                '}';
    }
}
