package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findAllSchedules(String name, String updated);

    ScheduleResponseDto findById(Long id);

    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);

    void deleteSchedule(Long id, Long password);
}
