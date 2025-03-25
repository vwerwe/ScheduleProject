package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findAllSchedules(String name, String updated);

    List<ScheduleResponseDto> findAllSchedulesName(String name);

    List<ScheduleResponseDto> findAllSchedulesUpdated(String updated);

    List<ScheduleResponseDto> findAllSchedulesNull();

    ScheduleResponseDto findById(Long id);

    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto);

    Long findPassword(Long id);

    void deleteSchedule(Long id);
}
