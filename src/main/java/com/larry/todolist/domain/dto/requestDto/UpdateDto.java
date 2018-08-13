package com.larry.todolist.domain.dto.requestDto;

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

    public UpdateDto(Long pk, String name, String value) {
        this.pk = pk;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "UpdateDto{" +
                "pk=" + pk +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
