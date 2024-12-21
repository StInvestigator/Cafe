package org.example.service.business;

import org.example.dao.orderDAO.OrderDao;
import org.example.dao.orderDAO.OrderDaoImpl;
import org.example.dao.menuItemDAO.MenuItemDao;
import org.example.dao.menuItemDAO.MenuItemDaoImpl;
import org.example.dao.workerDAO.WorkerDao;
import org.example.dao.workerDAO.WorkerDaoImpl;
import org.example.model.Order;
import org.example.model.MenuItem;
import org.example.model.Worker;
import org.example.service.CafeDbInitializer;
import org.example.utils.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.setProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private static final OrderDao orderDao = mock(OrderDaoImpl.class);
    private static final MenuItemDao menuItemDao = mock(MenuItemDaoImpl.class);
    private static final WorkerDao workerDao = mock(WorkerDaoImpl.class);
    @BeforeAll
    static void initMock() {
        when(orderDao.findAll()).thenReturn(createMockOrders());
        when(menuItemDao.findAll()).thenReturn(createMockMenuItems());
        when(workerDao.findAll()).thenReturn(createMockWorkers());
    }


    @Test
    void allOrdersToString_ShouldReturnCorrectString_WhenCalled() {
        String expected = "1. \n" +
                "Waiter who received: Worker(id=1, name=name1, surname=surname1, phone=+123123, email=email@gmail.com, position=Waiter)\n" +
                "Cook: Worker(id=3, name=name3, surname=surname3, phone=+123123, email=email@gmail.com, position=Pastry Chef)\n" +
                "Item on menu: MenuItem(id=2, name=name2, type=Dessert, price=100.0)\n" +
                "Price: 100.0\n" +
                "Receipt Id: 1\n" +
                "2. \n" +
                "Waiter who received: Worker(id=1, name=name1, surname=surname1, phone=+123123, email=email@gmail.com, position=Waiter)\n" +
                "Cook: Worker(id=3, name=name3, surname=surname3, phone=+123123, email=email@gmail.com, position=Pastry Chef)\n" +
                "Item on menu: MenuItem(id=2, name=name2, type=Dessert, price=100.0)\n" +
                "Price: 100.0\n" +
                "Receipt Id: 2\n";

        String result = OrderService.allOrdersToString(orderDao, menuItemDao, workerDao);

        assertEquals(expected, result);
    }

    @Test
    void OrderToString_ShouldReturnCorrectString_WhenCalled() {
        Order order = new Order();
        order.setWaiterId(1);
        order.setWorkerId(3);
        order.setMenuPositionId(2);
        order.setPrice(100f);
        order.setReceiptId(1);


        String expected = "Waiter who received: Worker(id=1, name=name1, surname=surname1, phone=+123123, email=email@gmail.com, position=Waiter)\n" +
                "Cook: Worker(id=3, name=name3, surname=surname3, phone=+123123, email=email@gmail.com, position=Pastry Chef)\n" +
                "Item on menu: MenuItem(id=2, name=name2, type=Dessert, price=100.0)\n" +
                "Price: 100.0\n" +
                "Receipt Id: 1\n";

        String result = OrderService.OrderToString(order, menuItemDao, workerDao);

        assertEquals(expected, result);
    }

    private static List<Order> createMockOrders() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setWaiterId(1L);
        order1.setWorkerId(3L);
        order1.setMenuPositionId(2L);
        order1.setPrice(100f);
        order1.setReceiptId(1);
        orders.add(order1);

        Order order2 = new Order();
        order2.setWaiterId(1L);
        order2.setWorkerId(3L);
        order2.setMenuPositionId(2L);
        order2.setPrice(100f);
        order2.setReceiptId(2);
        orders.add(order2);

        return orders;
    }

    private static List<MenuItem> createMockMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        MenuItem item1 = new MenuItem();
        item1.setId(2L);
        item1.setName("name2");
        item1.setType("Dessert");
        item1.setPrice(100f);
        menuItems.add(item1);

        return menuItems;
    }

    private static List<Worker> createMockWorkers() {
        List<Worker> workers = new ArrayList<>();
        Worker waiter = new Worker();
        waiter.setId(1L);
        waiter.setName("name1");
        waiter.setSurname("surname1");
        waiter.setPhone("+123123");
        waiter.setEmail("email@gmail.com");
        waiter.setPosition("Waiter");
        workers.add(waiter);

        Worker cook = new Worker();
        cook.setId(3L);
        cook.setName("name3");
        cook.setSurname("surname3");
        cook.setPhone("+123123");
        cook.setEmail("email@gmail.com");
        cook.setPosition("Pastry Chef");
        workers.add(cook);

        return workers;
    }
}
