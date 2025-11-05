package menu;

import java.util.Scanner;

public class SalesMenu {
    private final Scanner sc;

    public SalesMenu(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n--- SALES MENU ---");
            System.out.println("1. Record Sale");
            System.out.println("2. View Sales Report");
            System.out.println("3. Manage Clients");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> System.out.println("Recording sale (placeholder)...");
                case 2 -> System.out.println("Viewing report (placeholder)...");
                case 3 -> System.out.println("Managing clients (placeholder)...");
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }
}

