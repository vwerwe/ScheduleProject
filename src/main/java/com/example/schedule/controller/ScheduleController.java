package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    //일정 생성
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    //일정 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String updated
    ) {
        return new ResponseEntity<>(scheduleService.findAllSchedules(name, updated), HttpStatus.OK);
    }

    //일정 개별 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.OK);
    }

    //일정 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto), HttpStatus.OK);
    }

    //일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> daleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto) {
        scheduleService.deleteSchedule(id, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
