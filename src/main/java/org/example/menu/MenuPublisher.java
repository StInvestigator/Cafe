package org.example.menu;

import org.example.model.MenuItem;
import org.springframework.stereotype.Service;

import java.util.List;

public class MenuPublisher {

    private static final String ACTION_STRING = "To do action press the number";
    private static final String FIND_OPTION_STRING = "Find some information";
    private static final String FIND_ALL_DRINKS_STRING = "Find all drinks";
    private static final String FIND_ALL_DESSERTS_STRING = "Find all desserts";
    private static final String FIND_ALL_BARISTAS_STRING = "Find all baristas";
    private static final String FIND_ALL_WAITERS_STRING = "Find all waiters";
    private static final String FIND_ALL_ORDERS_BY_MENU_STRING = "Find all orders of exact menu item";
    private static final String FIND_SCHEDULE_ON_DATE_STRING = "Find schedule on date";
    private static final String FIND_ALL_ORDERS_BY_WAITER_STRING = "Find all orders that exact waiter received";
    private static final String FIND_ALL_ORDERS_BY_CLIENT_STRING = "Find all orders of exact client";
    private static final String FIND_MIN_DISCOUNT_FOR_CLIENT_STRING = "Find minimum discount for a client";
    private static final String FIND_MAX_DISCOUNT_FOR_CLIENT_STRING = "Find maximum discount for a client";
    private static final String FIND_CLIENTS_WITH_MIN_DISCOUNT_STRING = "Find clients with minimum discount";
    private static final String FIND_CLIENTS_WITH_MAX_DISCOUNT_STRING = "Find clients with maximum discount";
    private static final String FIND_AVG_DISCOUNT_FOR_CLIENT_STRING = "Find average discount for a client";
    private static final String FIND_YOUNGEST_CLIENTS_STRING = "Find youngest clients";
    private static final String FIND_OLDEST_CLIENTS_STRING = "Find oldest clients";
    private static final String FIND_CLIENTS_WITH_BIRTHDAY_STRING = "Find clients with birthdays";
    private static final String FIND_CLIENTS_WITHOUT_EMAIL_STRING = "Find clients without email";
    private static final String FIND_ORDERS_ON_DATE_STRING = "Find orders on a specific date";
    private static final String FIND_ORDERS_BETWEEN_DATES_STRING = "Find orders between dates";
    private static final String FIND_ORDERS_COUNT_ON_DATE_BY_DESSERT_STRING = "Find order count on a specific date for desserts";
    private static final String FIND_ORDERS_COUNT_ON_DATE_BY_DRINK_STRING = "Find order count on a specific date for drinks";
    private static final String FIND_CLIENTS_AND_COOKS_BY_ORDER_TYPE_ON_DATE_STRING = "Find clients and cooks for orders of type on date";
    private static final String FIND_AVG_ORDER_PRICE_ON_DATE_STRING = "Find average order price on a specific date";
    private static final String FIND_MAX_ORDER_PRICE_ON_DATE_STRING = "Find maximum order price on a specific date";
    private static final String FIND_CLIENTS_WITH_MAX_ORDER_PRICE_ON_DATE_STRING = "Find clients with maximum order price on a specific date";
    private static final String FIND_SCHEDULE_OF_WORKER_BY_POSITION_ON_WEEK_STRING = "Find schedule of a worker by position for the week";
    private static final String FIND_SCHEDULE_OF_ALL_WORKERS_BY_POSITION_ON_WEEK_STRING = "Find schedule of all workers by position for the week";
    private static final String FIND_SCHEDULE_ON_WEEK_STRING = "Find schedule for the week";
    private static final String ADD_OPTION_STRING = "Add new entity to the database";
    private static final String ADD_TO_MENU_STRING = "Add new item to the menu";
    private static final String ADD_WORKER_STRING = "Add new worker";
    private static final String ADD_CLIENT_STRING = "Add new client";
    private static final String ADD_RECEIPT_STRING = "Add new receipt";
    private static final String ADD_SCHEDULE_ON_MONDAY_STRING = "Add schedule on monday";
    private static final String EDIT_OPTION_STRING = "Edit existing entity";
    private static final String EDIT_PRICE_ON_DRINK_STRING = "Change price on some drink";
    private static final String EDIT_PASTRY_CHEF_EMAIL_STRING = "Change pastry chef`s email";
    private static final String EDIT_BARISTA_PHONE_STRING = "Change barista`s phone";
    private static final String EDIT_CLIENT_DISCOUNT_STRING = "Change client`s discount";
    private static final String EDIT_SCHEDULE_ON_TUESDAY_STRING = "Change schedule on tuesday";
    private static final String EDIT_NAME_TO_DRINK_STRING = "Change name of drink";
    private static final String EDIT_ORDER_STRING = "Change order information";
    private static final String EDIT_NAME_TO_DESSERT_STRING = "Change name of dessert";
    private static final String DELETE_OPTION_STRING = "Delete existing entity";
    private static final String DELETE_DESSERT_STRING = "Delete some dessert from the menu";
    private static final String DELETE_WAITER_STRING = "Fire a waiter";
    private static final String DELETE_BARISTA_STRING = "Fire a barista";
    private static final String DELETE_CLIENT_STRING = "Delete information about client";
    private static final String DELETE_ORDER_STRING = "Delete information about order";
    private static final String DELETE_ORDERS_BY_MENU_ITEM_STRING = "Delete information about orders of exact menu item";
    private static final String DELETE_SCHEDULE_ON_DATE_STRING = "Delete information about schedule on date";
    private static final String DELETE_SCHEDULE_BETWEEN_DATES_STRING = "Delete information about schedule between two dates";
    private static final String INVATION_STRING = "Please enter the number";
    private static final String BACK_STRING = "Return to the previous menu";
    private static final String EXIT_STRING = "Close the program";
    private static final String SEPARATOR = "-";
    private static final String DOT_SPACE = ".  ";
    private static final String END_LINE = "\n";

    public static void showMenu() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(ACTION_STRING)
                .append(END_LINE)
                .append(showStringList(List.of(FIND_OPTION_STRING,
                        ADD_OPTION_STRING, EDIT_OPTION_STRING,
                        DELETE_OPTION_STRING)))
                .append(-1)
                .append(DOT_SPACE)
                .append(EXIT_STRING)
                .append(END_LINE)
                .append(SEPARATOR.repeat(60))
                .append(END_LINE)
                .append(INVATION_STRING);
        System.out.println(resultString);
    }

    public static void showFindMenu() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(ACTION_STRING)
                .append(END_LINE)
                .append(showStringList(List.of(FIND_ALL_DRINKS_STRING,
                        FIND_ALL_DESSERTS_STRING, FIND_ALL_BARISTAS_STRING,
                        FIND_ALL_WAITERS_STRING, FIND_ALL_ORDERS_BY_MENU_STRING,
                        FIND_SCHEDULE_ON_DATE_STRING, FIND_ALL_ORDERS_BY_WAITER_STRING,
                        FIND_ALL_ORDERS_BY_CLIENT_STRING, FIND_MIN_DISCOUNT_FOR_CLIENT_STRING,
                        FIND_MAX_DISCOUNT_FOR_CLIENT_STRING, FIND_CLIENTS_WITH_MIN_DISCOUNT_STRING,
                        FIND_CLIENTS_WITH_MAX_DISCOUNT_STRING, FIND_AVG_DISCOUNT_FOR_CLIENT_STRING,
                        FIND_YOUNGEST_CLIENTS_STRING, FIND_OLDEST_CLIENTS_STRING,
                        FIND_CLIENTS_WITH_BIRTHDAY_STRING, FIND_CLIENTS_WITHOUT_EMAIL_STRING,
                        FIND_ORDERS_ON_DATE_STRING, FIND_ORDERS_BETWEEN_DATES_STRING,
                        FIND_ORDERS_COUNT_ON_DATE_BY_DESSERT_STRING,
                        FIND_ORDERS_COUNT_ON_DATE_BY_DRINK_STRING,
                        FIND_CLIENTS_AND_COOKS_BY_ORDER_TYPE_ON_DATE_STRING,
                        FIND_AVG_ORDER_PRICE_ON_DATE_STRING, FIND_MAX_ORDER_PRICE_ON_DATE_STRING,
                        FIND_CLIENTS_WITH_MAX_ORDER_PRICE_ON_DATE_STRING,
                        FIND_SCHEDULE_OF_WORKER_BY_POSITION_ON_WEEK_STRING,
                        FIND_SCHEDULE_OF_ALL_WORKERS_BY_POSITION_ON_WEEK_STRING,
                        FIND_SCHEDULE_ON_WEEK_STRING)))
                .append(-1)
                .append(DOT_SPACE)
                .append(BACK_STRING)
                .append(END_LINE)
                .append(SEPARATOR.repeat(60))
                .append(END_LINE)
                .append(INVATION_STRING);
        System.out.println(resultString);
    }

    public static void showAddMenu() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(ACTION_STRING)
                .append(END_LINE)
                .append(showStringList(List.of(ADD_TO_MENU_STRING,
                        ADD_WORKER_STRING, ADD_CLIENT_STRING,
                        ADD_RECEIPT_STRING, ADD_SCHEDULE_ON_MONDAY_STRING)))
                .append(-1)
                .append(DOT_SPACE)
                .append(BACK_STRING)
                .append(END_LINE)
                .append(SEPARATOR.repeat(60))
                .append(END_LINE)
                .append(INVATION_STRING);
        System.out.println(resultString);
    }

    public static void showEditMenu() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(ACTION_STRING)
                .append(END_LINE)
                .append(showStringList(List.of(EDIT_PRICE_ON_DRINK_STRING,
                        EDIT_PASTRY_CHEF_EMAIL_STRING, EDIT_BARISTA_PHONE_STRING,
                        EDIT_CLIENT_DISCOUNT_STRING, EDIT_SCHEDULE_ON_TUESDAY_STRING,
                        EDIT_NAME_TO_DRINK_STRING, EDIT_ORDER_STRING,
                        EDIT_NAME_TO_DESSERT_STRING)))
                .append(-1)
                .append(DOT_SPACE)
                .append(BACK_STRING)
                .append(END_LINE)
                .append(SEPARATOR.repeat(60))
                .append(END_LINE)
                .append(INVATION_STRING);
        System.out.println(resultString);
    }

    public static void showDeleteMenu() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(ACTION_STRING)
                .append(END_LINE)
                .append(showStringList(List.of(DELETE_DESSERT_STRING,
                        DELETE_WAITER_STRING, DELETE_BARISTA_STRING,
                        DELETE_CLIENT_STRING, DELETE_ORDER_STRING,
                        DELETE_ORDERS_BY_MENU_ITEM_STRING, DELETE_SCHEDULE_ON_DATE_STRING,
                        DELETE_SCHEDULE_BETWEEN_DATES_STRING)))
                .append(-1)
                .append(DOT_SPACE)
                .append(BACK_STRING)
                .append(END_LINE)
                .append(SEPARATOR.repeat(60))
                .append(END_LINE)
                .append(INVATION_STRING);
        System.out.println(resultString);
    }

    public static String showStringList(List<String> sourceStringList) {
        int menuLine = 1;
        StringBuilder resultString = new StringBuilder();

        resultString.append(SEPARATOR.repeat(60))
                .append(END_LINE);

        for (var currentString : sourceStringList) {
            resultString.append(menuLine++)
                    .append(DOT_SPACE)
                    .append(currentString)
                    .append(END_LINE);
        }
        resultString.append(END_LINE);

        return resultString.toString();
    }

    private MenuPublisher() {
    }

}
