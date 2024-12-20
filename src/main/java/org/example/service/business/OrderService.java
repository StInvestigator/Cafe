package org.example.service.business;

import org.example.dao.menuItemDAO.MenuItemDao;
import org.example.dao.menuItemDAO.MenuItemDaoImpl;
import org.example.dao.orderDAO.OrderDao;
import org.example.dao.orderDAO.OrderDaoImpl;
import org.example.dao.workerDAO.WorkerDao;
import org.example.dao.workerDAO.WorkerDaoImpl;
import org.example.model.MenuItem;
import org.example.model.Order;
import org.example.model.Worker;

import java.util.List;

public class OrderService {
    public static String allOrdersToString(OrderDao orderDao, MenuItemDao menuItemDao, WorkerDao workerDao) {;
        List<Order> orders = orderDao.findAll();

        StringBuilder result = new StringBuilder();
        int counter = 1;
        for (Order order : orders) {
            result.append(counter++).append(". ").append("\n").append(OrderToString(order, menuItemDao, workerDao));
        }
        return result.toString();
    }

    public static String OrderToString(Order order, MenuItemDao menuItemDao, WorkerDao workerDao) {
        List<MenuItem> items = menuItemDao.findAll();
        List<Worker> workers = workerDao.findAll();

        StringBuilder result = new StringBuilder();
        result.append("Waiter who received: ").append(workers.stream().filter(x -> x.getPosition().equals("Waiter") && x.getId() == order.getWaiterId()).findFirst().get())
                .append("\nCook: ").append(workers.stream().filter(x -> !x.getPosition().equals("Waiter") && x.getId() == order.getWorkerId()).findFirst().get())
                .append("\nItem on menu: ").append(items.stream().filter(x -> x.getId() == order.getMenuPositionId()).findFirst().get())
                .append("\nPrice: ").append(order.getPrice())
                .append("\nReceipt Id: ").append(order.getReceiptId())
                .append('\n');
        return result.toString();
    }
}
