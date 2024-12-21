package org.example.dao.clientDAO;

import org.example.model.Client;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ClientRowMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet resultSet, int i) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getLong("id"));
        client.setName(resultSet.getString("name"));
        client.setSurname(resultSet.getString("surname"));
        client.setPhone(resultSet.getString("phone"));
        client.setEmail(resultSet.getString("email"));
        client.setBirthday(resultSet.getDate("birthday"));
        client.setDiscount(resultSet.getFloat("discount"));
        return client;
    }
}
