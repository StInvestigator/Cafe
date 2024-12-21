package org.example.dao.workerDAO;

import org.example.model.Schedule;
import org.example.model.Worker;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class WorkerRowMapper implements RowMapper<Worker> {
    @Override
    public Worker mapRow(ResultSet resultSet, int i) throws SQLException {
        Worker worker = new Worker();
        worker.setId(resultSet.getLong("id"));
        worker.setName(resultSet.getString("name"));
        worker.setSurname(resultSet.getString("surname"));
        worker.setPhone(resultSet.getString("phone"));
        worker.setEmail(resultSet.getString("email"));
        worker.setPosition(resultSet.getString("position"));
        return worker;
    }
}
