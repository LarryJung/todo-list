package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.*;
import com.larry.todolist.domain.support.AbstractEntity;
import com.larry.todolist.exceptionHandle.CannotCompleteException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task extends AbstractEntity {

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime completedDate;

    @Size(min = 2, max = 20)
    @Column(name = "TODO", nullable = false, unique = true)
    private String todo;

    @JsonIgnore
    @Embedded
    private Relations relations;

    private Task(String todo) {
        this.todo = todo;
    }

    private Task(Long id, String todo) {
        super(id);
        this.todo = todo;
    }

    private Task(String todo, Relations relations) {
        this.todo = todo;
        this.relations = relations;
    }

    public static Task of(String todo) {
        return new Task(todo);
    }

    public static Task of(String todo, Relations relations) {
        return new Task(todo, relations);
    }

    public static Task of(Long id, String todo) {
        return new Task(id, todo);
    }

    public Task registerRelation(Relation relation) {
        if (relations == null) {
            this.relations = new Relations();
        }
        this.relations = relations.addRelation(relation);
        return this;
    }

    public Task registerRelations(Relations relations) {
        this.relations = relations;
        return this;
    }

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

    public Task updateTodo(String todo) {
        this.todo = todo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(completedDate, task.completedDate) &&
                Objects.equals(todo, task.todo) &&
                Objects.equals(relations, task.relations);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), completedDate, todo, relations);
    }

    @Override
    public String toString() {
        return "Task{" +
                "completedDate=" + completedDate +
                ", todo='" + todo + '\'' +
                ", relations=" + relations +
                '}';
    }

}
