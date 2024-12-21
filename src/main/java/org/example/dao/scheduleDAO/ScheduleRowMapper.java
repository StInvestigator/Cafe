package org.example.dao.scheduleDAO;

import org.example.model.Receipt;
import org.example.model.Schedule;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ScheduleRowMapper implements RowMapper<Schedule> {
    @Override
    public Schedule mapRow(ResultSet resultSet, int i) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setId(resultSet.getLong("id"));
        schedule.setWorkerId(resultSet.getLong("worker_id"));
        schedule.setDate(resultSet.getDate("date"));
        schedule.setStartTime(resultSet.getTime("startTime"));
        schedule.setEndTime(resultSet.getTime("endTime"));
        return schedule;
    }
}
