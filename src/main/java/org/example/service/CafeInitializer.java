package org.example.service;


import org.example.exception.FileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.System.setProperty;

@Service
public class CafeInitializer {

    @Autowired
    private CafeDbInitializer cafeDbInitializer;

    public void cafeInitialize() {
        try {
            cafeDbInitializer.createTables();
            cafeDbInitializer.deleteAllRowsInDB();
            cafeDbInitializer.createMenu();
            cafeDbInitializer.createRandomClients();
            cafeDbInitializer.createRandomWorkers();
            cafeDbInitializer.createSchedule();
            cafeDbInitializer.createRandomReceipts();
            cafeDbInitializer.createRandomOrders();
        } catch (FileException e) {
            System.err.println(e.getMessage());
        }
    }
}
