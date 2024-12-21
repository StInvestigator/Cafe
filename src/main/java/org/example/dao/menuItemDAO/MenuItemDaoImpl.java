package org.example.dao.menuItemDAO;

import org.example.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<MenuItem> menuItemRowMapper;

    @Override
    public void save(MenuItem item) {
        try {
            jdbcTemplate.update(SAVE_MENU_ITEM, item.getName(), item.getType(), item.getPrice());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveMany(List<MenuItem> items) {
        jdbcTemplate.batchUpdate(SAVE_MENU_ITEM,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, items.get(i).getName());
                        ps.setString(2, items.get(i).getType());
                        ps.setFloat(3, items.get(i).getPrice());
                    }

                    @Override
                    public int getBatchSize() {
                        return items.size();
                    }
                });
    }

    @Override
    public void update(MenuItem item) {
        try {
            jdbcTemplate.update(UPDATE_MENU_ITEM, item.getName(), item.getType(), item.getPrice(), item.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(MenuItem item) {
        try {
            jdbcTemplate.update(DELETE_MENU_ITEM, item.getId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<MenuItem> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL_MENU_ITEMS, menuItemRowMapper);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteAll() {
        try {
            jdbcTemplate.update(DELETE_ALL_MENU_ITEMS);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<MenuItem> findAllWithType(String type) {
        try {
            return jdbcTemplate.query(FIND_ALL_BY_TYPE, menuItemRowMapper, type);
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}

