package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @DateTimeFormat
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @CreatedDate
    private LocalDateTime createdDate;

//    @DateTimeFormat
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "TODO", nullable = false)
    private String todo;

//    @DateTimeFormat
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
    @JsonUnwrapped(prefix = "sub_")
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
    @JsonUnwrapped(prefix = "master_")
    private References masterTasks;

    public static Task of(String todo) {
        return new Task(todo);
    }

    private Task(String todo) {
        this.todo = todo;
    }

    public Task addSubTask(Task subTask) {
        if (this.subTasks == null) {
            System.out.println("서브 테스크가 널이었으므로 초기화합니다.");
            this.subTasks = new References();
        }
        this.subTasks = subTasks.addSubTask(subTask, this);
        return this;
    }

    public Task addMasterTask(Task masterTask) {
        if (this.masterTasks == null) {
            System.out.println("마스터 테스크가 널이었으므로 초기화합니다.");
            this.masterTasks = new References();
        }
        this.masterTasks = masterTasks.addMasterTask(masterTask, this);
        return this;
    }

    public boolean wasCompleted() {
        return completedDate != null;
    }

    public Task completeTask() {
        System.out.println("Present task? " + todo);
        System.out.println("Present sub? " + subTasks);
        if (subTasks == null) { // 이게 == null 인줄 알았는데 언제 초기화가 되는 거지???
            System.out.println("서브 테스크는 널 입니다. 그냥 완료 처리 하겠습니다.");
            this.completedDate = LocalDateTime.now();
            return this;


        }
        if (subTasks.isAllCompleted()) {
            System.out.println("서브 테스크들이 모두 완료되었습니다. 완료 처리 하겠습니다.");
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
}
