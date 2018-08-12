package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Embeddable
public class References {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"subTasks", "masterTasks", "createdDate", "modifiedDate", "completedDate"})
    private List<Task> tasks = new ArrayList<>();

    public References(List<Task> tasks) {
        this.tasks = tasks;
    }
//
//    @JsonIgnore
//    public boolean isAllCompleted() {
//        return tasks.stream().allMatch(Task::wasCompleted);
//    }
//
//    public References addTask(Task task) {
//        if (!tasks.contains(task)) {
//            tasks.add(task);
//            return this;
//        }
//        return this;
//    }
//
//    @JsonIgnore
//    public List<Long> getNotCompletedList() {
//        return tasks.stream()
//                .filter(r -> !r.wasCompleted())
//                .map(Task::getId)
//                .collect(Collectors.toList());
//    }

    @JsonIgnore
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    @Override
    public String toString() {
        return "References{" +
                "tasks=" + tasks.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        References that = (References) o;
        return Objects.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tasks);
    }
}
