package com.larry.todolist.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Embeddable
public class References {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"subTasks", "masterTasks", "createdDate", "modifiedDate", "completedDate"})
    private List<Task> tasks;

    public References(List<Task> tasks) {
        this.tasks = tasks;
    }

    @JsonIgnore
    public boolean isAllCompleted() {
        return tasks.stream().allMatch(Task::wasCompleted);
    }

    public boolean contains(Task task) {
        return tasks.contains(task);
    }

    public References addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        if (!contains(task)) {
            tasks.add(task);
            return this;
        }
        return this;
    }

    @JsonIgnore
    public List<Long> getNotCompletedList() {
        return tasks.stream()
                .filter(r -> !r.wasCompleted())
                .map(Task::getId)
                .collect(Collectors.toList());
    }

//    @Override
//    public String toString() {
//        return tasks.stream().map(Task::getTodo).collect(Collectors.joining(","));
//    }


    @Override
    public String toString() {
        return "References{" +
                "tasks=" + tasks +
                '}';
    }

    @JsonIgnore
    public boolean isEmpty() {
        return tasks == null;
    }
}
