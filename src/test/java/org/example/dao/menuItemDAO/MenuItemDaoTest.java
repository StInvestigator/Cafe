package org.example.dao.menuItemDAO;

import org.example.model.MenuItem;
import org.example.service.CafeDbInitializer;
import org.example.utils.CreateTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.setProperty;
import static junit.framework.Assert.assertEquals;

public class MenuItemDaoTest {

    private final MenuItemDao menuItemDao = new MenuItemDaoImpl();

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
    void save_ShouldInsertMenuItemIntoTable_WhenCalled() {
        MenuItem newItem = new MenuItem();
        newItem.setName("NewItem");
        newItem.setType("Dessert");
        newItem.setPrice(123.45f);

        menuItemDao.save(newItem);

        List<MenuItem> allItems = menuItemDao.findAll();

        assertEquals(3, allItems.size()); // Проверяем, что элемент добавился (изначально было 2)
        MenuItem insertedItem = allItems.get(2);
        assertEquals("NewItem", insertedItem.getName());
        assertEquals("Dessert", insertedItem.getType());
        assertEquals(123.45f, insertedItem.getPrice());
    }

    @Test
    void saveMany_ShouldInsertMultipleMenuItems_WhenCalled() {
        List<MenuItem> newItems = new ArrayList<>();

        MenuItem item1 = new MenuItem();
        item1.setName("Item1");
        item1.setType("Drink");
        item1.setPrice(10.50f);

        MenuItem item2 = new MenuItem();
        item2.setName("Item2");
        item2.setType("Dessert");
        item2.setPrice(20.75f);

        newItems.add(item1);
        newItems.add(item2);

        menuItemDao.saveMany(newItems);

        List<MenuItem> allItems = menuItemDao.findAll();
        assertEquals(4, allItems.size()); // Проверяем, что добавились 2 элемента (изначально было 2)
    }

    @Test
    void findAll_ShouldReturnAllMenuItems_WhenCalled() {
        List<MenuItem> items = menuItemDao.findAll();
        assertEquals(2, items.size()); // В базе изначально 2 элемента
    }

    @Test
    void update_ShouldUpdateMenuItem_WhenCalled() {
        List<MenuItem> items = menuItemDao.findAll();
        MenuItem itemToUpdate = items.get(0);

        itemToUpdate.setName("UpdatedName");
        itemToUpdate.setType("Drink");
        itemToUpdate.setPrice(99.99f);

        menuItemDao.update(itemToUpdate);

        List<MenuItem> updatedItems = menuItemDao.findAll();
        MenuItem updatedItem = updatedItems.get(0);

        assertEquals("UpdatedName", updatedItem.getName());
        assertEquals("Drink", updatedItem.getType());
        assertEquals(99.99f, updatedItem.getPrice());
    }

    @Test
    void delete_ShouldRemoveMenuItem_WhenCalled() {
        List<MenuItem> items = menuItemDao.findAll();
        assertEquals(2, items.size()); // Изначально 2 элемента

        MenuItem itemToDelete = items.get(0);
        menuItemDao.delete(itemToDelete);

        List<MenuItem> updatedItems = menuItemDao.findAll();
        assertEquals(1, updatedItems.size()); // Проверяем, что остался 1 элемент
    }

    @Test
    void deleteAll_ShouldClearMenuTable_WhenCalled() {
        menuItemDao.deleteAll();

        List<MenuItem> items = menuItemDao.findAll();
        assertEquals(0, items.size()); // Таблица должна быть пустой
    }

    @Test
    void findAllWithType_ShouldReturnItemsOfType_WhenCalled() {
        List<MenuItem> items = menuItemDao.findAllWithType("Drink");
        assertEquals(1, items.size()); // Проверяем, что возвращаются только элементы указанного типа
        assertEquals("name1", items.get(0).getName());
    }
}
