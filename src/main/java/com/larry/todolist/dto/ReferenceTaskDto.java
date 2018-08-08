package com.larry.todolist.dto;

import com.larry.todolist.domain.support.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ReferenceTaskDto {

    private TaskType taskType;
    private Long[] referenceTasks;

    public boolean hasReference() {
        return referenceTasks.length > 0;
    }
}
