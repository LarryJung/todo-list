package com.larry.todolist.domain;

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
    private List<Task> references = new ArrayList<>();

    public boolean isAllCompleted() {
        return references.stream().allMatch(Task::wasCompleted);
    }

    public boolean contains(Task task) {
        return references.contains(task);
    }

    public References addSubTask(Task subTask, Task masterTask) {
        if (!contains(subTask)) {
            references.add(subTask);
            subTask.addMasterTask(masterTask);
            return this;
        }
        return this;
    }

    public References addMasterTask(Task masterTask, Task subTask) {
        if (!contains(masterTask)) {
            references.add(masterTask);
            masterTask.addSubTask(subTask);
            return this;
        }
        return null;
    }

    @Override
    public String toString() {
        return references.stream().map(ref -> ref.getTodo()).collect(Collectors.joining(","));
    }

}
