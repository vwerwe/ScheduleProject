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

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        return scheduleRepository.saveSchedule(dto);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, String updated) {

        if (name != null && updated != null) {
            return scheduleRepository.findAllSchedules(name, updated);
        } else if (updated == null && name != null) {
            return scheduleRepository.findAllSchedulesName(name);
        } else if (name == null && updated != null) {
            return scheduleRepository.findAllSchedulesUpdated(updated);
        }

        return scheduleRepository.findAllSchedulesNull();
    }

    @Override
    public ScheduleResponseDto findById(Long id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {

        Long password = scheduleRepository.findPassword(id);

        log.info("password = {}", password);
        log.info("getPassword = {}", dto.getPassword());

        if (dto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!password.equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호 틀림");
        }

        return scheduleRepository.updateSchedule(id, dto);

    }

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
