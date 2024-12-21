package org.example.dao.clientDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImpl implements ClientDao {

    private static final String SAVE_CLIENT = "INSERT INTO clients(name, surname, phone, email, birthday, discount) VALUES(?,?,?,?,?,?)";
    private static final String FIND_ALL_CLIENTS = "SELECT * FROM clients ORDER BY id";
    private static final String FIND_WITH_MAX_DISCOUNT = "SELECT * " +
            "FROM public.clients " +
            "WHERE discount = (SELECT max(discount) FROM clients) ORDER BY id";
    private static final String FIND_WITH_MIN_DISCOUNT = "SELECT * " +
            "FROM public.clients " +
            "WHERE discount = (SELECT min(discount) FROM clients) ORDER BY id";
    private static final String FIND_WITH_MIN_BIRTHDAY = "SELECT * " +
            "FROM public.clients " +
            "WHERE birthday = (SELECT min(birthday) FROM clients) ORDER BY id";
    private static final String FIND_WITH_MAX_BIRTHDAY = "SELECT * " +
            "FROM public.clients " +
            "WHERE birthday = (SELECT max(birthday) FROM clients) ORDER BY id";
    private static final String FIND_THAT_ORDERED_MENU_TYPE_AND_DATE = "SELECT c.* " +
            "FROM clients c JOIN receipts r on c.id = r.client_id JOIN orders o ON r.id = o.receipt_id " +
            "JOIN menu m on o.menu_position_id = m.id JOIN menu_types mt ON m.type_id = mt.id " +
            "WHERE mt.name = ? AND r.date = ? " +
            "GROUP BY c.id " +
            "ORDER BY c.id";
    private static final String FIND_CLIENTS_THAT_ORDER_ON_MAX_PRICE_ON_DATE = """
            SELECT c.*
            FROM clients c JOIN receipts r ON r.client_id = c.id JOIN orders o ON r.id = o.receipt_id
            WHERE o.price = (SELECT max(price)
            FROM orders o JOIN receipts r ON o.receipt_id = r.id
            WHERE r.date = ?) AND r.date = ? GROUP BY c.id ORDER BY c.id""";
    private static final String FIND_AVG_DISCOUNT = "SELECT avg(discount) FROM clients";
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
        return findAllByQuery(FIND_ALL_CLIENTS);
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

    @Override
    public List<Client> findWithMinDiscount() {
        return findAllByQuery(FIND_WITH_MIN_DISCOUNT);
    }

    @Override
    public List<Client> findWithMaxDiscount() {
        return findAllByQuery(FIND_WITH_MAX_DISCOUNT);
    }

    @Override
    public Float findAvgDiscount() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_AVG_DISCOUNT);
             ResultSet result = ps.executeQuery()) {
            result.next();
            return result.getFloat(1);
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0f;
    }

    @Override
    public List<Client> findYoungestClients() {
        return findAllByQuery(FIND_WITH_MAX_BIRTHDAY);
    }

    @Override
    public List<Client> findOldestClients() {
        return findAllByQuery(FIND_WITH_MIN_BIRTHDAY);
    }

    @Override
    public List<Client> findClientsThatOrderedMenuTypeOnDate(String type, Date date) {
        List<Client> resultClients = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_THAT_ORDERED_MENU_TYPE_AND_DATE)) {
            ps.setString(1, type);
            ps.setDate(2, date);
            ResultSet result = ps.executeQuery();
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
    public List<Client> findClientsThatOrderedMaxPriceOnDate(Date date) {
        List<Client> resultClients = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_CLIENTS_THAT_ORDER_ON_MAX_PRICE_ON_DATE)) {
            ps.setDate(1, date);
            ps.setDate(2, date);
            ResultSet result = ps.executeQuery();
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

    private List<Client> findAllByQuery(String query) {
        List<Client> resultClients = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(query);
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
}
