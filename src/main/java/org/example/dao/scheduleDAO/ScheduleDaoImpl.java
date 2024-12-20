package org.example.dao.scheduleDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDaoImpl implements ScheduleDao {

    private static final String SAVE_SCHEDULE = "INSERT INTO schedule(worker_id, date, start_time, end_time) VALUES(?, ?, ?, ?)";
    private static final String FIND_ALL_SCHEDULES = "SELECT * FROM schedule";
    private static final String DELETE_ALL_SCHEDULES = "DELETE FROM schedule";
    private static final String UPDATE_SCHEDULE = "UPDATE schedule SET worker_id = ?, date = ?, start_time = ?, end_time = ? WHERE id = ?";
    private static final String DELETE_SCHEDULE = "DELETE FROM schedule WHERE id = ?";

    @Override
    public void save(Schedule schedule) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_SCHEDULE)) {
            ps.setLong(1, schedule.getWorkerId());
            ps.setDate(2, schedule.getDate());
            ps.setTime(3, schedule.getStartTime());
            ps.setTime(4, schedule.getEndTime());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void saveMany(List<Schedule> schedules) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_SCHEDULE)) {

            for (Schedule schedule : schedules) {
                ps.setLong(1, schedule.getWorkerId());
                ps.setDate(2, schedule.getDate());
                ps.setTime(3, schedule.getStartTime());
                ps.setTime(4, schedule.getEndTime());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Schedule schedule) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SCHEDULE)) {
            ps.setLong(1, schedule.getWorkerId());
            ps.setDate(2, schedule.getDate());
            ps.setTime(3, schedule.getStartTime());
            ps.setTime(4, schedule.getEndTime());
            ps.setLong(5, schedule.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Schedule schedule) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SCHEDULE)) {
            ps.setLong(1, schedule.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Schedule> findAll() {
        List<Schedule> resultSchedules = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SCHEDULES);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(result.getLong(1));
                schedule.setWorkerId(result.getLong(2));
                schedule.setDate(result.getDate(3));
                schedule.setStartTime(result.getTime(4));
                schedule.setEndTime(result.getTime(5));
                resultSchedules.add(schedule);
            }
            return resultSchedules;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultSchedules;
    }

    @Override
    public void deleteAll() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALL_SCHEDULES)) {
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
