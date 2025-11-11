package menu;

import java.util.Scanner;

public class ShippingDepartmentMenu {

    private final Scanner sc;

    public ShippingDepartmentMenu(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n=== SHIPPING DEPARTMENT MENU ===");
            System.out.println("1. View pending shipments (TODO)");
            System.out.println("2. Ship a product (TODO)");
            System.out.println("3. Track shipment (TODO)");
            System.out.println("0. Return to main menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> System.out.println("Feature coming soon: View pending shipments.");
                case 2 -> System.out.println("Feature coming soon: Ship a product.");
                case 3 -> System.out.println("Feature coming soon: Track shipment.");
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Try again.");
            }

        } while (choice != 0);
    }
}
