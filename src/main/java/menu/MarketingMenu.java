package menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Product;
import model.ProductRepository;
import service.FashionShowService;

public class MarketingMenu {
    private final Scanner sc;

    private final FashionShowService fashionShowService;

    public MarketingMenu(Scanner sc, FashionShowService fashionShowService) {
        this.sc = sc;
        this.fashionShowService = fashionShowService;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n--- MARKETING MENU ---");
            System.out.println("1. Run Fashion Show");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> runFashionShow();
                case 0 -> System.out.println("Returning to Main Menu..."); 
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }
    private void runFashionShow(){
        System.out.println("------ RUN FASHION SHOW ------");
    
        List<String> items = new ArrayList<>();
        String item;

        System.out.println("Enter items to include in the show (type 'done' when finished):");
        while (true) {
            item = sc.nextLine().trim();
            if (item.equalsIgnoreCase("done")) break;
            if (!item.isEmpty()) items.add(item);
        }

        System.out.println("Enter Expected Attendance: ");
        int attendance = Integer.parseInt(sc.nextLine());

        System.out.println("Enter venue name: ");
        String venue = sc.nextLine();

        System.out.println("Enter date (yyyy-mm-dd)");
        LocalDate date = LocalDate.parse(sc.nextLine());
        

        System.out.println("Enter ticket price");
        double ticketPrice = Double.parseDouble(sc.nextLine());

        double profit = attendance * ticketPrice;

        fashionShowService.runFashionShow(items, attendance, venue, profit, date);


        
    }


}
