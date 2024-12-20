package org.example.dao.orderDAO;

import org.example.model.Client;
import org.example.model.Order;
import org.example.service.CafeDbInitializer;
import org.example.utils.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.setProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderDaoTest {

    private final OrderDao orderDao = new OrderDaoImpl();

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
    void save_ShouldInsertOrderIntoTable_WhenCalled() {
        Order newOrder = new Order();
        newOrder.setWorkerId(2L);
        newOrder.setMenuPositionId(1L);
        newOrder.setReceiptId(2L);
        newOrder.setPrice(75.25f);
        newOrder.setWaiterId(1L);

        orderDao.save(newOrder);

        List<Order> allOrders = orderDao.findAll();
        assertEquals(4, allOrders.size()); // Проверяем, что добавился новый заказ (изначально было 3)
        Order insertedOrder = allOrders.get(3);
        assertEquals(2L, insertedOrder.getWorkerId());
        assertEquals(1L, insertedOrder.getMenuPositionId());
        assertEquals(2L, insertedOrder.getReceiptId());
        assertEquals(75.25f, insertedOrder.getPrice());
        assertEquals(1L, insertedOrder.getWaiterId());
    }

    @Test
    void saveMany_ShouldInsertMultipleOrders_WhenCalled() {
        List<Order> newOrders = new ArrayList<>();

        Order order1 = new Order();
        order1.setWorkerId(1L);
        order1.setMenuPositionId(2L);
        order1.setReceiptId(1L);
        order1.setPrice(120.0f);
        order1.setWaiterId(2L);

        Order order2 = new Order();
        order2.setWorkerId(2L);
        order2.setMenuPositionId(1L);
        order2.setReceiptId(2L);
        order2.setPrice(55.75f);
        order2.setWaiterId(3L);

        newOrders.add(order1);
        newOrders.add(order2);

        orderDao.saveMany(newOrders);

        List<Order> allOrders = orderDao.findAll();
        assertEquals(5, allOrders.size()); // Проверяем, что добавились 2 новых заказа
    }

    @Test
    void findAll_ShouldReturnAllOrders_WhenCalled() {
        List<Order> orders = orderDao.findAll();
        assertEquals(3, orders.size()); // Изначально в базе 3 заказа
    }

    @Test
    void update_ShouldUpdateOrder_WhenCalled() {
        List<Order> orders = orderDao.findAll();
        Order orderToUpdate = orders.get(0);

        orderToUpdate.setWorkerId(1L);
        orderToUpdate.setMenuPositionId(1L);
        orderToUpdate.setReceiptId(2L);
        orderToUpdate.setPrice(75.0f);
        orderToUpdate.setWaiterId(2L);

        orderDao.update(orderToUpdate);

        List<Order> updatedOrders = orderDao.findAll();
        Order updatedOrder = updatedOrders.get(0);

        assertEquals(1L, updatedOrder.getWorkerId());
        assertEquals(1L, updatedOrder.getMenuPositionId());
        assertEquals(2L, updatedOrder.getReceiptId());
        assertEquals(75.0f, updatedOrder.getPrice());
        assertEquals(2L, updatedOrder.getWaiterId());
    }

    @Test
    void delete_ShouldRemoveOrder_WhenCalled() {
        List<Order> orders = orderDao.findAll();
        assertEquals(3, orders.size()); // Изначально 3 заказа

        Order orderToDelete = orders.get(0);
        orderDao.delete(orderToDelete);

        List<Order> updatedOrders = orderDao.findAll();
        assertEquals(2, updatedOrders.size()); // Проверяем, что заказ удален
    }

    @Test
    void deleteAll_ShouldClearOrdersTable_WhenCalled() {
        orderDao.deleteAll();

        List<Order> orders = orderDao.findAll();
        assertEquals(0, orders.size()); // Таблица должна быть пустой
    }

    @Test
    void getOrdersByClient_ShouldReturnOrdersOfSpecificClient_WhenCalled() {
        Client client = new Client();
        client.setId(2L); // Клиент с ID 2

        List<Order> clientOrders = orderDao.getOrdersByClient(client);

        assertEquals(2, clientOrders.size()); // У клиента с ID 2 два заказа
        assertEquals(2L, clientOrders.get(0).getReceiptId());
        assertEquals(2L, clientOrders.get(1).getReceiptId());
    }
}
