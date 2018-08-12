package com.larry.todolist.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larry.todolist.domain.IdRelation;
import com.larry.todolist.domain.Relation;
import com.larry.todolist.domain.Relations;
import com.larry.todolist.domain.Task;
import com.larry.todolist.domain.support.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    private TaskRequestDto (ReferenceTaskDto masterTasksDto, ReferenceTaskDto subTasksDto) {
        this.masterTasksDto = masterTasksDto;
        this.subTasksDto = subTasksDto;
    }

    public static TaskRequestDto of(ReferenceTaskDto tasksDto) {
        if (tasksDto.getTaskType().equals(TaskType.MASTER)) {
            return new TaskRequestDto(tasksDto, null);
        }
        if (tasksDto.getTaskType().equals(TaskType.SUB)) {
            return new TaskRequestDto(null, tasksDto);
        }
        throw new RuntimeException("잘못된 요청입니다.");
    }

    public static TaskRequestDto of(ReferenceTaskDto masterTasksDto, ReferenceTaskDto subTasksDto) {
        return new TaskRequestDto(masterTasksDto, subTasksDto);
    }

    public Task toEntity() {
        if (masterTasksDto != null && subTasksDto != null) {
            masterTasksDto.checkDuplicates(subTasksDto);
        }
        return Task.of(todo);
    }

    public List<IdRelation> makeIdRelations(Long newTaskId) {
        List<IdRelation> relations = new ArrayList<>();
        if (masterTasksDto != null) {
            relations.addAll(masterTasksDto.makeIdRelationsMaster(newTaskId));
        }
        if (subTasksDto != null) {
            relations.addAll(subTasksDto.makeIdRelationsSub(newTaskId));
        }
        return relations;
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