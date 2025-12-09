package menu;

import service.FashionShowService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FashionShowMenu implements Menu {
    private final Scanner sc;
    private final FashionShowService fashionShowService;

    public FashionShowMenu(Scanner sc, FashionShowService fashionShowService) {
        this.sc = sc;
        this.fashionShowService = fashionShowService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "Schedule Fashion Show", this::scheduleFashionShow),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
    }

    private void scheduleFashionShow() {
        System.out.println("\n========== SCHEDULE FASHION SHOW ==========");

        // Get products to showcase
        List<String> products = new ArrayList<>();
        System.out.println("Enter product names to showcase (type 'done' when finished):");
        while (true) {
            System.out.print("Product name: ");
            String product = sc.nextLine().trim();
            if (product.equalsIgnoreCase("done")) {
                break;
            }
            if (!product.isEmpty()) {
                products.add(product);
            }
        }

        if (products.isEmpty()) {
            System.out.println("No products entered. Fashion show cancelled.");
            return;
        }

        // Get expected attendance
        System.out.print("Enter expected attendance: ");
        int expectedAttendance = 0;
        try {
            expectedAttendance = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Fashion show cancelled.");
            return;
        }

        // Get venue
        System.out.print("Enter venue: ");
        String venue = sc.nextLine().trim();
        if (venue.isEmpty()) {
            System.out.println("Venue cannot be empty. Fashion show cancelled.");
            return;
        }

        // Get projected profit
        System.out.print("Enter projected profit: $");
        double projectedProfit = 0.0;
        try {
            projectedProfit = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Fashion show cancelled.");
            return;
        }

        // Get date
        System.out.print("Enter date (yyyy-MM-dd): ");
        LocalDate date = null;
        try {
            String dateStr = sc.nextLine().trim();
            date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Fashion show cancelled.");
            return;
        }

        // Run the fashion show
        fashionShowService.runFashionShow(products, expectedAttendance, venue, projectedProfit, date);
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n========== FASHION SHOW MENU ==========");
            displayOptions();

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            executeOption(choice);
        } while (choice != 0);
    }
}

