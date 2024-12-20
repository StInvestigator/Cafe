package org.example.dao.workerDAO;

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

public class WorkerDaoTest {

    private final WorkerDao workerDao = new WorkerDaoImpl();

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
    void save_ShouldInsertWorkerIntoTable_WhenCalled() {
        Worker newWorker = new Worker();
        newWorker.setName("John");
        newWorker.setSurname("Doe");
        newWorker.setPhone("+1234567890");
        newWorker.setEmail("john.doe@example.com");
        newWorker.setPosition("Barista");

        workerDao.save(newWorker);

        List<Worker> allWorkers = workerDao.findAll();

        assertEquals(4, allWorkers.size()); // Проверяем, что работник добавился (изначально было 3)
        Worker insertedWorker = allWorkers.get(3);
        assertEquals("John", insertedWorker.getName());
        assertEquals("Doe", insertedWorker.getSurname());
    }

    @Test
    void saveMany_ShouldInsertMultipleWorkers_WhenCalled() {
        List<Worker> newWorkers = new ArrayList<>();

        Worker worker1 = new Worker();
        worker1.setName("Alice");
        worker1.setSurname("Smith");
        worker1.setPhone("+1111111111");
        worker1.setEmail("alice@example.com");
        worker1.setPosition("Barista");

        Worker worker2 = new Worker();
        worker2.setName("Bob");
        worker2.setSurname("Brown");
        worker2.setPhone("+2222222222");
        worker2.setEmail("bob@example.com");
        worker2.setPosition("Waiter");

        newWorkers.add(worker1);
        newWorkers.add(worker2);

        workerDao.saveMany(newWorkers);

        List<Worker> allWorkers = workerDao.findAll();
        assertEquals(5, allWorkers.size()); // Проверяем, что добавились 2 работника (изначально было 3)
    }

    @Test
    void findAll_ShouldReturnAllWorkers_WhenCalled() {
        List<Worker> workers = workerDao.findAll();
        assertEquals(3, workers.size()); // В базе изначально 3 работника
    }

    @Test
    void update_ShouldUpdateWorker_WhenCalled() {
        List<Worker> workers = workerDao.findAll();
        Worker workerToUpdate = workers.get(0);

        workerToUpdate.setName("UpdatedName");
        workerToUpdate.setSurname("UpdatedSurname");
        workerToUpdate.setPhone("+9876543210");
        workerToUpdate.setEmail("updated.email@example.com");
        workerToUpdate.setPosition("Barista");

        workerDao.update(workerToUpdate);

        List<Worker> updatedWorkers = workerDao.findAll();
        Worker updatedWorker = updatedWorkers.get(0);

        assertEquals("UpdatedName", updatedWorker.getName());
        assertEquals("UpdatedSurname", updatedWorker.getSurname());
        assertEquals("+9876543210", updatedWorker.getPhone());
        assertEquals("updated.email@example.com", updatedWorker.getEmail());
        assertEquals("Barista", updatedWorker.getPosition());
    }

    @Test
    void delete_ShouldRemoveWorker_WhenCalled() {
        List<Worker> workers = workerDao.findAll();
        assertEquals(3, workers.size()); // Изначально 3 работника

        Worker workerToDelete = workers.get(0);
        workerDao.delete(workerToDelete);

        List<Worker> updatedWorkers = workerDao.findAll();
        assertEquals(2, updatedWorkers.size()); // Проверяем, что остался 2 работника
    }

    @Test
    void deleteAll_ShouldClearWorkersTable_WhenCalled() {
        workerDao.deleteAll();

        List<Worker> workers = workerDao.findAll();
        assertEquals(0, workers.size()); // Таблица должна быть пустой
    }

    @Test
    void findAllWorkersWithPosition_ShouldReturnWorkersWithSpecificPosition_WhenCalled() {
        List<Worker> workersWithPosition = workerDao.findAllWorkersWithPosition("Waiter");
        assertEquals(1, workersWithPosition.size()); // Только 1 работник с позицией "Manager"

        Worker worker = workersWithPosition.get(0);
        assertEquals("name1", worker.getName());
        assertEquals("surname1", worker.getSurname());
    }
}
