package org.example.dao.clientDAO;

import org.example.exception.ConnectionDBException;
import org.example.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Client> clientRowMapper;

    @Autowired
    private RowMapper<Float> floatRowMapper;

    @Override
    public void save(Client item) {
        try {
            jdbcTemplate.update(SAVE_CLIENT, item.getName(), item.getSurname(), item.getPhone(), item.getEmail(), item.getBirthday(), item.getDiscount());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveMany(List<Client> items) {
        jdbcTemplate.batchUpdate(SAVE_CLIENT,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, items.get(i).getName());
                        ps.setString(2, items.get(i).getSurname());
                        ps.setString(3, items.get(i).getPhone());
                        ps.setString(4, items.get(i).getEmail());
                        ps.setDate(5, items.get(i).getBirthday());
                        ps.setFloat(6, items.get(i).getDiscount());
                    }

                    @Override
                    public int getBatchSize() {
                        return items.size();
                    }
                });
    }

    @Override
    public void update(Client item) {
        try {
            jdbcTemplate.update(UPDATE_CLIENT, item.getName(), item.getSurname(), item.getPhone(),
                    item.getEmail(), item.getBirthday(), item.getDiscount(), item.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Client item) {
        try {
            jdbcTemplate.update(DELETE_CLIENT, item.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return findAllByQuery(FIND_ALL_CLIENTS);
    }

    @Override
    public void deleteAll() {
        try {
            jdbcTemplate.update(DELETE_ALL_CLIENTS);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findWithMinDiscount() {
        return findAllByQuery(FIND_WITH_MIN_DISCOUNT);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findWithMaxDiscount() {
        return findAllByQuery(FIND_WITH_MAX_DISCOUNT);
    }

    @Override
    public Float findAvgDiscount() {
        try {
            return jdbcTemplate.query(FIND_AVG_DISCOUNT, floatRowMapper).get(0);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return 0f;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findYoungestClients() {
        return findAllByQuery(FIND_WITH_MAX_BIRTHDAY);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findOldestClients() {
        return findAllByQuery(FIND_WITH_MIN_BIRTHDAY);
    }

    @Override
    public List<Client> findClientsThatOrderedMenuTypeOnDate(String type, Date date) {
        try {
            return jdbcTemplate.query(FIND_THAT_ORDERED_MENU_TYPE_AND_DATE, clientRowMapper, type, date);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Client> findClientsThatOrderedMaxPriceOnDate(Date date) {
        try {
            return jdbcTemplate.query(FIND_CLIENTS_THAT_ORDER_ON_MAX_PRICE_ON_DATE, clientRowMapper, date, date);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    private List<Client> findAllByQuery(String query) {
        try {
            return jdbcTemplate.query(query, clientRowMapper);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
