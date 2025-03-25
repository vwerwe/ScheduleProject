package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Schedule {

    private String name;
    private String todo;
    private Long password;
    private Long updated_date;

    public Schedule(String name, String todo, Long password) {
        this.name = name;
        this.todo = todo;
        this.password = password;
    }


}
