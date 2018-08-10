package com.larry.todolist.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larry.todolist.domain.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@NoArgsConstructor
@Setter
@Getter
public class TaskRequestDto {

    @JsonProperty(value = "todo")
    private String todo;

    @JsonProperty(value = "masterTasksDto")
    private ReferenceTaskDto masterTasksDto;

    @JsonProperty(value = "subTasksDto")
    private ReferenceTaskDto subTasksDto;

    public Task toEntity() {
        return Task.of(todo);
    }

    @Override
    public String toString() {
        return "TaskRequestDto{" +
                "todo='" + todo + '\'' +
                ", masterTasksDto=" + masterTasksDto +
                ", subTasksDto=" + subTasksDto +
                '}';
    }
}
