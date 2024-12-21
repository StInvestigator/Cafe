package org.example.service;

import org.example.dao.clientDAO.ClientDao;
import org.example.dao.menuItemDAO.MenuItemDao;
import org.example.dao.orderDAO.OrderDao;
import org.example.dao.receiptDAO.ReceiptDao;
import org.example.dao.scheduleDAO.ScheduleDao;
import org.example.dao.workerDAO.WorkerDao;
import org.example.exception.FileException;
import org.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class CafeDbInitializer {

    @Value("${data.names}")
    private String namesPath;

    @Value("${data.surnames}")
    private String surnamesPath;

    @Value("${data.emails}")
    private String emailsPath;

    @Value("${data.phone_numbers}")
    private String phonePath;

    @Value("${data.menu_desserts}")
    private String menu_dessertsPath;

    @Value("${data.menu_drinks}")
    private String menu_drinksPath;

    @Value("${data.create_tables}")
    private String create_tablesPath;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private MenuItemDao menuItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ReceiptDao receiptDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private  WorkerDao workerDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Random RANDOM_GENERATOR = new Random();

    public void createTables() {
        List<String> tableStrings;
        try {
            tableStrings = Files.readAllLines(Paths.get(create_tablesPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder createTablesQuery = new StringBuilder();

        for (var currentString : tableStrings) {
            createTablesQuery.append(currentString);
            createTablesQuery.append("\n");
        }

        String resStr = createTablesQuery.toString();

        jdbcTemplate.execute(resStr);
    }

    public void deleteAllRowsInDB() {
        clientDao.deleteAll();
        menuItemDao.deleteAll();
        orderDao.deleteAll();
        receiptDao.deleteAll();
        scheduleDao.deleteAll();
        workerDao.deleteAll();
    }

    public Date generateRandomBirthday(int minAge, int maxAge) {
        LocalDate currentDate = LocalDate.now();
        Random random = new Random();

        int age = random.nextInt(maxAge - minAge + 1) + minAge;
        LocalDate birthday = currentDate.minusYears(age);
        int dayOfYear = random.nextInt(birthday.lengthOfYear()) + 1;
        LocalDate randomDate = birthday.withDayOfYear(dayOfYear);

        return Date.valueOf(randomDate);
    }

    public void createRandomClients() {
        List<String> randomNames;
        List<String> randomSurnames;
        List<String> randomPhones;
        List<String> randomEmails;
        try {
            randomNames = Files.readAllLines(Paths.get(namesPath));
            randomSurnames = Files.readAllLines(Paths.get(surnamesPath));
            randomPhones = Files.readAllLines(Paths.get(phonePath));
            randomEmails = Files.readAllLines(Paths.get(emailsPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public void createMenu() {
        List<String> randomDrinks;
        List<String> randomDesserts;
        try {
            randomDrinks = Files.readAllLines(Paths.get(menu_drinksPath));
            randomDesserts = Files.readAllLines(Paths.get(menu_dessertsPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public void createRandomWorkers() throws FileException {
        List<String> randomNames;
        List<String> randomSurnames;
        List<String> randomPhones;
        List<String> randomEmails;
        try {
            randomNames = Files.readAllLines(Paths.get(namesPath));
            randomSurnames = Files.readAllLines(Paths.get(surnamesPath));
            randomPhones = Files.readAllLines(Paths.get(phonePath));
            randomEmails = Files.readAllLines(Paths.get(emailsPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public void createSchedule() {
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

    public List<Schedule> generateShiftForWorkers(List<Worker> workers, Date date, int maxWorkersPerDay) {
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

    public void createRandomReceipts() {
        List<Client> clients = clientDao.findAll();

        List<Receipt> receipts = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Receipt receipt = new Receipt();
            receipt.setClientId(clients.get(RANDOM_GENERATOR.nextInt(clients.size())).getId());
            receipt.setDate(new java.sql.Date(new java.util.Date().getTime()));
            receipts.add(receipt);
        }
        receiptDao.saveMany(receipts);
    }

    public void createRandomOrders() {
        List<Receipt> receipts = receiptDao.findAll();
        List<MenuItem> menuItems = menuItemDao.findAll();
        List<Worker> baristas = workerDao.findAllWorkersWithPosition("Barista");
        List<Worker> pastryChefs = workerDao.findAllWorkersWithPosition("Pastry Chef");
        List<Worker> waiters = workerDao.findAllWorkersWithPosition("Waiter");

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
            order.setWaiterId(waiters.get(RANDOM_GENERATOR.nextInt(waiters.size())).getId());
            receipt.setTotalPrice(receipt.getTotalPrice() + menuPosition.getPrice());
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
                order.setWaiterId(waiters.get(RANDOM_GENERATOR.nextInt(waiters.size())).getId());
                receipt.setTotalPrice(receipt.getTotalPrice() + menuPosition.getPrice());
                orders.add(order);
            }
            ordersCount -= ordersToAdd;
        }
        orderDao.saveMany(orders);
        for (Receipt receipt : receipts) {
            receiptDao.update(receipt);
        }
    }
}
