package org.example.dao.receiptDAO;

import org.example.model.Receipt;
import org.example.service.CafeDbInitializer;
import org.example.utils.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.setProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiptDaoTest {

    private final ReceiptDao receiptDao = new ReceiptDaoImpl();

    @BeforeAll
    static void initTestDB() {
        setProperty("test", "true");
    }

    @BeforeEach
    void prepareTestData() {
        CafeDbInitializer.createTables();
        CreateTestData.insertData();
    }

    @Test
    void save_ShouldInsertReceiptIntoTable_WhenCalled() {
        Receipt newReceipt = new Receipt();
        newReceipt.setClientId(1L);
        newReceipt.setTotalPrice(200.0f);
        newReceipt.setDate(Date.valueOf("2024-12-21"));

        receiptDao.save(newReceipt);

        List<Receipt> allReceipts = receiptDao.findAll();
        assertEquals(4, allReceipts.size()); // Проверяем, что добавился новый чек (изначально было 3)
        Receipt insertedReceipt = allReceipts.get(3);
        assertEquals(1L, insertedReceipt.getClientId());
        assertEquals(200.0f, insertedReceipt.getTotalPrice());
        assertEquals(Date.valueOf("2024-12-21"), insertedReceipt.getDate());
    }

    @Test
    void saveMany_ShouldInsertMultipleReceipts_WhenCalled() {
        List<Receipt> newReceipts = new ArrayList<>();

        Receipt receipt1 = new Receipt();
        receipt1.setClientId(2L);
        receipt1.setTotalPrice(300.0f);
        receipt1.setDate(Date.valueOf("2024-12-22"));

        Receipt receipt2 = new Receipt();
        receipt2.setClientId(1L);
        receipt2.setTotalPrice(150.0f);
        receipt2.setDate(Date.valueOf("2024-12-23"));

        newReceipts.add(receipt1);
        newReceipts.add(receipt2);

        receiptDao.saveMany(newReceipts);

        List<Receipt> allReceipts = receiptDao.findAll();
        assertEquals(5, allReceipts.size()); // Проверяем, что добавились 2 новых чека
    }

    @Test
    void findAll_ShouldReturnAllReceipts_WhenCalled() {
        List<Receipt> receipts = receiptDao.findAll();
        assertEquals(3, receipts.size()); // Изначально в базе 3 чека
    }

    @Test
    void update_ShouldUpdateReceipt_WhenCalled() {
        List<Receipt> receipts = receiptDao.findAll();
        Receipt receiptToUpdate = receipts.get(0);

        receiptToUpdate.setClientId(2L);
        receiptToUpdate.setTotalPrice(250.0f);
        receiptToUpdate.setDate(Date.valueOf("2024-12-24"));

        receiptDao.update(receiptToUpdate);

        List<Receipt> updatedReceipts = receiptDao.findAll();
        Receipt updatedReceipt = updatedReceipts.get(0);

        assertEquals(2L, updatedReceipt.getClientId());
        assertEquals(250.0f, updatedReceipt.getTotalPrice());
        assertEquals(Date.valueOf("2024-12-24"), updatedReceipt.getDate());
    }

    @Test
    void delete_ShouldRemoveReceipt_WhenCalled() {
        List<Receipt> receipts = receiptDao.findAll();
        assertEquals(3, receipts.size()); // Изначально 3 чека

        Receipt receiptToDelete = receipts.get(0);
        receiptDao.delete(receiptToDelete);

        List<Receipt> updatedReceipts = receiptDao.findAll();
        assertEquals(2, updatedReceipts.size()); // Проверяем, что чек удален
    }

    @Test
    void deleteAll_ShouldClearReceiptsTable_WhenCalled() {
        receiptDao.deleteAll();

        List<Receipt> receipts = receiptDao.findAll();
        assertEquals(0, receipts.size()); // Таблица должна быть пустой
    }
}
