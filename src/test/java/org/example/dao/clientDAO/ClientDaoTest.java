package org.example.dao.clientDAO;

import org.example.model.Client;
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

public class ClientDaoTest {

    private final ClientDao clientDao = new ClientDaoImpl();

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
    void save_ShouldInsertClientIntoTable_WhenCalled() {
        Client newClient = new Client();
        newClient.setName("John");
        newClient.setSurname("Doe");
        newClient.setPhone("1234567890");
        newClient.setEmail("john.doe@example.com");
        newClient.setBirthday(Date.valueOf("1990-01-01"));
        newClient.setDiscount(10.5f);

        clientDao.save(newClient);

        List<Client> allClients = clientDao.findAll();

        assertEquals(3, allClients.size()); // Проверяем, что клиент добавился (изначально было 2)
        Client insertedClient = allClients.get(2);
        assertEquals("John", insertedClient.getName());
        assertEquals("Doe", insertedClient.getSurname());
    }

    @Test
    void saveMany_ShouldInsertMultipleClients_WhenCalled() {
        List<Client> newClients = new ArrayList<>();

        Client client1 = new Client();
        client1.setName("Alice");
        client1.setSurname("Smith");
        client1.setPhone("1111111111");
        client1.setEmail("alice@example.com");
        client1.setBirthday(Date.valueOf("1985-03-15"));
        client1.setDiscount(5.0f);

        Client client2 = new Client();
        client2.setName("Bob");
        client2.setSurname("Brown");
        client2.setPhone("2222222222");
        client2.setEmail("bob@example.com");
        client2.setBirthday(Date.valueOf("1992-07-08"));
        client2.setDiscount(7.5f);

        newClients.add(client1);
        newClients.add(client2);

        clientDao.saveMany(newClients);

        List<Client> allClients = clientDao.findAll();
        assertEquals(4, allClients.size()); // Проверяем, что добавились 2 клиента (изначально было 2)
    }

    @Test
    void findAll_ShouldReturnAllClients_WhenCalled() {
        List<Client> clients = clientDao.findAll();
        assertEquals(2, clients.size()); // В базе изначально 2 клиента
    }

    @Test
    void update_ShouldUpdateClient_WhenCalled() {
        List<Client> clients = clientDao.findAll();
        Client clientToUpdate = clients.get(0);

        clientToUpdate.setName("UpdatedName");
        clientToUpdate.setSurname("UpdatedSurname");
        clientToUpdate.setPhone("+9876543210");
        clientToUpdate.setEmail("updated.email@example.com");
        clientToUpdate.setDiscount(20.0f);

        clientDao.update(clientToUpdate);

        List<Client> updatedClients = clientDao.findAll();
        Client updatedClient = updatedClients.get(0);

        assertEquals("UpdatedName", updatedClient.getName());
        assertEquals("UpdatedSurname", updatedClient.getSurname());
        assertEquals("+9876543210", updatedClient.getPhone());
        assertEquals("updated.email@example.com", updatedClient.getEmail());
        assertEquals(20.0f, updatedClient.getDiscount());
    }

    @Test
    void delete_ShouldRemoveClient_WhenCalled() {
        List<Client> clients = clientDao.findAll();
        assertEquals(2, clients.size()); // Изначально 2 клиента

        Client clientToDelete = clients.get(0);
        clientDao.delete(clientToDelete);

        List<Client> updatedClients = clientDao.findAll();
        assertEquals(1, updatedClients.size()); // Проверяем, что остался 1 клиент
    }

    @Test
    void deleteAll_ShouldClearClientsTable_WhenCalled() {
        clientDao.deleteAll();

        List<Client> clients = clientDao.findAll();
        assertEquals(0, clients.size()); // Таблица должна быть пустой
    }
}
