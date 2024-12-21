package org.example.dao.orderDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.Client;
import org.example.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void save(Order order) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_ORDER)) {
            ps.setLong(1, order.getWorkerId());
            ps.setLong(2, order.getMenuPositionId());
            ps.setLong(3, order.getReceiptId());
            ps.setFloat(4, order.getPrice());
            ps.setLong(5, order.getWaiterId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void saveMany(List<Order> orders) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_ORDER)) {
            for (Order order : orders) {
                ps.setLong(1, order.getWorkerId());
                ps.setLong(2, order.getMenuPositionId());
                ps.setLong(3, order.getReceiptId());
                ps.setFloat(4, order.getPrice());
                ps.setLong(5, order.getWaiterId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Order order) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ORDER)) {
            ps.setLong(1, order.getWorkerId());
            ps.setLong(2, order.getMenuPositionId());
            ps.setLong(3, order.getReceiptId());
            ps.setFloat(4, order.getPrice());
            ps.setLong(5, order.getWaiterId());
            ps.setLong(6, order.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Order order) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ORDER)) {
            ps.setLong(1, order.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> resultOrders = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_ORDERS);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                Order order = new Order();
                order.setId(result.getLong(1));
                order.setWorkerId(result.getLong(2));
                order.setMenuPositionId(result.getLong(3));
                order.setReceiptId(result.getLong(4));
                order.setPrice(result.getFloat(5));
                order.setWaiterId(result.getLong(6));
                resultOrders.add(order);
            }
            return resultOrders;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultOrders;
    }

    @Override
    public void deleteAll() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALL_ORDERS)) {
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Order> findOrdersByClient(Client client) {
        List<Order> resultOrders = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ORDERS_BY_CLIENT_ID)){
            ps.setLong(1, client.getId());
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Order order = new Order();
                order.setId(result.getLong(1));
                order.setWorkerId(result.getLong(2));
                order.setMenuPositionId(result.getLong(3));
                order.setReceiptId(result.getLong(4));
                order.setPrice(result.getFloat(5));
                order.setWaiterId(result.getLong(6));
                resultOrders.add(order);
            }
            return resultOrders;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultOrders;
    }

    @Override
    public List<Order> findOrdersByDate(Date date) {
        List<Order> resultOrders = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ORDERS_BY_DATE)){
            ps.setDate(1, date);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Order order = new Order();
                order.setId(result.getLong(1));
                order.setWorkerId(result.getLong(2));
                order.setMenuPositionId(result.getLong(3));
                order.setReceiptId(result.getLong(4));
                order.setPrice(result.getFloat(5));
                order.setWaiterId(result.getLong(6));
                resultOrders.add(order);
            }
            return resultOrders;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultOrders;
    }

    @Override
    public List<Order> findOrdersBetweenDates(Date start, Date end) {
        List<Order> resultOrders = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ORDERS_BETWEEN_DATES)){
            ps.setDate(1, start);
            ps.setDate(2, end);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Order order = new Order();
                order.setId(result.getLong(1));
                order.setWorkerId(result.getLong(2));
                order.setMenuPositionId(result.getLong(3));
                order.setReceiptId(result.getLong(4));
                order.setPrice(result.getFloat(5));
                order.setWaiterId(result.getLong(6));
                resultOrders.add(order);
            }
            return resultOrders;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultOrders;
    }

    @Override
    public List<Order> findOrdersOnDateByMenuItemType(Date date, String type) {
        List<Order> resultOrders = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ORDERS_BY_DATE_AND_MENU_ITEM_TYPE)){
            ps.setDate(1, date);
            ps.setString(2, type);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Order order = new Order();
                order.setId(result.getLong(1));
                order.setWorkerId(result.getLong(2));
                order.setMenuPositionId(result.getLong(3));
                order.setReceiptId(result.getLong(4));
                order.setPrice(result.getFloat(5));
                order.setWaiterId(result.getLong(6));
                resultOrders.add(order);
            }
            return resultOrders;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultOrders;
    }

    @Override
    public Float findAvgPriceOnDate(Date date) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_AVG_PRICE_ON_DATE)){
            ps.setDate(1, date);
            ResultSet result = ps.executeQuery();
            result.next();
            return result.getFloat(1);
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0f;
    }

    @Override
    public Float findMaxPriceOnDate(Date date) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_MAX_PRICE_ON_DATE)){
            ps.setDate(1, date);
            ResultSet result = ps.executeQuery();
            result.next();
            return result.getFloat(1);
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0f;
    }
}
