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
    private List<Task> references;

    public References(List<Task> references) {
        this.references = references;
    }

    @JsonIgnore
    public boolean isAllCompleted() {
        return references.stream().allMatch(Task::wasCompleted);
    }

    public boolean contains(Task task) {
        return references.contains(task);
    }

    public References addSubTask(Task subTask, Task masterTask) {
        if (references == null) {
            references = new ArrayList<>();
        }
        if (!contains(subTask)) {
            references.add(subTask);
            subTask.addMasterTask(masterTask);
            return this;
        }
        return this;
    }

    public References addMasterTask(Task masterTask, Task subTask) {
        if (references == null) {
            references = new ArrayList<>();
        }
        if (!contains(masterTask)) {
            references.add(masterTask);
            masterTask.addSubTask(subTask);
            return this;
        }
        return null;
    }

    public List<Long> getNotCompletedList() {
        return references.stream()
                .filter(r -> !r.wasCompleted())
                .map(r -> r.getId())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return references.stream().map(Task::getTodo).collect(Collectors.joining(","));
    }
}
