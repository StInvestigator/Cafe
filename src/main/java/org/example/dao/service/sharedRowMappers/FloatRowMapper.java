package org.example.dao.service.sharedRowMappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class FloatRowMapper implements RowMapper<Float> {
    @Override
    public Float mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getFloat(1);
    }
}
