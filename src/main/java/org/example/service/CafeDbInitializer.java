package org.example.service;

import org.example.dao.ConnectionFactory;
import org.example.dao.clientDAO.ClientDao;
import org.example.dao.clientDAO.ClientDaoImpl;
import org.example.dao.menuItemDAO.MenuItemDao;
import org.example.dao.menuItemDAO.MenuItemDaoImpl;
import org.example.dao.orderDAO.OrderDao;
import org.example.dao.orderDAO.OrderDaoImpl;
import org.example.dao.receiptDAO.ReceiptDao;
import org.example.dao.receiptDAO.ReceiptDaoImpl;
import org.example.dao.scheduleDAO.ScheduleDao;
import org.example.dao.scheduleDAO.ScheduleDaoImpl;
import org.example.dao.workerDAO.WorkerDao;
import org.example.dao.workerDAO.WorkerDaoImpl;
import org.example.exception.ConnectionDBException;
import org.example.exception.FileException;
import org.example.model.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CafeDbInitializer {

    private static final Random RANDOM_GENERATOR = new Random();
    private static final List<String> TABLES_NAME_ARRAY;
    private static final String SQL_SCRIPT_CREATE_TABLES;

    static {
        SQL_SCRIPT_CREATE_TABLES = PropertyFactory.getInstance().getProperty().getProperty("db.sqlScriptCreateTables");

        String tablesNames = PropertyFactory.getInstance().getProperty().getProperty("db.tablesNames");
        TABLES_NAME_ARRAY = Arrays.stream(tablesNames.split(",")).collect(Collectors.toList());
    }

    public static void createTables() {
        try (Connection conn = ConnectionFactory.getInstance().makeConnection()){


            for (var tableName : TABLES_NAME_ARRAY) {
                if (!tableExists(tableName)) {

                    try (Stream<String> lineStream = Files.lines(Paths.get(SQL_SCRIPT_CREATE_TABLES))) {
                        StringBuilder createTablesQuery = new StringBuilder();

                        for (var currentString : lineStream.collect(Collectors.toList())) {
                            createTablesQuery.append(currentString).append(" ");
                        }

                        try (PreparedStatement ps = conn.prepareStatement(createTablesQuery.toString())) {
                            ps.execute();
                        }

                    } catch (IOException exception) {
                        throw new FileException("Error with createTables.sql");
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }

        } catch (ConnectionDBException | FileException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void deleteAllRowsInDB() {
        ClientDao clientDao = new ClientDaoImpl();
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        OrderDao orderDao = new OrderDaoImpl();
        ReceiptDao receiptDao = new ReceiptDaoImpl();
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        WorkerDao workerDao = new WorkerDaoImpl();

        clientDao.deleteAll();
        menuItemDao.deleteAll();
        orderDao.deleteAll();
        receiptDao.deleteAll();
        scheduleDao.deleteAll();
        workerDao.deleteAll();
    }

    public static Date generateRandomBirthday(int minAge, int maxAge) {
        LocalDate currentDate = LocalDate.now();
        Random random = new Random();

        int age = random.nextInt(maxAge - minAge + 1) + minAge;
        LocalDate birthday = currentDate.minusYears(age);
        int dayOfYear = random.nextInt(birthday.lengthOfYear()) + 1;
        LocalDate randomDate = birthday.withDayOfYear(dayOfYear);

        return Date.valueOf(randomDate);
    }

    public static void createRandomClients() throws FileException {
        ClientDao clientDao = new ClientDaoImpl();

        TxtFileReader txtFileReaderNames = new TxtFileReader("data.names");
        List<String> randomNames = txtFileReaderNames.readFile();
        TxtFileReader txtFileReaderSurnames = new TxtFileReader("data.surnames");
        List<String> randomSurnames = txtFileReaderSurnames.readFile();
        TxtFileReader txtFileReaderPhones = new TxtFileReader("data.phone_numbers");
        List<String> randomPhones = txtFileReaderPhones.readFile();
        TxtFileReader txtFileReaderEmails = new TxtFileReader("data.emails");
        List<String> randomEmails = txtFileReaderEmails.readFile();

        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Client client = new Client();

            client.setName(randomNames.get(RANDOM_GENERATOR.nextInt(randomNames.size())));
            client.setSurname(randomSurnames.get(RANDOM_GENERATOR.nextInt(randomSurnames.size())));
            client.setPhone(randomPhones.get(RANDOM_GENERATOR.nextInt(randomPhones.size())));
            client.setEmail(randomEmails.get(RANDOM_GENERATOR.nextInt(randomEmails.size())));
            client.setDiscount(RANDOM_GENERATOR.nextInt(0, 2001) / 100f);
            client.setBirthday(generateRandomBirthday(10, 100));

            clients.add(client);
        }
        clientDao.saveMany(clients);
    }

    public static void createMenu() throws FileException {
        MenuItemDao menuItemDao = new MenuItemDaoImpl();

        TxtFileReader txtFileReaderDrinks = new TxtFileReader("data.menu_desserts");
        List<String> randomDrinks = txtFileReaderDrinks.readFile();
        TxtFileReader txtFileReaderDesserts = new TxtFileReader("data.menu_drinks");
        List<String> randomDesserts = txtFileReaderDesserts.readFile();

        List<MenuItem> menuItems = new ArrayList<>();
        for (String randomDrink : randomDrinks) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName(randomDrink);
            menuItem.setType("Drink");
            menuItem.setPrice(RANDOM_GENERATOR.nextInt(5000, 20000) / 100f);

            menuItems.add(menuItem);
        }
        for (String randomDessert : randomDesserts) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName(randomDessert);
            menuItem.setType("Dessert");
            menuItem.setPrice(RANDOM_GENERATOR.nextInt(5000, 20000) / 100f);

            menuItems.add(menuItem);
        }
        menuItemDao.saveMany(menuItems);
    }

    public static void createRandomWorkers() throws FileException {
        WorkerDao workerDao = new WorkerDaoImpl();

        TxtFileReader txtFileReaderNames = new TxtFileReader("data.names");
        List<String> randomNames = txtFileReaderNames.readFile();
        TxtFileReader txtFileReaderSurnames = new TxtFileReader("data.surnames");
        List<String> randomSurnames = txtFileReaderSurnames.readFile();
        TxtFileReader txtFileReaderPhones = new TxtFileReader("data.phone_numbers");
        List<String> randomPhones = txtFileReaderPhones.readFile();
        TxtFileReader txtFileReaderEmails = new TxtFileReader("data.emails");
        List<String> randomEmails = txtFileReaderEmails.readFile();

        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            Worker worker = new Worker();
            worker.setName(randomNames.get(RANDOM_GENERATOR.nextInt(randomNames.size())));
            worker.setSurname(randomSurnames.get(RANDOM_GENERATOR.nextInt(randomSurnames.size())));
            worker.setPhone(randomPhones.get(RANDOM_GENERATOR.nextInt(randomPhones.size())));
            worker.setEmail(randomEmails.get(RANDOM_GENERATOR.nextInt(randomEmails.size())));
            worker.setPosition(i < 15 ? "Barista" : (i < 30 ? "Waiter" : "Pastry Chef"));

            workers.add(worker);
        }
        workerDao.saveMany(workers);
    }

    public static void createSchedule() {
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        WorkerDao workerDao = new WorkerDaoImpl();
        List<Worker> baristas = workerDao.findAllWorkersWithPosition("Barista");
        List<Worker> waiters = workerDao.findAllWorkersWithPosition("Waiter");
        List<Worker> pastryChefs = workerDao.findAllWorkersWithPosition("Pastry Chef");

        List<Schedule> scheduleList = new ArrayList<>();

        java.util.Date today = new java.util.Date();
        java.sql.Date startDate = new java.sql.Date(today.getTime());

        for (int dayOffset = 0; dayOffset < 3; dayOffset++) {
            java.sql.Date currentDate = new java.sql.Date(startDate.getTime() + (dayOffset * 86400000L));
            scheduleList.addAll(generateShiftForWorkers(baristas, currentDate, 6));
            scheduleList.addAll(generateShiftForWorkers(waiters, currentDate, 6));
            scheduleList.addAll(generateShiftForWorkers(pastryChefs, currentDate, 6));
        }
        scheduleDao.saveMany(scheduleList);

    }

    public static List<Schedule> generateShiftForWorkers(List<Worker> workers, Date date, int maxWorkersPerDay) {
        List<Schedule> shifts = new ArrayList<>();
        List<Worker> selectedWorkers = new ArrayList<>();
        while (selectedWorkers.size() < maxWorkersPerDay) {
            Worker selectedWorker = workers.get(RANDOM_GENERATOR.nextInt(workers.size()));
            if (!selectedWorkers.contains(selectedWorker)) {
                selectedWorkers.add(selectedWorker);
            }
        }
        Time[] shiftStartTimes = {Time.valueOf("09:00:00"), Time.valueOf("14:00:00"), Time.valueOf("19:00:00")};
        Time[] shiftEndTimes = {Time.valueOf("13:00:00"), Time.valueOf("18:00:00"), Time.valueOf("23:00:00")};

        Collections.shuffle(selectedWorkers);

        for (int i = 0; i < selectedWorkers.size(); i++) {
            int shiftIndex = i % 3;

            Schedule schedule = new Schedule();
            schedule.setWorkerId(selectedWorkers.get(i).getId());
            schedule.setDate(date);
            schedule.setStartTime(shiftStartTimes[shiftIndex]);
            schedule.setEndTime(shiftEndTimes[shiftIndex]);
            shifts.add(schedule);
        }

        return shifts;
    }

    public static void createRandomReceipts() {
        ReceiptDao receiptDao = new ReceiptDaoImpl();
        ClientDao clientDao = new ClientDaoImpl();
        List<Client> clients = clientDao.findAll();

        List<Receipt> receipts = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Receipt receipt = new Receipt();
            receipt.setClientId(clients.get(RANDOM_GENERATOR.nextInt(clients.size())).getId());
            receipts.add(receipt);
        }
        receiptDao.saveMany(receipts);
    }

    public static void createRandomOrders() {
        OrderDao orderDao = new OrderDaoImpl();
        ReceiptDao receiptDao = new ReceiptDaoImpl();
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        WorkerDao workerDao = new WorkerDaoImpl();
        List<Receipt> receipts = receiptDao.findAll();
        List<MenuItem> menuItems = menuItemDao.findAll();
        List<Worker> baristas = workerDao.findAllWorkersWithPosition("Barista");
        List<Worker> pastryChefs = workerDao.findAllWorkersWithPosition("Waiter");

        List<Order> orders = new ArrayList<>();
        int ordersCount = Math.round(receipts.size() * RANDOM_GENERATOR.nextFloat(1, 3));
        for (Receipt receipt : receipts) {
            Order order = new Order();
            MenuItem menuPosition = menuItems.get(RANDOM_GENERATOR.nextInt(menuItems.size()));
            order.setPrice(menuPosition.getPrice());
            order.setMenuPositionId(menuPosition.getId());
            order.setReceiptId(receipt.getId());
            if (menuPosition.getType().equals("Dessert")) {
                order.setWorkerId(pastryChefs.get(RANDOM_GENERATOR.nextInt(pastryChefs.size())).getId());
            } else {
                order.setWorkerId(baristas.get(RANDOM_GENERATOR.nextInt(baristas.size())).getId());
            }
            receipt.setTotalPrice(receipt.getTotalPrice()+menuPosition.getPrice());
            orders.add(order);
        }
        ordersCount -= receipts.size();
        while (ordersCount > 0) {
            int ordersToAdd = RANDOM_GENERATOR.nextInt(4);
            for (int i = 0; i < ordersToAdd; i++) {
                Order order = new Order();
                Receipt receipt = receipts.get(RANDOM_GENERATOR.nextInt(receipts.size()));
                MenuItem menuPosition = menuItems.get(RANDOM_GENERATOR.nextInt(menuItems.size()));
                order.setPrice(menuPosition.getPrice());
                order.setMenuPositionId(menuPosition.getId());
                order.setReceiptId(receipt.getId());
                if (menuPosition.getType().equals("Dessert")) {
                    order.setWorkerId(pastryChefs.get(RANDOM_GENERATOR.nextInt(pastryChefs.size())).getId());
                } else {
                    order.setWorkerId(baristas.get(RANDOM_GENERATOR.nextInt(baristas.size())).getId());
                }
                receipt.setTotalPrice(receipt.getTotalPrice()+menuPosition.getPrice());
                orders.add(order);
            }
            ordersCount -= ordersToAdd;
        }
        orderDao.saveMany(orders);
        for (Receipt receipt : receipts) {
            receiptDao.update(receipt);
        }
    }

    private static boolean tableExists(String tableName) throws ConnectionDBException {
        try (Connection connection = ConnectionFactory.getInstance().makeConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});
            return resultSet.next();
        } catch (SQLException exception) {
            throw new ConnectionDBException("error connection to DB");
        }
    }

    private CafeDbInitializer() {
    }

}
