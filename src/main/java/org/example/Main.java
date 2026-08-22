package org.example;

import org.example.dao.*;
import org.example.exception.CustomException;
import org.example.model.*;
import org.example.service.CustomerService;
import org.example.service.DealService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final CustomerDao customerDao = new CustomerDaoImpl();
    private static final ContactDao contactDao = new ContactDaoImpl();
    private static final DealDao dealDao = new DealDaoImpl();

    private static final CustomerService customerService = new CustomerService();
    private static final DealService dealService = new DealService();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Alege o optiune: ");

            try {
                switch (choice) {
                    case 1 -> customerMenu();
                    case 2 -> contactMenu();
                    case 3 -> dealMenu();
                    case 0 -> running = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (CustomException e) {
                System.out.println("Eroare: " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("Cauza: " + e.getCause().getMessage());
                }
            }
        }

        System.out.println("La revedere!");
        scanner.close();
    }

    // ================= MENIU PRINCIPAL =================

    private static void printMainMenu() {
        System.out.println("""
                
                ===== CRM SYSTEM =====
                1. Gestionare Customers
                2. Gestionare Contacts
                3. Gestionare Deals
                0. Iesire
                """);
    }

    // ================= CUSTOMER =================

    private static void customerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""
                    
                    --- Customers ---
                    1. Adauga customer (cu contact nou, tranzactie)
                    2. Vezi toti customerii
                    3. Cauta customer dupa id
                    4. Actualizeaza customer
                    5. Sterge customer
                    0. Inapoi
                    """);
            int choice = readInt("Alege o optiune: ");

            switch (choice) {
                case 1 -> createCustomerWithContactFlow();
                case 2 -> System.out.println(customerDao.findAll());
                case 3 -> {
                    Long id = readLong("Id customer: ");
                    Customer c = customerDao.findById(id);
                    System.out.println(c != null ? c : "Nu a fost gasit.");
                }
                case 4 -> updateCustomerFlow();
                case 5 -> {
                    Long id = readLong("Id customer de sters: ");
                    customerDao.delete(id);
                }
                case 0 -> back = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private static void createCustomerWithContactFlow() {
        System.out.println("Introdu datele contactului principal:");
        String name = readString("Nume contact: ");
        String email = readString("Email: ");
        String phone = readString("Telefon (10 cifre): ");

        Contact contact = new Contact(name, email, phone);

        String customerName = readString("Nume customer: ");
        String industry = readString("Industrie: ");

        customerService.createCustomerWithContact(contact, customerName, industry);
    }

    private static void updateCustomerFlow() {
        Long id = readLong("Id customer de actualizat: ");
        Customer existing = customerDao.findById(id);
        if (existing == null) {
            System.out.println("Nu exista un customer cu acest id.");
            return;
        }
        String name = readString("Nume nou (" + existing.getName() + "): ");
        String industry = readString("Industrie noua (" + existing.getIndustry() + "): ");
        Long primaryContact = readLong("Id contact principal (" + existing.getPrimaryContact() + "): ");

        Customer updated = new Customer(id, name, industry, primaryContact);
        customerDao.update(updated);
    }

    // ================= CONTACT =================

    private static void contactMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""
                    
                    --- Contacts ---
                    1. Adauga contact
                    2. Vezi toate contactele
                    3. Cauta contact dupa id
                    4. Actualizeaza contact
                    5. Sterge contact
                    0. Inapoi
                    """);
            int choice = readInt("Alege o optiune: ");

            switch (choice) {
                case 1 -> {
                    String name = readString("Nume: ");
                    String email = readString("Email: ");
                    String phone = readString("Telefon (10 cifre): ");
                    contactDao.save(new Contact(name, email, phone));
                }
                case 2 -> System.out.println(contactDao.findAll());
                case 3 -> {
                    Long id = readLong("Id contact: ");
                    Contact c = contactDao.findById(id);
                    System.out.println(c != null ? c : "Nu a fost gasit.");
                }
                case 4 -> updateContactFlow();
                case 5 -> {
                    Long id = readLong("Id contact de sters: ");
                    contactDao.delete(id);
                }
                case 0 -> back = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private static void updateContactFlow() {
        Long id = readLong("Id contact de actualizat: ");
        Contact existing = contactDao.findById(id);
        if (existing == null) {
            System.out.println("Nu exista un contact cu acest id.");
            return;
        }
        String name = readString("Nume nou (" + existing.getName() + "): ");
        String email = readString("Email nou (" + existing.getEmail() + "): ");
        String phone = readString("Telefon nou (" + existing.getPhoneNumber() + "): ");

        contactDao.update(new Contact(id, name, email, phone));
    }

    // ================= DEAL =================

    private static void dealMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""
                    
                    --- Deals ---
                    1. Adauga deal
                    2. Vezi toate deal-urile
                    3. Cauta deal dupa id
                    4. Vezi deal-urile unui customer
                    5. Actualizeaza suma unui deal (sincronizat)
                    6. Sterge deal
                    0. Inapoi
                    """);
            int choice = readInt("Alege o optiune: ");

            switch (choice) {
                case 1 -> createDealFlow();
                case 2 -> System.out.println(dealDao.findAll());
                case 3 -> {
                    Long id = readLong("Id deal: ");
                    Deal d = dealDao.findById(id);
                    System.out.println(d != null ? d : "Nu a fost gasit.");
                }
                case 4 -> {
                    Long customerId = readLong("Id customer: ");
                    List<Deal> deals = dealDao.findByCustomerId(customerId);
                    System.out.println(deals);
                }
                case 5 -> {
                    Long id = readLong("Id deal: ");
                    BigDecimal amount = readBigDecimal("Suma noua: ");
                    dealService.updateDealAmount(id, amount);
                    System.out.println("Suma actualizata.");
                }
                case 6 -> {
                    Long id = readLong("Id deal de sters: ");
                    dealDao.delete(id);
                }
                case 0 -> back = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private static void createDealFlow() {
        String title = readString("Titlu deal: ");
        BigDecimal amount = readBigDecimal("Suma: ");

        System.out.println("Stadii disponibile: ");
        for (DealStage stage : DealStage.values()) {
            System.out.println(" - " + stage);
        }
        DealStage stage = readDealStage("Stadiu: ");
        Long customerId = readLong("Id customer: ");

        dealDao.save(new Deal(title, amount, stage, customerId));
    }

    // ================= HELPERE CITIRE INPUT =================

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Te rog introdu un numar valid.");
            }
        }
    }

    private static Long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Te rog introdu un numar valid.");
            }
        }
    }

    private static BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Te rog introdu o suma valida (ex: 1500.50).");
            }
        }
    }

    private static DealStage readDealStage(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return DealStage.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Stadiu invalid. Foloseste unul din lista de mai sus.");
            }
        }
    }
}