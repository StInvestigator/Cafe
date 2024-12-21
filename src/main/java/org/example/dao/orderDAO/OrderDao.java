package org.example.dao.orderDAO;

import org.example.dao.CRUDInterface;
import org.example.model.Client;
import org.example.model.Order;

import java.sql.Date;
import java.util.List;

public interface OrderDao extends CRUDInterface<Order> {
    List<Order> findOrdersByClient(Client client);
    List<Order> findOrdersByDate(Date date);
    List<Order> findOrdersBetweenDates(Date start, Date end);
    List<Order> findOrdersOnDateByMenuItemType(Date date, String type);
    Float findAvgPriceOnDate(Date date);
    Float findMaxPriceOnDate(Date date);
}
