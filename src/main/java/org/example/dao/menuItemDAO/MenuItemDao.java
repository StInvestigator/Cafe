package org.example.dao.menuItemDAO;

import org.example.dao.CRUDInterface;
import org.example.model.MenuItem;

import java.util.List;

public interface MenuItemDao extends CRUDInterface<MenuItem> {
    List<MenuItem> findAllWithType(String type);
}
