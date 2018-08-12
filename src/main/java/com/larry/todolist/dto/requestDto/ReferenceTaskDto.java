package com.larry.todolist.dto.requestDto;

import com.larry.todolist.domain.IdRelation;
import com.larry.todolist.domain.support.TaskType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@Getter
public class ReferenceTaskDto {

    private TaskType taskType;
    private Set<Long> referenceTasks;

    public ReferenceTaskDto(TaskType taskType, Long referenceTask) {
        this.taskType = taskType;
        this.referenceTasks = new HashSet<>(Arrays.asList(referenceTask));
    }

    public ReferenceTaskDto(TaskType taskType, Long[] referenceTasks) {
        this.taskType = taskType;
        this.referenceTasks = new HashSet<>(Arrays.asList(referenceTasks));
    }

    public void checkDuplicates(ReferenceTaskDto subTasksDto) {
        referenceTasks.stream().forEach(ref -> {
            if (subTasksDto.referenceTasks.contains(ref)) {
                throw new RuntimeException("참조 번호가 겹치는 것이 있습니다." + ref);
            }
        });
    }

    public List<IdRelation> makeIdRelationsMaster(Long newTaskId) {
        return referenceTasks.stream().map(r -> new IdRelation(r, newTaskId)).collect(Collectors.toList());
    }

    public List<IdRelation> makeIdRelationsSub(Long newTaskId) {
        return referenceTasks.stream().map(r -> new IdRelation(newTaskId, r)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ReferenceTaskDto{" +
                "taskType=" + taskType +
                ", referenceTasks=" + referenceTasks +
                '}';
    }
}
