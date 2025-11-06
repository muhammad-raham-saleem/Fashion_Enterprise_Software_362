package menu;

import java.util.List;
import java.util.Scanner;

import model.Product;
import model.ProductRepository;
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
        ProductRepository productRepo = new ProductRepository("data/products.txt");
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
                    Product product = productRepo.findByName(productName);

                    if (product == null){
                        System.out.println("Product doesn't exist.");
                        break;
                    }
                    saleService.completeSale(product);
                }
                case 0 -> System.out.println("Returning to Main Menu..."); 
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

}

