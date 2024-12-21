package org.example.dao.menuItemDAO;

import org.example.model.MenuItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class MenuItemRowMapper implements RowMapper<MenuItem> {
    @Override
    public MenuItem mapRow(ResultSet resultSet, int i) throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(resultSet.getLong("id"));
        menuItem.setName(resultSet.getString("name"));
        menuItem.setPrice(resultSet.getFloat("price"));
        menuItem.setType(resultSet.getString("type"));
        return menuItem;
    }
}
