package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
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

    // eager를 안할 수는 없을까?
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "TASK_has_CHILDREN",
    joinColumns = @JoinColumn(name = "PARENT_ID"),
    inverseJoinColumns = @JoinColumn(name = "CHILD_ID"))
    private List<Task> children;

    public static Task of(String todo) {
        return new Task(todo, new ArrayList<>());
    }

    @Builder
    public Task(String todo, List<Task> children) {
        this.todo = todo;
        this.children = children;
    }

    private boolean contains(Task task) {
        return this.children.contains(task);
    }

    public Task addSubTask(Task subTask) {
        System.out.println("들어온 테스크 " + subTask);
        children.add(subTask);
        return this;
    }

    public Task addSubTask(Task ... tasks) {
        System.out.println("children " + children);
        System.out.println("들오온 테슼스 " + tasks);
        children.addAll(Arrays.asList(tasks));
        return this;
    }

    public String toChildrenString() {
        StringBuilder sb = new StringBuilder();
        for (Task task : children) {
            sb.append(task.getTodo());
        }
        return sb.toString();
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
                Objects.equals(children, task.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, modifiedDate, todo, children);
    }
}
