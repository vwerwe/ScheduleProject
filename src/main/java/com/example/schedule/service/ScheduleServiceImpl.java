package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    //일정 생성
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        return scheduleRepository.saveSchedule(dto);
    }

    //일정 조회
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, String updated) {

        // 이름, 날짜 존재여부에 따라 메서드 분리
        if (name != null && updated != null) {
            return scheduleRepository.findAllSchedules(name, updated);
        } else if (updated == null && name != null) {
            return scheduleRepository.findAllSchedulesName(name);
        } else if (name == null && updated != null) {
            return scheduleRepository.findAllSchedulesUpdated(updated);
        }

        return scheduleRepository.findAllSchedulesNull();
    }

    //일정 개별 조회
    @Override
    public ScheduleResponseDto findById(Long id) {
        return scheduleRepository.findById(id);
    }

    //일정 수정
    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {

        scheduleRepository.findById(id);

        if (dto.getName() == null || dto.getTodo() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Long password = scheduleRepository.findPassword(id);

        if (dto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!password.equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호 틀림");
        }

        return scheduleRepository.updateSchedule(id, dto);

    }

    //일정 삭제
    @Override
    public void deleteSchedule(Long id, Long password) {

        scheduleRepository.findById(id);

        Long passwordKey = scheduleRepository.findPassword(id);

        if (password == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!password.equals(passwordKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호 틀림");
        }

        scheduleRepository.deleteSchedule(id);

    }
}
