package com.larry.todolist.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UpdateDto {

    private Long pk;

    private String name;

    private String value;

    @Override
    public String toString() {
        return "UpdateDto{" +
                "pk=" + pk +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
