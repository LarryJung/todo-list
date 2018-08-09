package com.larry.todolist.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

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

    @JsonProperty(value = "sub_references")
    private List<RefTask> subReferences;

    @JsonProperty(value = "master_references")
    private List<RefTask> masterReferences;

    @Override
    public String toString() {
        return "TaskResponseDto{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", todo='" + todo + '\'' +
                ", completedDate=" + completedDate +
                ", subReferences=" + subReferences +
                ", masterReferences=" + masterReferences +
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
    }
}
