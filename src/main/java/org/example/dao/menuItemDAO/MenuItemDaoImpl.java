package org.example.dao.menuItemDAO;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.model.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDaoImpl implements MenuItemDao {

    private static final String SAVE_MENU_ITEM = "INSERT INTO menu(name, type_id, price) " +
            "VALUES(?, (SELECT id FROM menu_types WHERE menu_types.name = ?), ?)";
    private static final String FIND_ALL_MENU_ITEMS = "SELECT menu.id, menu.name, menu.price, " +
            "(SELECT menu_types.name FROM menu_types WHERE menu_types.id = menu.type_id) AS type " +
            "FROM menu ORDER BY id";
    private static final String DELETE_ALL_MENU_ITEMS = "DELETE FROM menu";
    private static final String UPDATE_MENU_ITEM = "UPDATE menu SET name = ?, " +
            "type_id = (SELECT id FROM menu_types WHERE menu_types.name = ?), price = ? WHERE id = ?";
    private static final String DELETE_MENU_ITEM = "DELETE FROM menu WHERE id = ?";
    private static final String FIND_ALL_BY_TYPE = "SELECT m.id, m.name, m.price, mt.name AS type " +
            "FROM menu m JOIN menu_types mt ON mt.id = m.type_id " +
            "WHERE mt.name = ? ORDER BY m.id";

    @Override
    public void save(MenuItem item) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_MENU_ITEM)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getType());
            ps.setFloat(3, item.getPrice());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void saveMany(List<MenuItem> items) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE_MENU_ITEM)) {

            for (MenuItem currentItem : items) {
                ps.setString(1, currentItem.getName());
                ps.setString(2, currentItem.getType());
                ps.setFloat(3, currentItem.getPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(MenuItem item) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_MENU_ITEM)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getType());
            ps.setFloat(3, item.getPrice());
            ps.setLong(4, item.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(MenuItem item) {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_MENU_ITEM)) {
            ps.setLong(1, item.getId());
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<MenuItem> findAll() {
        List<MenuItem> resultMenuItems = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_MENU_ITEMS);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(result.getLong(1));
                menuItem.setName(result.getString(2));
                menuItem.setPrice(result.getFloat(3));
                menuItem.setType(result.getString(4));
                resultMenuItems.add(menuItem);
            }
            return resultMenuItems;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultMenuItems;
    }

    @Override
    public void deleteAll() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALL_MENU_ITEMS)) {
            ps.execute();
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<MenuItem> findAllWithType(String type) {
        List<MenuItem> resultMenuItems = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().makeConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_BY_TYPE)) {
            ps.setString(1, type);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(result.getLong(1));
                menuItem.setName(result.getString(2));
                menuItem.setPrice(result.getFloat(3));
                menuItem.setType(result.getString(4));
                resultMenuItems.add(menuItem);
            }
            return resultMenuItems;
        } catch (ConnectionDBException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultMenuItems;
    }
}

