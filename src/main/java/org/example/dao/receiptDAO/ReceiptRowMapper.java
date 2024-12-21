package org.example.dao.receiptDAO;

import org.example.model.Order;
import org.example.model.Receipt;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ReceiptRowMapper implements RowMapper<Receipt> {
    @Override
    public Receipt mapRow(ResultSet resultSet, int i) throws SQLException {
        Receipt receipt = new Receipt();
        receipt.setId(resultSet.getLong("id"));
        receipt.setClientId(resultSet.getLong("client_id"));
        receipt.setTotalPrice(resultSet.getFloat("total_price"));
        receipt.setDate(resultSet.getDate("date"));
        return receipt;
    }
}
