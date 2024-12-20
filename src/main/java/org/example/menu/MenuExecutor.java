package org.example.menu;


import org.example.dao.clientDAO.ClientDao;
import org.example.dao.clientDAO.ClientDaoImpl;
import org.example.dao.menuItemDAO.MenuItemDao;
import org.example.dao.menuItemDAO.MenuItemDaoImpl;
import org.example.dao.workerDAO.WorkerDao;
import org.example.dao.workerDAO.WorkerDaoImpl;
import org.example.model.Client;
import org.example.model.MenuItem;
import org.example.model.Worker;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        } while (choice != -1);
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
        } while (choice != -1);
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
        System.out.println("3. Pastry chef");

        int choice = scanner.nextInt();
        worker.setPosition(choice == 1 ? "Barista" : choice == 2 ? "Waiter" : "Pastry chef");

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
        } while (choice != -1);
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

    private MenuExecutor() {
    }

}
