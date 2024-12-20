package org.example.dao.receiptDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.Receipt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDaoImpl implements ReceiptDao {

    private static final String SAVE_RECEIPT = "INSERT INTO receipts(client_id, total_price, date) VALUES(?, ?,?)";
    private static final String FIND_ALL_RECEIPTS = "SELECT * FROM receipts ORDER BY id";
    private static final String DELETE_ALL_RECEIPTS = "DELETE FROM receipts";
    private static final String UPDATE_RECEIPT = "UPDATE receipts SET client_id = ?, total_price = ?, date = ? WHERE id = ?";
    private static final String DELETE_RECEIPT = "DELETE FROM receipts WHERE id = ?";

    @Override
    public void save(Receipt receipt) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_RECEIPT)) {
            ps.setLong(1, receipt.getClientId());
            ps.setFloat(2, receipt.getTotalPrice());
            ps.setDate(3, receipt.getDate());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void saveMany(List<Receipt> receipts) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_RECEIPT)) {

            for (Receipt receipt : receipts) {
                ps.setLong(1, receipt.getClientId());
                ps.setFloat(2, receipt.getTotalPrice());
                ps.setDate(3, receipt.getDate());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Receipt receipt) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_RECEIPT)) {
            ps.setLong(1, receipt.getClientId());
            ps.setFloat(2, receipt.getTotalPrice());
            ps.setDate(3, receipt.getDate());
            ps.setLong(4, receipt.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(Receipt receipt) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_RECEIPT)) {
            ps.setLong(1, receipt.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Receipt> findAll() {
        List<Receipt> resultReceipts = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_RECEIPTS);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                Receipt receipt = new Receipt();
                receipt.setId(result.getLong(1));
                receipt.setClientId(result.getLong(2));
                receipt.setTotalPrice(result.getFloat(3));
                receipt.setDate(result.getDate(4));
                resultReceipts.add(receipt);
            }
            return resultReceipts;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultReceipts;
    }

    @Override
    public void deleteAll() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALL_RECEIPTS)) {
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
