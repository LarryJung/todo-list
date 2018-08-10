package com.larry.todolist.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class TaskResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("createdDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createdDate;

    @JsonProperty("modifiedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime modifiedDate;

    @JsonProperty("completedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime completedDate;

    @JsonProperty("todo")
    private String todo;

    @JsonProperty(value = "sub_tasks")
    private List<RefTask> subTasks;

    @JsonProperty(value = "master_tasks")
    private List<RefTask> masterTasks;

    private String makeRefString(List<RefTask> refTasks) {
        if(refTasks == null) {
            return null;
        }
        return refTasks.stream().map(r -> r.todo).collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskResponseDto that = (TaskResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(modifiedDate, that.modifiedDate) &&
                Objects.equals(completedDate, that.completedDate) &&
                Objects.equals(todo, that.todo) &&
                Objects.equals(subTasks, that.subTasks) &&
                Objects.equals(masterTasks, that.masterTasks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, createdDate, modifiedDate, completedDate, todo, subTasks, masterTasks);
    }

    @Override
    public String toString() {
        return "TaskResponseDto{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", todo='" + todo + '\'' +
                ", completedDate=" + completedDate +
                ", subTasks=" + makeRefString(subTasks) +
                ", masterTasks=" + makeRefString(masterTasks) +
                '}';
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class RefTask {
        private Long id;
        private String todo;

        public RefTask(Long id, String todo) {
            this.id = id;
            this.todo = todo;
        }

        @Override
        public String toString() {
            return todo;
        }
    }
}
