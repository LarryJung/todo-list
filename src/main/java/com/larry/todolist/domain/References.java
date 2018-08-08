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

    // eager를 안할 수는 없을까?
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "TASK_has_REFERENCE",
            joinColumns = @JoinColumn(name = "PARENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "REFERENCE_ID"))
    private List<Task> references = new ArrayList<>();

    public boolean isAllCompleted() {
        return references.stream().allMatch(Task::wasCompleted);
    }

    public boolean contains(Task task) {
        return references.contains(task);
    }

    public References addTask(Task referenceTask, Task counterPart) {
        if (!contains(referenceTask)) {
            references.add(referenceTask);
            referenceTask.addReferenceTask(counterPart);
            return this;
        }
        return this;
    }

    public References addAll(List<Task> tasks, Task counterPart) {
        references.addAll(tasks);
        tasks.stream().forEach(t -> t.addReferenceTask(counterPart));
        return this;
    }

    @Override
    public String toString() {
        return references.stream().map(ref -> ref.getTodo()).collect(Collectors.joining(","));
    }
}
