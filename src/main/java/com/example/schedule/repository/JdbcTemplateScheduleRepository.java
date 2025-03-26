package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository

public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //일정 생성
    @Transactional
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        //현재 날짜 생성
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", dto.getName());
        parameters.put("todo", dto.getTodo());
        parameters.put("password", dto.getPassword());
        parameters.put("created", nowStr);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new ScheduleResponseDto(key.longValue(), dto.getName(), dto.getTodo(), nowStr);

    }

    //일정 조회(이름, 날짜 둘다 입력했을때)
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, String updated) {
            String inputUpdated = updated + "%";
            return jdbcTemplate.query("select * from schedule where name = ? and created like ? order by created desc", scheduleRowMapper(), name, inputUpdated);
    }

    //일정 조회(이름만 입력했을때)
    @Override
    public List<ScheduleResponseDto> findAllSchedulesName(String name) {
        return jdbcTemplate.query("select * from schedule where name = ? order by created desc", scheduleRowMapper(), name);
    }

    //일정 조회(날짜만 입력했을떄)
    @Override
    public List<ScheduleResponseDto> findAllSchedulesUpdated(String updated) {
        String inputUpdated = updated + "%";
        return jdbcTemplate.query("select * from schedule where created like ? order by created desc", scheduleRowMapper(), inputUpdated);
    }

    //일정 조회(둘다 입력안했을때)
    @Override
    public List<ScheduleResponseDto> findAllSchedulesNull() {
        return jdbcTemplate.query("select * from schedule order by created desc", scheduleRowMapper());
    }

    //id값에 맞는 일정있는지 확인, 반환
    @Override
    public ScheduleResponseDto findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("select * from schedule where id = ?", scheduleRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    //일정 수정
    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        //수정 날짜 생성
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        jdbcTemplate.update("update schedule set name = ?, todo = ?, updated = ? where id = ?", dto.getName(), dto.getTodo(), nowStr, id);


        return findById(id);

    }


    //비밀번호 찾기
    @Override
    public Long findPassword(Long id) {
        return jdbcTemplate.queryForObject("select password from schedule where id = ?", scheduleRowMapperPassword(), id);
    }

    //일정 삭제
    @Override
    public void deleteSchedule(Long id) {
        jdbcTemplate.update("delete from schedule where id = ?", id);
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {

        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("todo"),
                    rs.getString("created"),
                    rs.getString("updated")
                );
            }
        };
    }

    private RowMapper<Long> scheduleRowMapperPassword() {

        return new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("password");
            }
        };
    }


}
