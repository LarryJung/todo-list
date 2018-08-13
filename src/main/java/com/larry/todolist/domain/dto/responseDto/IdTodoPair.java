package com.larry.todolist.domain.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IdTodoPair {

    @JsonProperty("pk")
    private Long id;
    @JsonProperty("todo")
    private String todo;

    public IdTodoPair(Long id, String todo) {
        this.id = id;
        this.todo = todo;
    }
}
