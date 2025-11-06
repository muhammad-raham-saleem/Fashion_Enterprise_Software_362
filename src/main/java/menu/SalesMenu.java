package menu;

import java.util.List;
import java.util.Scanner;

import model.Product;
import service.SaleService;
import util.FileManager;

public class SalesMenu {
    private final Scanner sc;

    private final SaleService saleService;

    public SalesMenu(Scanner sc, SaleService saleService) {
        this.sc = sc;
        this.saleService = saleService;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n--- SALES MENU ---");
            System.out.println("1. Start Sale");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter product name.");
                    String productName = sc.nextLine();
                    String productLine = findProductLine(productName);

                    if (productLine == null){
                        System.out.println("Product doesn't exist.");
                        break;
                    }
                    Product product = convertLineToProduct(productLine);

                    saleService.completeSale(product);
                }
                case 0 -> System.out.println("Returning to Main Menu..."); 
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

        private String findProductLine(String productName) {
        List<String> lines = FileManager.readLines("products.txt");

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            
            if (parts[1].equalsIgnoreCase(productName)) {
                return lines.get(i); 
            }
        }

        return null; 
    }

        private Product convertLineToProduct(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        double price = Double.parseDouble(parts[2]);



        return new Product(id, name, price);
    }


}

