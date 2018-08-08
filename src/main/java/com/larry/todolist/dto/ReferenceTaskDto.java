package com.larry.todolist.dto;

import com.larry.todolist.domain.support.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@Getter
public class ReferenceTaskDto {

    private TaskType taskType;
    private Long[] referenceTasks;

    public ReferenceTaskDto(TaskType taskType, Long referenceTask) {
        this.taskType = taskType;
        this.referenceTasks = new Long[] {referenceTask};
    }

    public ReferenceTaskDto(TaskType taskType, Long[] referenceTasks) {
        this.taskType = taskType;
        this.referenceTasks = referenceTasks;
    }

    private String referencesIdString() {
        return Arrays.stream(referenceTasks).map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    public String toString() {
        if (taskType != null && referenceTasks != null) {
            return taskType + " : " + referencesIdString();
        }
        return "";
    }
}
