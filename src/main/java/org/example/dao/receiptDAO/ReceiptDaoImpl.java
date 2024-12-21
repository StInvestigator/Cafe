package org.example.dao.receiptDAO;

import org.example.model.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReceiptDaoImpl implements ReceiptDao {

    private static final String SAVE_RECEIPT = "INSERT INTO receipts(client_id, total_price, date) VALUES(?, ?,?)";
    private static final String FIND_ALL_RECEIPTS = "SELECT * FROM receipts ORDER BY id";
    private static final String DELETE_ALL_RECEIPTS = "DELETE FROM receipts";
    private static final String UPDATE_RECEIPT = "UPDATE receipts SET client_id = ?, total_price = ?, date = ? WHERE id = ?";
    private static final String DELETE_RECEIPT = "DELETE FROM receipts WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<Receipt> receiptRowMapper;

    @Override
    public void save(Receipt receipt) {
        try {
            jdbcTemplate.update(SAVE_RECEIPT, receipt.getClientId(), receipt.getTotalPrice(), receipt.getDate());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveMany(List<Receipt> receipts) {
        jdbcTemplate.batchUpdate(SAVE_RECEIPT,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, receipts.get(i).getClientId());
                        ps.setFloat(2, receipts.get(i).getTotalPrice());
                        ps.setDate(3, receipts.get(i).getDate());
                    }

                    @Override
                    public int getBatchSize() {
                        return receipts.size();
                    }
                });
    }

    @Override
    public void update(Receipt receipt) {
        try {
            jdbcTemplate.update(UPDATE_RECEIPT, receipt.getClientId(), receipt.getTotalPrice(),
                    receipt.getDate(), receipt.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Receipt receipt) {
        try {
            jdbcTemplate.update(DELETE_RECEIPT, receipt.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Receipt> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_RECEIPTS, receiptRowMapper);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteAll() {
        try {
            jdbcTemplate.update(DELETE_ALL_RECEIPTS);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
