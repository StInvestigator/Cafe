package org.example.dao.orderDAO;

import org.example.dao.CRUDInterface;
import org.example.model.Client;
import org.example.model.MenuItem;
import org.example.model.Order;

import java.util.List;

public interface OrderDao extends CRUDInterface<Order> {
    List<Order> getOrdersByClient(Client client);
}
