package org.example.dao.workerDAO;

import org.example.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WorkerDaoImpl implements WorkerDao {

    private static final String SAVE_WORKER = "INSERT INTO workers(name, surname, phone, email, position_id) " +
            "VALUES(?, ?, ?, ?, (SELECT id FROM work_positions WHERE name = ?))";
    private static final String FIND_ALL_WORKERS = "SELECT w.id, w.name, w.surname, w.phone, w.email, p.name as position " +
            "FROM workers w JOIN work_positions p ON w.position_id = p.id ORDER BY w.id";
    private static final String FIND_ALL_WORKERS_WITH_POSITION = "SELECT w.id, w.name, w.surname, w.phone, w.email, p.name as position " +
            "FROM workers w JOIN work_positions p ON w.position_id = p.id " +
            "WHERE p.name = ? ORDER BY w.id";
    private static final String FIND_WORKERS_THAT_COOKED_MENU_TYPE_IN_DATE = "SELECT w.id, w.name, w.surname, w.phone, w.email, wp.name as position " +
            "FROM workers w " +
            "JOIN orders o ON W.id = o.worker_id " +
            "JOIN receipts r on r.id = o.receipt_id " +
            "JOIN menu m on o.menu_position_id = m.id " +
            "JOIN menu_types mt ON m.type_id = mt.id " +
            "JOIN work_positions wp ON w.position_id = wp.id " +
            "WHERE mt.name = ? " +
            "and r.date = ? " +
            "GROUP BY w.id, wp.id " +
            "ORDER BY w.id";
    private static final String DELETE_ALL_WORKERS = "DELETE FROM workers";
    private static final String UPDATE_WORKER = "UPDATE workers SET name = ?, surname = ?, phone = ?, email = ?, position_id = " +
            "(SELECT id FROM work_positions WHERE name = ?) WHERE id = ?";
    private static final String DELETE_WORKER = "DELETE FROM workers WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Worker> workerRowMapper;

    @Override
    public void save(Worker worker) {
        try {
            jdbcTemplate.update(SAVE_WORKER, worker.getName(), worker.getSurname(), worker.getPhone(),
                    worker.getEmail(), worker.getPosition());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveMany(List<Worker> workers) {
        jdbcTemplate.batchUpdate(SAVE_WORKER,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, workers.get(i).getName());
                        ps.setString(2, workers.get(i).getSurname());
                        ps.setString(3, workers.get(i).getPhone());
                        ps.setString(4, workers.get(i).getEmail());
                        ps.setString(5, workers.get(i).getPosition());
                    }

                    @Override
                    public int getBatchSize() {
                        return workers.size();
                    }
                });
    }

    @Override
    public void update(Worker worker) {
        try {
            jdbcTemplate.update(UPDATE_WORKER, worker.getName(), worker.getSurname(), worker.getPhone(),
                    worker.getEmail(), worker.getPosition(), worker.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Worker worker) {
        try {
            jdbcTemplate.update(DELETE_WORKER, worker.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Worker> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_WORKERS, workerRowMapper);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteAll() {
        try {
            jdbcTemplate.update(DELETE_ALL_WORKERS);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Worker> findAllWorkersWithPosition(String position) {
        try {
            return jdbcTemplate.query(FIND_ALL_WORKERS_WITH_POSITION, workerRowMapper, position);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Worker> findWorkersThatDidMenuTypeOnDate(String type, Date date) {
        try {
            return jdbcTemplate.query(FIND_WORKERS_THAT_COOKED_MENU_TYPE_IN_DATE,
                    workerRowMapper, type, date);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
