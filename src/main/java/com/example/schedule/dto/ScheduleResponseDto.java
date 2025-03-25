package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long id;
    private String name;
    private String todo;
    private String created;
    private String updated;

    public ScheduleResponseDto(Long id, String name, String todo, String created) {
        this.id = id;
        this.name = name;
        this.todo = todo;
        this.created = created;
    }

}
