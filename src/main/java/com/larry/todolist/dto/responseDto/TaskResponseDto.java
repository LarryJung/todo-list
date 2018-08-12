package com.larry.todolist.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Setter
@Getter
@NoArgsConstructor
public class TaskResponseDto {

    @JsonProperty("pk")
    private Long id;

    @JsonProperty("createdDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createdDate;

    @JsonProperty("modifiedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime modifiedDate;

    @JsonProperty("completedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime completedDate;

    @JsonProperty("todo")
    private String todo;

}
