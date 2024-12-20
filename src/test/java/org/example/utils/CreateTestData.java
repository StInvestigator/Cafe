package org.example.utils;

import org.example.dao.ConnectionFactory;
import org.example.exception.ConnectionDBException;
import org.example.service.PropertyFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.setProperty;

public class CreateTestData {

    private static final String SQL_SCRIPT_INSERT_TABLES;
    private static final String SQL_SCRIPT_DROP_TABLES;

    static {
        setProperty("test", "true");
        SQL_SCRIPT_INSERT_TABLES = PropertyFactory.getInstance().
                getProperty().getProperty("db.sqlScriptInsertTables");
        SQL_SCRIPT_DROP_TABLES = PropertyFactory.getInstance().
                getProperty().getProperty("db.sqlScriptDropTables");
    }

    public static void insertData() {
        executeQuare(SQL_SCRIPT_INSERT_TABLES);
    }


    public static void dropTables() {
        executeQuare(SQL_SCRIPT_DROP_TABLES);
    }

    private static void executeQuare(String sourceString) {

        try (Connection conn = ConnectionFactory.getInstance().makeConnection()) {
            try (Stream<String> lineStream = Files.lines(Paths.get(sourceString))) {
                StringBuilder createTablesQuery = new StringBuilder();

                for (var currentString : lineStream.toList()) {
                    createTablesQuery.append(currentString).append(" ");
                }

                try (PreparedStatement ps = conn.prepareStatement(createTablesQuery.toString())) {
                    ps.execute();
                }

            } catch (IOException | SQLException e) {
                System.err.println(e.getMessage());
            }
        } catch (ConnectionDBException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private CreateTestData() {
    }

}
