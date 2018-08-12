package com.larry.todolist.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateRequestDto {

    @JsonProperty("pk")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

}
