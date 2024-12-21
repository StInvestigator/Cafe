package org.example.menu;


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
import org.example.model.*;
import org.example.service.business.OrderService;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Scanner;

import static org.example.menu.MenuPublisher.*;


public class MenuExecutor {
    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            showMenu();
            choice = scanner.nextInt();

            if (choice == 1) {
                startFindMenu();
            }
            if (choice == 2) {
                startAddMenu();
            }
            if (choice == 3) {
                startEditMenu();
            }
            if (choice == 4) {
                startDeleteMenu();
            }
        } while (choice != -1);
    }

    private static void startFindMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            showFindMenu();
            choice = scanner.nextInt();

            if (choice == 1) {
                printAllMenuItemsByType("Drink");
            }
            if (choice == 2) {
                printAllMenuItemsByType("Dessert");
            }
            if (choice == 3) {
                printAllWorkersByPosition("Barista");
            }
            if (choice == 4) {
                printAllWorkersByPosition("Waiter");
            }
            if (choice == 5) {
                printAllOrdersByMenuItem();
            }
            if (choice == 6) {
                printScheduleOnDate();
            }
            if (choice == 7) {
                printAllOrdersByWaiter();
            }
            if (choice == 8) {
                printAllOrdersByClient();
            }
            if (choice == 9) {
                findMinDiscountForClient();
            }
            if (choice == 10) {
                findMaxDiscountForClient();
            }
            if (choice == 11) {
                findClientsWithMinDiscount();
            }
            if (choice == 12) {
                findClientsWithMaxDiscount();
            }
            if (choice == 13) {
                findAvgDiscountForClient();
            }
            if (choice == 14) {
                findYoungestClients();
            }
            if (choice == 15) {
                findOldestClients();
            }
            if (choice == 16) {
                findClientsWithBirthday();
            }
            if (choice == 17) {
                findClientsWithoutEmail();
            }
            if (choice == 18) {
                findOrdersOnDate();
            }
            if (choice == 19) {
                findOrdersBetweenDates();
            }
            if (choice == 20) {
                findOrdersCountOnDateByType("Dessert");
            }
            if (choice == 21) {
                findOrdersCountOnDateByType("Drink");
            }
            if (choice == 22) {
                findClientsAndCooksThatDidOrdersOfTypeOnDate("Drink");
            }
            if (choice == 23) {
                findAvgOrderPriceOnDate();
            }
            if (choice == 24) {
                findMaxOrderPriceOnDate();
            }
            if (choice == 25) {
                findClientsWithMaxOrderPriceOnDate();
            }
            if (choice == 26) {
                findScheduleOfWorkerByPositionOnWeek("Barista");
            }
            if (choice == 27) {
                findScheduleOfAllWorkersByPositionOnWeek("Barista");
            }
            if (choice == 28) {
                findScheduleOnWeek();
            }
        } while (choice != -1);
    }

    private static void findScheduleOfWorkerByPositionOnWeek(String position) {
        WorkerDao workerDao = new WorkerDaoImpl();
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        List<Worker> workers = workerDao.findAllWorkersWithPosition(position);
        int count = 1;
        for (Worker worker : workers) {
            System.out.println(count++ + ". " + "Worker`s name: " + worker.getName() + " " + worker.getSurname());
        }
        System.out.print("Choose worker number: ");
        Scanner scanner = new Scanner(System.in);
        long workerId = workers.get(scanner.nextInt() - 1).getId();
        Date now = Date.valueOf(LocalDate.now());
        Date weekAfter = Date.valueOf(LocalDate.now().plusWeeks(1));
        for (Schedule schedule : scheduleDao.findAll().stream()
                .filter(x -> x.getDate().getTime() >= now.getTime() &&
                        x.getDate().getTime() <= weekAfter.getTime()
                        && x.getWorkerId() == workerId)
                .toList()) {
            System.out.println(schedule);
        }
    }

    private static void findScheduleOfAllWorkersByPositionOnWeek(String position) {
        WorkerDao workerDao = new WorkerDaoImpl();
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        List<Long> workerIds = workerDao.findAllWorkersWithPosition(position).stream().map(Worker::getId).toList();
        Date now = Date.valueOf(LocalDate.now());
        Date weekAfter = Date.valueOf(LocalDate.now().plusWeeks(1));
        for (Schedule schedule : scheduleDao.findAll().stream()
                .filter(x -> x.getDate().getTime() >= now.getTime() &&
                        x.getDate().getTime() <= weekAfter.getTime()
                        && workerIds.contains(x.getWorkerId()))
                .toList()) {
            System.out.println(schedule);
        }
    }

    private static void findScheduleOnWeek() {
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        Date now = Date.valueOf(LocalDate.now());
        Date weekAfter = Date.valueOf(LocalDate.now().plusWeeks(1));
        for (Schedule schedule : scheduleDao.findAll().stream()
                .filter(x -> x.getDate().getTime() >= now.getTime() &&
                        x.getDate().getTime() <= weekAfter.getTime())
                .toList()) {
            System.out.println(schedule);
        }
    }

    private static void findClientsAndCooksThatDidOrdersOfTypeOnDate(String type) {
        ClientDao clientDao = new ClientDaoImpl();
        WorkerDao workerDao = new WorkerDaoImpl();
        Date date = getDateFromUser();
        System.out.println("Clients: ");
        clientDao.findClientsThatOrderedMenuTypeOnDate(type, date).forEach(System.out::println);
        System.out.println("Cooks: ");
        workerDao.findWorkersThatDidMenuTypeOnDate(type, date).forEach(System.out::println);
    }

    private static void findAvgOrderPriceOnDate() {
        OrderDao orderDao = new OrderDaoImpl();
        float price = orderDao.findAvgPriceOnDate(getDateFromUser());
        System.out.println("Avg Order Price: " + price);
    }

    private static void findMaxOrderPriceOnDate() {
        OrderDao orderDao = new OrderDaoImpl();
        float price = orderDao.findMaxPriceOnDate(getDateFromUser());
        System.out.println("Max Order Price: " + price);
    }

    private static void findClientsWithMaxOrderPriceOnDate() {
        ClientDao clientDao = new ClientDaoImpl();
        clientDao.findClientsThatOrderedMaxPriceOnDate(getDateFromUser()).forEach(System.out::println);
    }

    private static void findOrdersOnDate() {
        OrderDao orderDao = new OrderDaoImpl();
        orderDao.findOrdersByDate(getDateFromUser())
                .forEach(x -> OrderService.OrderToString(x, new MenuItemDaoImpl(), new WorkerDaoImpl()));
    }

    private static void findOrdersBetweenDates() {
        OrderDao orderDao = new OrderDaoImpl();
        Scanner scanner = new Scanner(System.in);
        LocalDate dateStart = null;
        LocalDate dateEnd = null;
        boolean validDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!validDate) {
            try {
                System.out.print("Enter a starting date (yyyy-MM-dd): ");
                dateStart = LocalDate.parse(scanner.nextLine(), formatter);
                System.out.print("Enter a ending date (yyyy-MM-dd): ");
                dateEnd = LocalDate.parse(scanner.nextLine(), formatter);
                if (dateEnd.isAfter(dateStart)) {
                    validDate = true;
                } else {
                    throw new DateTimeParseException("Wrong input", dateStart.toString(), 0);
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        orderDao.findOrdersBetweenDates(Date.valueOf(dateStart), Date.valueOf(dateEnd))
                .forEach(x -> OrderService.OrderToString(x, new MenuItemDaoImpl(), new WorkerDaoImpl()));
    }

    private static void findOrdersCountOnDateByType(String type) {
        OrderDao orderDao = new OrderDaoImpl();
        int count = orderDao.findOrdersOnDateByMenuItemType(getDateFromUser(), type).size();
        System.out.println("Orders count: " + count);
    }

    private static void findYoungestClients() {
        ClientDao clientDao = new ClientDaoImpl();
        clientDao.findYoungestClients().forEach(System.out::println);
    }

    private static void findOldestClients() {
        ClientDao clientDao = new ClientDaoImpl();
        clientDao.findOldestClients().forEach(System.out::println);
    }

    private static void findClientsWithBirthday() {
        ClientDao clientDao = new ClientDaoImpl();
        clientDao.findAll().stream().filter(x -> x.getBirthday().equals(getDateFromUser()))
                .forEach(System.out::println);
    }

    private static void findClientsWithoutEmail() {
        ClientDao clientDao = new ClientDaoImpl();
        clientDao.findAll().stream().filter(x -> x.getEmail().isBlank())
                .forEach(System.out::println);
    }

    private static void findMinDiscountForClient() {
        ClientDao clientDao = new ClientDaoImpl();
        System.out.println("Min discount: " + clientDao.findWithMinDiscount().get(0).getDiscount());
    }

    private static void findMaxDiscountForClient() {
        ClientDao clientDao = new ClientDaoImpl();
        System.out.println("Max discount: " + clientDao.findWithMaxDiscount().get(0).getDiscount());
    }

    private static void findClientsWithMinDiscount() {
        ClientDao clientDao = new ClientDaoImpl();
        clientDao.findWithMinDiscount().forEach(System.out::println);
    }

    private static void findClientsWithMaxDiscount() {
        ClientDao clientDao = new ClientDaoImpl();
        clientDao.findWithMaxDiscount().forEach(System.out::println);
    }

    private static void findAvgDiscountForClient() {
        ClientDao clientDao = new ClientDaoImpl();
        System.out.println("Avg discount: " + clientDao.findAvgDiscount());
    }

    private static void printAllMenuItemsByType(String type) {
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        System.out.println("Result:");
        menuItemDao.findAllWithType(type).forEach(System.out::println);
        System.out.println("-".repeat(60));
    }

    private static void printAllWorkersByPosition(String position) {
        WorkerDao workerDao = new WorkerDaoImpl();
        System.out.println("Result:");
        workerDao.findAllWorkersWithPosition(position).forEach(System.out::println);
        System.out.println("-".repeat(60));
    }

    private static void printAllOrdersByMenuItem() {
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        List<MenuItem> menuItems = menuItemDao.findAll();
        int counter = 1;
        for (MenuItem menuItem : menuItems) {
            System.out.println(counter++ + ". Item`s Name: " + menuItem.getName() + ", Price: " + menuItem.getPrice());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the menu item number: ");
        MenuItem menuItem = menuItems.get(scanner.nextInt() - 1);

        OrderDao orderDao = new OrderDaoImpl();
        for (Order order : orderDao.findAll().stream().filter(x -> x.getMenuPositionId() == menuItem.getId()).toList()) {
            System.out.println(OrderService.OrderToString(order, new MenuItemDaoImpl(), new WorkerDaoImpl()));
        }
    }

    private static void printAllOrdersByWaiter() {
        WorkerDao workerDao = new WorkerDaoImpl();
        List<Worker> workers = workerDao.findAllWorkersWithPosition("Waiter");
        int counter = 1;
        for (Worker worker : workers) {
            System.out.println(counter++ + ". Worker`s Name: " + worker.getName() + " " + worker.getSurname());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the worker number: ");
        Worker worker = workers.get(scanner.nextInt() - 1);

        OrderDao orderDao = new OrderDaoImpl();
        for (Order order : orderDao.findAll().stream().filter(x -> x.getWaiterId() == worker.getId()).toList()) {
            System.out.println(OrderService.OrderToString(order, new MenuItemDaoImpl(), new WorkerDaoImpl()));
        }
    }

    private static void printAllOrdersByClient() {
        ClientDao clientDao = new ClientDaoImpl();
        List<Client> clients = clientDao.findAll();
        ReceiptDao receiptDao = new ReceiptDaoImpl();

        int counter = 1;
        for (Client client : clients) {
            System.out.println(counter++ + ". Client`s Name: " + client.getName() + " " + client.getSurname());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the client number: ");
        Client client = clients.get(scanner.nextInt() - 1);
        OrderDao orderDao = new OrderDaoImpl();
        List<Order> orders = orderDao.findOrdersByClient(client);
        for (Order order : orders) {
            System.out.println(OrderService.OrderToString(order, new MenuItemDaoImpl(), new WorkerDaoImpl()));
        }
    }

    private static void printScheduleOnDate() {
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        List<Schedule> schedules = scheduleDao.findAll();

        Date date = getDateFromUser();
        for (Schedule schedule : schedules.stream().filter(x -> x.getDate().equals(date)).toList()) {
            System.out.println(schedule);
        }
    }

    private static void startAddMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            showAddMenu();
            choice = scanner.nextInt();

            if (choice == 1) {
                addToMenu();
            }
            if (choice == 2) {
                addWorker();
            }
            if (choice == 3) {
                addClient();
            }
            if (choice == 4) {
                addReceipt();
            }
            if (choice == 5) {
                LocalDate today = LocalDate.now();
                addScheduleForDay(today.with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
            }
        } while (choice != -1);
    }

    private static void addScheduleForDay(LocalDate day) {
        Scanner scanner = new Scanner(System.in);
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        WorkerDao workerDao = new WorkerDaoImpl();

        List<Worker> workers = workerDao.findAll();

        System.out.println("Assign schedule for: " + day);
        for (Worker worker : workers) {
            System.out.println("Worker: " + worker.getName() + " " + worker.getSurname());
            System.out.print("Enter start time (HH:mm) or leave empty for a day off: ");
            String startTimeInput = scanner.nextLine();

            if (startTimeInput.isEmpty()) {
                continue;
            }

            System.out.print("Enter end time (HH:mm): ");
            String endTimeInput = scanner.nextLine();

            Schedule schedule = new Schedule();
            schedule.setWorkerId(worker.getId());
            schedule.setDate(Date.valueOf(day));
            schedule.setStartTime(Time.valueOf(startTimeInput + ":00"));
            schedule.setEndTime(Time.valueOf(endTimeInput + ":00"));

            scheduleDao.save(schedule);
        }
    }

    private static void addReceipt() {
        ClientDao clientDao = new ClientDaoImpl();
        List<Client> clients = clientDao.findAll();
        ReceiptDao receiptDao = new ReceiptDaoImpl();
        Receipt receipt = new Receipt();

        Scanner scanner = new Scanner(System.in);
        int counter = 1;
        for (Client client : clients) {
            System.out.println(counter++ + ". Client`s Name: " + client.getName() + " " + client.getSurname());
        }
        System.out.print("Choose the client number: ");
        receipt.setClientId(clients.get(scanner.nextInt() - 1).getId());
        var receipts = receiptDao.findAll();
        if (receipts.isEmpty()) {
            receipt.setId(1L);
        } else receipt.setId(receipts.stream().map(Receipt::getId).max(Long::compareTo).get() + 1);
        char answC;
        do {
            System.out.println("Which type of order does client want to do?");
            System.out.println("1. Drink");
            System.out.println("2. Dessert");
            addOrder(scanner.nextInt() == 1 ? "Drink" : "Dessert", receipt);
            System.out.println("Do you want to continue adding orders to the receipt? (y/n)");
            answC = scanner.next().charAt(0);
        } while (Character.toLowerCase(answC) != 'n');
        receipt.setDate(new Date(new java.util.Date().getTime()));
        receiptDao.save(receipt);
    }

    private static void addOrder(String type, Receipt receipt) {
        WorkerDao workerDao = new WorkerDaoImpl();
        List<Worker> waiters = workerDao.findAllWorkersWithPosition("Waiter");
        List<Worker> cooks = workerDao.findAllWorkersWithPosition(type.equals("Drink") ? "Barista" : "Pastry Chef");
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        List<MenuItem> items = menuItemDao.findAllWithType(type);
        OrderDao orderDao = new OrderDaoImpl();

        Order order = new Order();

        Scanner scanner = new Scanner(System.in);
        int counter = 1;
        for (Worker worker : waiters) {
            System.out.println(counter++ + ". Workers`s Name: " + worker.getName() + " " + worker.getSurname());
        }
        System.out.print("Choose the waiter number: ");
        order.setWaiterId(waiters.get(scanner.nextInt() - 1).getId());

        counter = 1;
        for (Worker worker : cooks) {
            System.out.println(counter++ + ". Workers`s Name: " + worker.getName() + " " + worker.getSurname());
        }
        System.out.print("Choose the cooks number: ");
        order.setWorkerId(cooks.get(scanner.nextInt() - 1).getId());

        counter = 1;
        for (MenuItem item : items) {
            System.out.println(counter++ + ". Position`s Name: " + item.getName() + ", Price " + item.getPrice());
        }
        System.out.print("Choose the position from menu number: ");
        MenuItem menuItem = items.get(scanner.nextInt() - 1);
        order.setMenuPositionId(menuItem.getId());

        order.setPrice(menuItem.getPrice());
        order.setReceiptId(receipt.getId());
        receipt.setTotalPrice(receipt.getTotalPrice() + order.getPrice());
        orderDao.save(order);
    }

    private static void addToMenu() {
        Scanner scanner = new Scanner(System.in);
        MenuItem menuItem = new MenuItem();

        System.out.print("Enter position name: ");
        menuItem.setName(scanner.nextLine());

        System.out.print("Enter price: ");
        menuItem.setPrice(scanner.nextFloat());

        System.out.println("Choose the type of the position: ");
        System.out.println("1. Drink");
        System.out.println("2. Dessert");

        int choice = scanner.nextInt();
        menuItem.setType(choice == 1 ? "Drink" : "Dessert");

        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        menuItemDao.save(menuItem);
    }

    private static void addWorker() {
        Scanner scanner = new Scanner(System.in);
        Worker worker = new Worker();

        System.out.print("Enter the name of the worker: ");
        worker.setName(scanner.nextLine());

        System.out.print("Enter the surname of the worker: ");
        worker.setSurname(scanner.nextLine());

        System.out.print("Enter the phone number of the worker: ");
        worker.setPhone(scanner.nextLine());

        System.out.print("Enter the email of the worker: ");
        worker.setEmail(scanner.nextLine());

        System.out.println("Choose the position: ");
        System.out.println("1. Barista");
        System.out.println("2. Waiter");
        System.out.println("3. Pastry Chef");

        int choice = scanner.nextInt();
        worker.setPosition(choice == 1 ? "Barista" : choice == 2 ? "Waiter" : "Pastry Chef");

        WorkerDao workerDao = new WorkerDaoImpl();
        workerDao.save(worker);
    }

    private static void addClient() {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();

        System.out.print("Enter the name of the client: ");
        client.setName(scanner.nextLine());

        System.out.print("Enter the surname of the client: ");
        client.setSurname(scanner.nextLine());

        System.out.print("Enter the phone number of the client: ");
        client.setPhone(scanner.nextLine());

        System.out.print("Enter the email of the client: ");
        client.setEmail(scanner.nextLine());

        System.out.print("Enter the discount of the client: ");
        client.setDiscount(scanner.nextFloat());

        System.out.print("Enter the birthday date of the client in format yyyy-MM-dd: ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            client.setBirthday(new Date(format.parse(scanner.nextLine()).getTime()));
        } catch (ParseException e) {
            System.out.println("Invalid date format. This information will be skipped");
        }

        ClientDao clientDao = new ClientDaoImpl();
        clientDao.save(client);
    }

    private static void startEditMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            showEditMenu();
            choice = scanner.nextInt();

            if (choice == 1) {
                editPriceToMenuItemWithType("Drink");
            }
            if (choice == 2) {
                editEmailToWorkerWithPosition("Pastry Chef");
            }
            if (choice == 3) {
                editPhoneToWorkerWithPosition("Barista");
            }
            if (choice == 4) {
                editClientDiscount();
            }
            if (choice == 5) {
                LocalDate today = LocalDate.now();
                editScheduleForDay(today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)));
            }
            if (choice == 6) {
                editNameToMenuItemWithType("Drink");
            }
            if (choice == 7) {
                editOrderInfo();
            }
            if (choice == 8) {
                editNameToMenuItemWithType("Dessert");
            }
        } while (choice != -1);
    }

    private static void editOrderInfo() {
        OrderDao orderDao = new OrderDaoImpl();
        List<Order> orders = orderDao.findAll();
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        List<MenuItem> items = menuItemDao.findAll();
        WorkerDao workerDao = new WorkerDaoImpl();
        ReceiptDao receiptDao = new ReceiptDaoImpl();
        List<Receipt> receipts = receiptDao.findAll();

        System.out.println(OrderService.allOrdersToString(new OrderDaoImpl(), new MenuItemDaoImpl(), new WorkerDaoImpl()));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the order to update: ");
        Order order = orders.get(scanner.nextInt() - 1);
        List<Worker> waiters = workerDao.findAllWorkersWithPosition("Waiter");
        List<Worker> cooks = workerDao.findAllWorkersWithPosition(items.stream().filter(x -> x.getId() == order.getMenuPositionId()).findFirst().get().getType().equals("Drink") ? "Barista" : "Pastry Chef");

        int counter = 1;
        for (Worker worker : waiters) {
            System.out.println(counter++ + ". Workers`s Name: " + worker.getName() + " " + worker.getSurname());
        }
        System.out.print("Choose the waiter number: ");
        order.setWaiterId(waiters.get(scanner.nextInt() - 1).getId());

        counter = 1;
        for (Worker worker : cooks) {
            System.out.println(counter++ + ". Workers`s Name: " + worker.getName() + " " + worker.getSurname());
        }
        System.out.print("Choose the cooks number: ");
        order.setWorkerId(cooks.get(scanner.nextInt() - 1).getId());

        counter = 1;
        for (MenuItem item : items) {
            System.out.println(counter++ + ". Position`s Name: " + item.getName() + ", Price " + item.getPrice());
        }
        System.out.print("Choose the position from menu number: ");
        MenuItem menuItem = items.get(scanner.nextInt() - 1);
        order.setMenuPositionId(menuItem.getId());

        order.setPrice(menuItem.getPrice());
        Receipt receipt = receipts.stream().filter(x -> x.getId() == order.getReceiptId()).findFirst().get();
        receipt.setTotalPrice(receipt.getTotalPrice() + order.getPrice());
        orderDao.update(order);
        receiptDao.update(receipt);
    }

    private static void editScheduleForDay(LocalDate day) {
        Scanner scanner = new Scanner(System.in);
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        WorkerDao workerDao = new WorkerDaoImpl();

        List<Worker> workers = workerDao.findAll();
        List<Schedule> schedules = scheduleDao.findAll();

        int counter = 1;
        for (Schedule schedule : schedules) {
            System.out.println(counter++ + ". " + schedule);
        }
        System.out.print("Choose the schedule to update: ");
        Schedule schedule = schedules.get(scanner.nextInt() - 1);


        counter = 1;
        for (Worker worker : workers) {
            System.out.println(counter++ + ". " + worker);
        }
        System.out.print("Choose the worker number: ");
        schedule.setWorkerId(workers.get(scanner.nextInt() - 1).getId());
        scanner.nextLine();
        System.out.print("Enter start time (HH:mm) ");
        String startTimeInput = scanner.nextLine();


        System.out.print("Enter end time (HH:mm): ");
        String endTimeInput = scanner.nextLine();

        schedule.setDate(Date.valueOf(day));
        schedule.setStartTime(Time.valueOf(startTimeInput + ":00"));
        schedule.setEndTime(Time.valueOf(endTimeInput + ":00"));

        scheduleDao.update(schedule);
    }

    private static void editPriceToMenuItemWithType(String type) {
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        List<MenuItem> items = menuItemDao.findAllWithType(type);

        int counter = 1;
        for (MenuItem menuItem : items) {
            System.out.println(counter++ + ". Item Name: " + menuItem.getName() + ", Price: " + menuItem.getPrice());
        }
        System.out.print("Choose the item number to edit price: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new price: ");
        items.get(choice - 1).setPrice(scanner.nextFloat());

        menuItemDao.update(items.get(choice - 1));
    }

    private static void editNameToMenuItemWithType(String type) {
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        List<MenuItem> items = menuItemDao.findAllWithType(type);

        int counter = 1;
        for (MenuItem menuItem : items) {
            System.out.println(counter++ + ". Item Name: " + menuItem.getName() + ", Price: " + menuItem.getPrice());
        }
        System.out.print("Choose the item number to edit price: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new name: ");
        items.get(choice - 1).setName(scanner.nextLine());

        menuItemDao.update(items.get(choice - 1));
    }

    private static void editEmailToWorkerWithPosition(String position) {
        WorkerDao workerDao = new WorkerDaoImpl();
        List<Worker> workers = workerDao.findAllWorkersWithPosition(position);
        int counter = 1;
        for (Worker worker : workers) {
            System.out.println(counter++ + ". Worker`s Name: " + worker.getName() + " " + worker.getSurname() + ", Email: " + worker.getEmail());
        }
        System.out.print("Choose the worker number to edit email: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new email: ");
        workers.get(choice - 1).setEmail(scanner.nextLine());
        workerDao.update(workers.get(choice - 1));
    }

    private static void editPhoneToWorkerWithPosition(String position) {
        WorkerDao workerDao = new WorkerDaoImpl();
        List<Worker> workers = workerDao.findAllWorkersWithPosition(position);
        int counter = 1;
        for (Worker worker : workers) {
            System.out.println(counter++ + ". Worker`s Name: " + worker.getName() + " " + worker.getSurname() + ", Phone number: " + worker.getPhone());
        }
        System.out.print("Choose the worker number to edit phone number: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new phone number: ");
        workers.get(choice - 1).setPhone(scanner.nextLine());
        workerDao.update(workers.get(choice - 1));
    }

    private static void editClientDiscount() {
        ClientDao clientDao = new ClientDaoImpl();
        List<Client> clients = clientDao.findAll();
        int counter = 1;
        for (Client client : clients) {
            System.out.println(counter++ + ". Client`s Name: " + client.getName() + " " + client.getSurname() + ", Discount: " + client.getDiscount());
        }
        System.out.print("Choose the client number to edit discount: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the new discount: ");
        clients.get(choice - 1).setDiscount(scanner.nextFloat());
        clientDao.update(clients.get(choice - 1));
    }

    private static void startDeleteMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            showDeleteMenu();
            choice = scanner.nextInt();

            if (choice == 1) {
                deleteMenuItemWithType("Dessert");
            }
            if (choice == 2) {
                deleteWorkerWithPosition("Waiter");
            }
            if (choice == 3) {
                deleteWorkerWithPosition("Barista");
            }
            if (choice == 4) {
                deleteClient();
            }
            if (choice == 5) {
                deleteOrder();
            }
            if (choice == 6) {
                deleteOrdersByMenuItem();
            }
            if (choice == 7) {
                deleteScheduleWithDate();
            }
            if (choice == 8) {
                deleteScheduleInBetweenDates();
            }
        } while (choice != -1);
    }

    private static void deleteMenuItemWithType(String type) {
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        List<MenuItem> menuItems = menuItemDao.findAllWithType(type);

        int counter = 1;
        for (MenuItem menuItem : menuItems) {
            System.out.println(counter++ + ". Item`s Name: " + menuItem.getName() + ", Price: " + menuItem.getPrice());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the item number to delete: ");
        menuItemDao.delete(menuItems.get(scanner.nextInt() - 1));
    }

    private static void deleteWorkerWithPosition(String position) {
        WorkerDao workerDao = new WorkerDaoImpl();
        List<Worker> workers = workerDao.findAllWorkersWithPosition(position);

        int counter = 1;
        for (Worker worker : workers) {
            System.out.println(counter++ + ". Worker`s Name: " + worker.getName() + " " + worker.getSurname());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the worker number to fire: ");
        workerDao.delete(workers.get(scanner.nextInt() - 1));
    }

    private static void deleteClient() {
        ClientDao clientDao = new ClientDaoImpl();
        List<Client> clients = clientDao.findAll();

        int counter = 1;
        for (Client client : clients) {
            System.out.println(counter++ + ". Client`s Name: " + client.getName() + " " + client.getSurname());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the client number to delete information about: ");
        clientDao.delete(clients.get(scanner.nextInt() - 1));
    }

    private static void deleteOrder() {
        System.out.println(OrderService.allOrdersToString(new OrderDaoImpl(), new MenuItemDaoImpl(), new WorkerDaoImpl()));
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the order number to delete: ");

        OrderDao orderDao = new OrderDaoImpl();
        orderDao.delete(orderDao.findAll().get(scanner.nextInt() - 1));
    }

    private static void deleteOrdersByMenuItem() {
        MenuItemDao menuItemDao = new MenuItemDaoImpl();
        List<MenuItem> menuItems = menuItemDao.findAll();
        int counter = 1;
        for (MenuItem menuItem : menuItems) {
            System.out.println(counter++ + ". Item`s Name: " + menuItem.getName() + ", Type: " + menuItem.getType());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the menu item number to delete orders of: ");
        MenuItem menuItem = menuItems.get(scanner.nextInt() - 1);

        OrderDao orderDao = new OrderDaoImpl();
        for (Order order : orderDao.findAll().stream().filter(x -> x.getMenuPositionId() == menuItem.getId()).toList()) {
            orderDao.delete(order);
        }
    }

    private static void deleteScheduleWithDate() {
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        List<Schedule> schedules = scheduleDao.findAll();
        Scanner scanner = new Scanner(System.in);
        LocalDate date = null;
        boolean validDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!validDate) {
            try {
                System.out.print("Enter a date (yyyy-MM-dd): ");
                date = LocalDate.parse(scanner.nextLine(), formatter);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
        LocalDate finalDate = date;
        for (Schedule schedule : schedules.stream().filter(x -> x.getDate().equals(Date.valueOf(finalDate))).toList()) {
            scheduleDao.delete(schedule);
        }
    }

    private static void deleteScheduleInBetweenDates() {
        Scanner scanner = new Scanner(System.in);
        LocalDate dateStart = null;
        LocalDate dateEnd = null;
        boolean validDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!validDate) {
            try {
                System.out.print("Enter a starting date (yyyy-MM-dd): ");
                dateStart = LocalDate.parse(scanner.nextLine(), formatter);
                System.out.print("Enter a ending date (yyyy-MM-dd): ");
                dateEnd = LocalDate.parse(scanner.nextLine(), formatter);
                if (dateEnd.isAfter(dateStart)) {
                    validDate = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        ScheduleDao scheduleDao = new ScheduleDaoImpl();
        List<Schedule> schedules = scheduleDao.findAll();

        LocalDate finalDateStart = dateStart;
        LocalDate finalDateEnd = dateEnd;
        for (Schedule schedule : schedules.stream().filter(x -> x.getDate().getTime() >= Date.valueOf(finalDateStart).getTime() && x.getDate().getTime() <= Date.valueOf(finalDateEnd).getTime()).toList()) {
            scheduleDao.delete(schedule);
        }
    }

    private static Date getDateFromUser() {
        Scanner scanner = new Scanner(System.in);
        LocalDate date = null;
        boolean validDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!validDate) {
            try {
                System.out.print("Enter a date (yyyy-MM-dd): ");
                date = LocalDate.parse(scanner.nextLine(), formatter);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
        return Date.valueOf(date);
    }

    private MenuExecutor() {
    }

}