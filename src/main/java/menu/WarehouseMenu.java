package menu;

import java.util.Scanner;

public class WarehouseMenu {
    private final Scanner sc;

    public WarehouseMenu(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n--- WAREHOUSE MENU ---");
            System.out.println("1. View Inventory");
            System.out.println("2. Add New Item");
            System.out.println("3. Check Stock Levels");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> System.out.println("Displaying inventory (placeholder)...");
                case 2 -> System.out.println("Adding item (placeholder)...");
                case 3 -> System.out.println("Checking stock (placeholder)...");
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }
}

