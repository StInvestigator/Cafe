package org.example.dao.orderDAO;

import org.example.dao.clientDAO.ClientDao;
import org.example.model.Client;
import org.example.model.Order;
import org.example.service.CafeDbInitializer;
import org.example.utils.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
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
        newOrder.setWorkerId(1L);
        newOrder.setWaiterId(2L);
        newOrder.setMenuPositionId(1L);
        newOrder.setReceiptId(1L);
        newOrder.setPrice(75.25f);

        orderDao.save(newOrder);

        List<Order> allOrders = orderDao.findAll();
        assertEquals(5, allOrders.size()); // Было 4 заказа, добавился еще один
    }

    @Test
    void saveMany_ShouldInsertMultipleOrders_WhenCalled() {
        Order order1 = new Order();
        order1.setWorkerId(2L);
        order1.setWaiterId(1L);
        order1.setMenuPositionId(2L);
        order1.setReceiptId(1L);
        order1.setPrice(100.0f);

        Order order2 = new Order();
        order2.setWorkerId(3L);
        order2.setWaiterId(1L);
        order2.setMenuPositionId(1L);
        order2.setReceiptId(2L);
        order2.setPrice(50.50f);

        orderDao.saveMany(List.of(order1, order2));

        List<Order> allOrders = orderDao.findAll();
        assertEquals(6, allOrders.size()); // Было 4 заказа, добавилось еще два
    }

    @Test
    void findOrdersByClient_ShouldReturnOrdersForClient_WhenCalled() {
        Client client = new Client();
        client.setId(1L);
        List<Order> clientOrders = orderDao.findOrdersByClient(client);
        assertEquals(1, clientOrders.size()); // Клиент 1 делал 1 заказ
    }

    @Test
    void findOrdersByDate_ShouldReturnOrdersForDate_WhenCalled() {
        List<Order> orders = orderDao.findOrdersByDate(Date.valueOf("2024-12-20"));
        assertEquals(3, orders.size()); // На дату 2024-12-20 было сделано 3 заказа
    }

    @Test
    void findOrdersBetweenDates_ShouldReturnOrdersWithinRange_WhenCalled() {
        List<Order> orders = orderDao.findOrdersBetweenDates(Date.valueOf("2024-12-20"), Date.valueOf("2024-12-21"));
        assertEquals(4, orders.size()); // Заказы на 2024-12-20 и 2024-12-21
    }

    @Test
    void findOrdersOnDateByMenuItemType_ShouldReturnOrders_WhenCalled() {
        List<Order> orders = orderDao.findOrdersOnDateByMenuItemType(Date.valueOf("2024-12-20"), "Dessert");
        assertEquals(2, orders.size()); // 2 заказа с типом "Dessert" на указанную дату
    }

    @Test
    void findAvgPriceOnDate_ShouldReturnAveragePrice_WhenCalled() {
        Float avgPrice = orderDao.findAvgPriceOnDate(Date.valueOf("2024-12-20"));
        assertEquals(83.5f, avgPrice); // Средняя цена = 100
    }

    @Test
    void findMaxPriceOnDate_ShouldReturnMaxPrice_WhenCalled() {
        Float maxPrice = orderDao.findMaxPriceOnDate(Date.valueOf("2024-12-20"));
        assertEquals(100.0f, maxPrice); // Максимальная цена = 100
    }

    @Test
    void update_ShouldUpdateOrder_WhenCalled() {
        List<Order> allOrders = orderDao.findAll();
        Order orderToUpdate = allOrders.get(0);

        orderToUpdate.setPrice(200.0f);
        orderDao.update(orderToUpdate);

        List<Order> updatedOrders = orderDao.findAll();
        Order updatedOrder = updatedOrders.get(0);
        assertEquals(200.0f, updatedOrder.getPrice()); // Проверяем обновление цены
    }

    @Test
    void delete_ShouldRemoveOrder_WhenCalled() {
        List<Order> allOrders = orderDao.findAll();
        assertEquals(4, allOrders.size()); // Было 4 заказа

        Order orderToDelete = allOrders.get(0);
        orderDao.delete(orderToDelete);

        List<Order> updatedOrders = orderDao.findAll();
        assertEquals(3, updatedOrders.size()); // После удаления стало 3
    }

    @Test
    void deleteAll_ShouldClearOrdersTable_WhenCalled() {
        orderDao.deleteAll();

        List<Order> orders = orderDao.findAll();
        assertEquals(0, orders.size()); // Таблица заказов должна быть пустой
    }
}
