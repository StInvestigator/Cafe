package org.example.dao.clientDAO;

import org.example.dao.CRUDInterface;
import org.example.model.Client;

import java.sql.Date;
import java.util.List;

public interface ClientDao extends CRUDInterface<Client> {
    List<Client> findWithMinDiscount();
    List<Client> findWithMaxDiscount();
    Float findAvgDiscount();
    List<Client> findYoungestClients();
    List<Client> findOldestClients();
    List<Client> findClientsThatOrderedMenuTypeOnDate(String type, Date date);
    List<Client> findClientsThatOrderedMaxPriceOnDate(Date date);
}
