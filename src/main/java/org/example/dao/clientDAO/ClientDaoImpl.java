package org.example.dao.clientDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImpl implements ClientDao {

    private static final String SAVE_CLIENT = "INSERT INTO clients(name, surname, phone, email, birthday, discount) VALUES(?,?,?,?,?,?)";
    private static final String FIND_ALL_CLIENTS = "SELECT * FROM clients";
    private static final String DELETE_ALL_CLIENTS = "DELETE FROM clients";
    private static final String UPDATE_CLIENT = "UPDATE clients SET name = ?, surname = ?, phone = ?, email = ?, birthday = ?, discount = ? " +
            " WHERE clients.id = ? ";
    private static final String DELETE_CLIENT = "DELETE FROM clients WHERE clients.id = ?";

    @Override
    public void save(Client item) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_CLIENT)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getSurname());
            ps.setString(3, item.getPhone());
            ps.setString(4, item.getEmail());
            ps.setDate(5, item.getBirthday());
            ps.setFloat(6, item.getDiscount());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void saveMany(List<Client> items) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_CLIENT)) {
            for (Client currentClient : items) {
                ps.setString(1, currentClient.getName());
                ps.setString(2, currentClient.getSurname());
                ps.setString(3, currentClient.getPhone());
                ps.setString(4, currentClient.getEmail());
                ps.setDate(5, currentClient.getBirthday());
                ps.setFloat(6, currentClient.getDiscount());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Client item) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_CLIENT)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getSurname());
            ps.setString(3, item.getPhone());
            ps.setString(4, item.getEmail());
            ps.setDate(5, item.getBirthday());
            ps.setFloat(6, item.getDiscount());
            ps.setLong(7, item.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Client item) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CLIENT)) {
            ps.setLong(1, item.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> resultClients = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_CLIENTS);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                Client client = new Client();
                client.setId(result.getLong(1));
                client.setName(result.getString(2));
                client.setSurname(result.getString(3));
                client.setPhone(result.getString(4));
                client.setEmail(result.getString(5));
                client.setBirthday(result.getDate(6));
                client.setDiscount(result.getFloat(7));
                resultClients.add(client);
            }
            return resultClients;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultClients;
    }

    @Override
    public void deleteAll() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALL_CLIENTS)) {
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
