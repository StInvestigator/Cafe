package org.example;

import org.example.menu.MenuExecutor;
import org.example.service.CafeInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class App {

    @Autowired
    private MenuExecutor menuExecutor;

    @Autowired
    private CafeInitializer cafeInitializer;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        cafeInitializer.cafeInitialize();
        menuExecutor.startMenu();
    }
}
