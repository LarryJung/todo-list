package com.larry.todolist.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IdTodoPair {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("todo")
    private String todo;

    public IdTodoPair(Long id, String todo) {
        this.id = id;
        this.todo = todo;
    }
}
