package org.example.dao.scheduleDAO;

import org.example.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    private static final String SAVE_SCHEDULE = "INSERT INTO schedule(worker_id, date, start_time, end_time) VALUES(?, ?, ?, ?)";
    private static final String FIND_ALL_SCHEDULES = "SELECT * FROM schedule ORDER BY id";
    private static final String DELETE_ALL_SCHEDULES = "DELETE FROM schedule";
    private static final String UPDATE_SCHEDULE = "UPDATE schedule SET worker_id = ?, date = ?, start_time = ?, end_time = ? WHERE id = ?";
    private static final String DELETE_SCHEDULE = "DELETE FROM schedule WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Schedule> scheduleRowMapper;

    @Override
    public void save(Schedule schedule) {
        try {
            jdbcTemplate.update(SAVE_SCHEDULE, schedule.getWorkerId(), schedule.getDate(),
                    schedule.getStartTime(), schedule.getEndTime());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveMany(List<Schedule> schedules) {
        jdbcTemplate.batchUpdate(SAVE_SCHEDULE,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, schedules.get(i).getWorkerId());
                        ps.setDate(2, schedules.get(i).getDate());
                        ps.setTime(3, schedules.get(i).getStartTime());
                        ps.setTime(4, schedules.get(i).getEndTime());
                    }

                    @Override
                    public int getBatchSize() {
                        return schedules.size();
                    }
                });
    }

    @Override
    public void update(Schedule schedule) {
        try {
            jdbcTemplate.update(UPDATE_SCHEDULE, schedule.getWorkerId(), schedule.getDate(),
                    schedule.getStartTime(), schedule.getEndTime(), schedule.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Schedule schedule) {
        try {
            jdbcTemplate.update(DELETE_SCHEDULE, schedule.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Schedule> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_SCHEDULES, scheduleRowMapper);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteAll() {
        try {
            jdbcTemplate.update(DELETE_ALL_SCHEDULES);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
