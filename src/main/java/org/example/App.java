package org.example;

import org.example.service.CafeInitializer;

import static org.example.menu.MenuExecutor.startMenu;

public class App
{
    public static void main( String[] args )
    {
        System.setProperty("test", "false");

        CafeInitializer cafe = new CafeInitializer();
        cafe.cafeInitialize();
        startMenu();
    }
}
