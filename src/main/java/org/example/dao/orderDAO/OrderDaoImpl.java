package org.example.dao.orderDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    private static final String SAVE_ORDER = "INSERT INTO orders(worker_id, menu_position_id, receipt_id, price) VALUES(?, ?, ?, ?)";
    private static final String FIND_ALL_ORDERS = "SELECT * FROM orders";
    private static final String DELETE_ALL_ORDERS = "DELETE FROM orders";
    private static final String UPDATE_ORDER = "UPDATE orders SET worker_id = ?, menu_position_id = ?, receipt_id = ?, price = ? WHERE id = ?";
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";

    @Override
    public void save(Order order) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_ORDER)) {
            ps.setLong(1, order.getWorkerId());
            ps.setLong(2, order.getMenuPositionId());
            ps.setLong(3, order.getReceiptId());
            ps.setFloat(4, order.getPrice());
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
            ps.setLong(5, order.getId());
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
}
