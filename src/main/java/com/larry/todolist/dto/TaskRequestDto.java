package com.larry.todolist.dto;

import com.larry.todolist.domain.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@Setter
@Getter
public class TaskRequestDto {

    private String todo;
    private Long[] references;

    public Task toEntity() {
        return Task.of(todo);
    }

    public boolean hasReferences() {
        return references.length > 0;
    }

    @Override
    public String toString() {
        return "TaskRequestDto{" +
                "todo='" + todo + '\'' +
                ", references=" + Arrays.toString(references) +
                '}';
    }

}
