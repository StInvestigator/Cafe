package org.example.dao.orderDAO;

import org.example.model.MenuItem;
import org.example.model.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setWorkerId(resultSet.getLong("worker_id"));
        order.setMenuPositionId(resultSet.getLong("menu_position_id"));
        order.setReceiptId(resultSet.getLong("receipt_id"));
        order.setPrice(resultSet.getFloat("price"));
        order.setWaiterId(resultSet.getLong("waiter_id"));
        return order;
    }
}
