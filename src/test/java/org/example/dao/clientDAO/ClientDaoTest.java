package org.example.dao.clientDAO;

import org.example.model.Client;
import org.example.service.CafeDbInitializer;
import org.example.utils.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
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
        newClient.setPhone("+123456789");
        newClient.setEmail("john.doe@example.com");
        newClient.setBirthday(Date.valueOf("1990-01-01"));
        newClient.setDiscount(20.0f);

        clientDao.save(newClient);

        List<Client> allClients = clientDao.findAll();
        assertEquals(3, allClients.size()); // Проверяем, что добавилась новая запись
    }

    @Test
    void saveMany_ShouldInsertMultipleClients_WhenCalled() {
        Client client1 = new Client();
        client1.setName("Alice");
        client1.setSurname("Smith");
        client1.setPhone("+987654321");
        client1.setEmail("alice.smith@example.com");
        client1.setBirthday(Date.valueOf("1995-05-05"));
        client1.setDiscount(10.0f);

        Client client2 = new Client();
        client2.setName("Bob");
        client2.setSurname("Brown");
        client2.setPhone("+111222333");
        client2.setEmail("bob.brown@example.com");
        client2.setBirthday(Date.valueOf("1985-08-15"));
        client2.setDiscount(15.0f);

        clientDao.saveMany(List.of(client1, client2));

        List<Client> allClients = clientDao.findAll();
        assertEquals(4, allClients.size()); // Проверяем, что добавились 2 новые записи
    }

    @Test
    void findWithMaxDiscount_ShouldReturnClientsWithMaxDiscount_WhenCalled() {
        List<Client> clients = clientDao.findWithMaxDiscount();
        assertEquals(1, clients.size());
        assertEquals(15.0f, clients.get(0).getDiscount()); // Максимальная скидка = 15.0f
    }

    @Test
    void findWithMinDiscount_ShouldReturnClientsWithMinDiscount_WhenCalled() {
        List<Client> clients = clientDao.findWithMinDiscount();
        assertEquals(1, clients.size());
        assertEquals(0.0f, clients.get(0).getDiscount()); // Минимальная скидка = 0.0f
    }

    @Test
    void findAvgDiscount_ShouldReturnAverageDiscount_WhenCalled() {
        Float avgDiscount = clientDao.findAvgDiscount();
        assertEquals(7.5f, avgDiscount); // Средняя скидка: (0.0f + 15.0f) / 2 = 7.5f
    }

    @Test
    void findYoungestClients_ShouldReturnClientsWithMaxBirthday_WhenCalled() {
        List<Client> clients = clientDao.findYoungestClients();
        assertEquals(2, clients.size());
        assertEquals(Date.valueOf("2004-01-01"), clients.get(0).getBirthday()); // Молодейший клиент с датой 2004-01-01
    }

    @Test
    void findClientsThatOrderedMenuTypeOnDate_ShouldReturnClients_WhenCalled() {
        List<Client> clients = clientDao.findClientsThatOrderedMenuTypeOnDate("Dessert", Date.valueOf("2024-12-20"));
        assertEquals(2, clients.size()); // Оба клиента заказывали блюда типа "Dessert" на дату 2024-12-20
    }

    @Test
    void findClientsThatOrderedMaxPriceOnDate_ShouldReturnClients_WhenCalled() {
        List<Client> clients = clientDao.findClientsThatOrderedMaxPriceOnDate(Date.valueOf("2024-12-20"));
        assertEquals(2, clients.size());
    }

    @Test
    void update_ShouldUpdateClient_WhenCalled() {
        List<Client> clients = clientDao.findAll();
        Client clientToUpdate = clients.get(0);

        clientToUpdate.setName("UpdatedName");
        clientToUpdate.setSurname("UpdatedSurname");
        clientToUpdate.setDiscount(25.0f);

        clientDao.update(clientToUpdate);

        List<Client> updatedClients = clientDao.findAll();
        Client updatedClient = updatedClients.get(0);

        assertEquals("UpdatedName", updatedClient.getName());
        assertEquals("UpdatedSurname", updatedClient.getSurname());
        assertEquals(25.0f, updatedClient.getDiscount());
    }

    @Test
    void delete_ShouldRemoveClient_WhenCalled() {
        List<Client> clients = clientDao.findAll();
        assertEquals(2, clients.size()); // Изначально 2 записи

        Client clientToDelete = clients.get(0);
        clientDao.delete(clientToDelete);

        List<Client> updatedClients = clientDao.findAll();
        assertEquals(1, updatedClients.size()); // Проверяем, что запись удалена
    }

    @Test
    void deleteAll_ShouldClearClientsTable_WhenCalled() {
        clientDao.deleteAll();

        List<Client> clients = clientDao.findAll();
        assertEquals(0, clients.size()); // Таблица должна быть пустой
    }
}
