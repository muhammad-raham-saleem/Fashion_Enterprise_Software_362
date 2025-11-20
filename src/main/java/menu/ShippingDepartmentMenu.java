package menu;

import java.util.Scanner;

public class ShippingDepartmentMenu implements Menu {

    private final Scanner sc;

    public ShippingDepartmentMenu(Scanner sc) {
        this.sc = sc;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "View pending shipments (TODO)", () -> System.out.println("Feature coming soon: View pending shipments.")),
            new MenuOption(2, "Ship a product (TODO)", () -> System.out.println("Feature coming soon: Ship a product.")),
            new MenuOption(3, "Track shipment (TODO)", () -> System.out.println("Feature coming soon: Track shipment.")),
            new MenuOption(0, "Return to main menu", () -> System.out.println("Returning to main menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n=== SHIPPING DEPARTMENT MENU ===");
            displayOptions();

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            executeOption(choice);

        } while (choice != 0);
    }

}
