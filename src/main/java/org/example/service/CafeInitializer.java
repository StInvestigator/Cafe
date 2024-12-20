package org.example.service;


import org.example.exception.FileException;

import static java.lang.System.setProperty;

public class CafeInitializer {

    public void schoolInitialize() {
        setProperty("test", "false");
        try {
            CafeDbInitializer.createTables();
            CafeDbInitializer.deleteAllRowsInDB();
            CafeDbInitializer.createMenu();
            CafeDbInitializer.createRandomClients();
            CafeDbInitializer.createRandomWorkers();
            CafeDbInitializer.createSchedule();
            CafeDbInitializer.createRandomReceipts();
            CafeDbInitializer.createRandomOrders();
        } catch (FileException e) {
            System.err.println(e.getMessage());
        }
    }
}
