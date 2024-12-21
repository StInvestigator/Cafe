package org.example.dao.orderDAO;

import org.example.model.Client;
import org.example.model.Order;
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
public class OrderDaoImpl implements OrderDao {

    private static final String SAVE_ORDER = "INSERT INTO orders(worker_id, menu_position_id, receipt_id, price, waiter_id) VALUES(?, ?, ?, ?, ?)";
    private static final String FIND_ALL_ORDERS = "SELECT * FROM orders ORDER BY id";
    private static final String FIND_ORDERS_BY_CLIENT_ID = "SELECT o.* " +
            "FROM orders o JOIN receipts r ON o.receipt_id = r.id " +
            "WHERE r.client_id = ? ORDER BY o.id";
    private static final String FIND_ORDERS_BY_DATE = "SELECT o.* " +
            "FROM orders o JOIN receipts r ON o.receipt_id = r.id " +
            "WHERE r.date = ? ORDER BY o.id";
    private static final String FIND_ORDERS_BETWEEN_DATES = "SELECT o.* " +
            "FROM orders o JOIN receipts r ON o.receipt_id = r.id " +
            "WHERE r.date BETWEEN ? AND ? ORDER BY o.id";
    private static final String FIND_ORDERS_BY_DATE_AND_MENU_ITEM_TYPE = "SELECT o.* " +
            "FROM orders o JOIN receipts r ON o.receipt_id = r.id JOIN menu m ON o.menu_position_id = m.id " +
            "WHERE r.date = ? AND  m.type_id = (SELECT id FROM menu_types WHERE name = ?) " +
            "ORDER BY o.id";
    private static final String FIND_AVG_PRICE_ON_DATE = """
            SELECT avg(price)
            FROM orders o JOIN receipts r ON o.receipt_id = r.id
            WHERE r.date = ?""";
    private static final String FIND_MAX_PRICE_ON_DATE = """
            SELECT max(price)
            FROM orders o JOIN receipts r ON o.receipt_id = r.id
            WHERE r.date = ?""";
    private static final String DELETE_ALL_ORDERS = "DELETE FROM orders";
    private static final String UPDATE_ORDER = "UPDATE orders SET worker_id = ?, menu_position_id = ?, receipt_id = ?, price = ?, waiter_id = ? WHERE id = ?";
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Order> orderRowMapper;

    @Autowired
    private RowMapper<Float> floatRowMapper;

    @Override
    public void save(Order order) {
        try {
            jdbcTemplate.update(SAVE_ORDER, order.getWorkerId(), order.getMenuPositionId(),
                    order.getReceiptId(), order.getPrice(), order.getWaiterId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveMany(List<Order> items) {
        jdbcTemplate.batchUpdate(SAVE_ORDER,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, items.get(i).getWorkerId());
                        ps.setLong(2, items.get(i).getMenuPositionId());
                        ps.setLong(3, items.get(i).getReceiptId());
                        ps.setFloat(4, items.get(i).getPrice());
                        ps.setLong(5, items.get(i).getWaiterId());
                    }

                    @Override
                    public int getBatchSize() {
                        return items.size();
                    }
                });
    }

    @Override
    public void update(Order order) {
        try {
            jdbcTemplate.update(UPDATE_ORDER, order.getWorkerId(), order.getMenuPositionId(),
                    order.getReceiptId(), order.getPrice(), order.getWaiterId(),order.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Order order) {
        try {
            jdbcTemplate.update(DELETE_ORDER,order.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Order> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_ORDERS, orderRowMapper);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteAll() {
        try {
            jdbcTemplate.update(DELETE_ALL_ORDERS);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Order> findOrdersByClient(Client client) {
        try {
            return jdbcTemplate.query(FIND_ORDERS_BY_CLIENT_ID, orderRowMapper, client.getId());
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Order> findOrdersByDate(Date date) {
        try {
            return jdbcTemplate.query(FIND_ORDERS_BY_DATE, orderRowMapper, date);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Order> findOrdersBetweenDates(Date start, Date end) {
        try {
            return jdbcTemplate.query(FIND_ORDERS_BETWEEN_DATES, orderRowMapper, start, end);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Order> findOrdersOnDateByMenuItemType(Date date, String type) {
        try {
            return jdbcTemplate.query(FIND_ORDERS_BY_DATE_AND_MENU_ITEM_TYPE, orderRowMapper, date, type);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Float findAvgPriceOnDate(Date date) {
        try {
            return jdbcTemplate.query(FIND_AVG_PRICE_ON_DATE, floatRowMapper, date).get(0);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return 0f;
    }

    @Override
    public Float findMaxPriceOnDate(Date date) {
        try {
            return jdbcTemplate.query(FIND_MAX_PRICE_ON_DATE, floatRowMapper, date).get(0);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return 0f;
    }
}
