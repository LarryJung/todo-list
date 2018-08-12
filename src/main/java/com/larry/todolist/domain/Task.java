package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.*;
import com.larry.todolist.exceptionHandle.CannotCompleteException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task extends AbstractEntity{

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime completedDate;

    @Size(min = 2, max = 20)
    @Column(name = "TODO", nullable = false, unique = true)
    private String todo;

    @Embedded
    private Relations relations;

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
        super(id);
        this.todo = todo;
    }

    public Task registerRelations(Relations relations) {
        this.relations = relations;
        return this;
    }
//
//    public Task addSubTask(Task subTask) {
//        subTasks = subTasks.addTask(subTask);
//        subTask.addMasterTask(this);
//        return this;
//    }
//
//    private Task addMasterTask(Task masterTask) {
//        masterTasks = masterTasks.addTask(masterTask);
//        return this;
//    }
//

    public boolean wasCompleted() {
        return completedDate != null;
    }


    public Task completeTask() {
        if (relations == null) {
            this.completedDate = LocalDateTime.now();
            return this;
        }
        if (!relations.isSubTaskAllCompleted(this)) {
            throw new CannotCompleteException(String.format("아직 끝나지 않은 일들이 있습니다. Id : %s", relations.getNotCompletedSubTaskList()));
        }
        this.completedDate = LocalDateTime.now();
        return this;
    }
//
//    public Task updateTodo(String todo) {
//        this.todo = todo;
//        return this;
//    }
//
//    public IdTodoPair toIdTodoPairDto() {
//        return new IdTodoPair(id, todo);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Task task = (Task) o;
//        return Objects.equals(id, task.id) &&
//                Objects.equals(createdDate, task.createdDate) &&
//                Objects.equals(modifiedDate, task.modifiedDate) &&
//                Objects.equals(completedDate, task.completedDate) &&
//                Objects.equals(todo, task.todo) &&
//                Objects.equals(subTasks, task.subTasks) &&
//                Objects.equals(masterTasks, task.masterTasks);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(id, createdDate, modifiedDate, completedDate, todo, subTasks, masterTasks);
//    }
//
//    @Override
//    public String toString() {
//        return "Task{" +
//                "id=" + id +
//                ", createdDate=" + createdDate +
//                ", modifiedDate=" + modifiedDate +
//                ", completedDate=" + completedDate +
//                ", todo='" + todo + '\'' +
//                ", subTasks=" + subTasks +
//                ", masterTasks=" + masterTasks +
//                '}';
//    }
}
