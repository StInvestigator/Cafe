package org.example.dao.workerDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.Worker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void save(Worker worker) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_WORKER)) {
            ps.setString(1, worker.getName());
            ps.setString(2, worker.getSurname());
            ps.setString(3, worker.getPhone());
            ps.setString(4, worker.getEmail());
            ps.setString(5, worker.getPosition());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void saveMany(List<Worker> workers) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_WORKER)) {

            for (Worker worker : workers) {
                ps.setString(1, worker.getName());
                ps.setString(2, worker.getSurname());
                ps.setString(3, worker.getPhone());
                ps.setString(4, worker.getEmail());
                ps.setString(5, worker.getPosition());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Worker worker) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_WORKER)) {
            ps.setString(1, worker.getName());
            ps.setString(2, worker.getSurname());
            ps.setString(3, worker.getPhone());
            ps.setString(4, worker.getEmail());
            ps.setString(5, worker.getPosition());
            ps.setLong(6, worker.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Worker worker) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_WORKER)) {
            ps.setLong(1, worker.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Worker> findAll() {
        List<Worker> resultWorkers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_WORKERS);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                Worker worker = new Worker();
                worker.setId(result.getLong(1));
                worker.setName(result.getString(2));
                worker.setSurname(result.getString(3));
                worker.setPhone(result.getString(4));
                worker.setEmail(result.getString(5));
                worker.setPosition(result.getString(6)); // Get position name
                resultWorkers.add(worker);
            }
            return resultWorkers;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultWorkers;
    }

    @Override
    public void deleteAll() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALL_WORKERS)) {
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Worker> findAllWorkersWithPosition(String position) {
        List<Worker> resultWorkers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_WORKERS_WITH_POSITION)) {
            ps.setString(1, position);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Worker worker = new Worker();
                worker.setId(result.getLong(1));
                worker.setName(result.getString(2));
                worker.setSurname(result.getString(3));
                worker.setPhone(result.getString(4));
                worker.setEmail(result.getString(5));
                worker.setPosition(result.getString(6)); // Get position name
                resultWorkers.add(worker);
            }
            return resultWorkers;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultWorkers;
    }

    @Override
    public List<Worker> findWorkersThatDidMenuTypeOnDate(String type, Date date) {
        List<Worker> resultWorkers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_WORKERS_THAT_COOKED_MENU_TYPE_IN_DATE)) {
            ps.setString(1, type);
            ps.setDate(2, date);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Worker worker = new Worker();
                worker.setId(result.getLong(1));
                worker.setName(result.getString(2));
                worker.setSurname(result.getString(3));
                worker.setPhone(result.getString(4));
                worker.setEmail(result.getString(5));
                worker.setPosition(result.getString(6)); // Get position name
                resultWorkers.add(worker);
            }
            return resultWorkers;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultWorkers;
    }
}
