package menu;

import java.util.Scanner;

import model.Product;
import model.ProductRepository;
import service.ProductService;
import service.ReturnService;
import service.SaleService;
import util.LogManager;

public class SalesMenu {
    private final Scanner sc;

    private final SaleService saleService;
    private final ReturnService returnService;

    public SalesMenu(Scanner sc, SaleService saleService, ReturnService returnService) {
        this.sc = sc;
        this.saleService = saleService;
        this.returnService = returnService;
    }

    public void start() {
        int choice;
        ProductRepository productRepo = new ProductRepository("data/products.txt");
        ProductService ProductService = new ProductService(productRepo, new LogManager("data/productLogs.txt"));
        do {
            System.out.println("\n--- SALES MENU ---");
            System.out.println("1. Start Sale");
            System.out.println("2. Update Product Price");
            System.out.println("3. Start return");
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

                    if (product == null) {
                        System.out.println("Product doesn't exist.");
                        productRepo.getAll().forEach(p -> System.out.println(p.getId() + ": " + p.getName()));
                        break;
                    }
                    saleService.completeSale(product);
                }
                case 2 -> {
                    System.out.println("List of all products");
                    for (Product p : productRepo.getAll()) {
                        System.out.println(p.getId() + ": " + p.getName() + " $" + p.getPrice());
                    }
                    System.out.println("Enter productID to change.");
                    int id = sc.nextInt();
                    sc.nextLine();

                    Product product = ProductService.findProductByID(id);

                    if (product == null) {
                        System.out.println("Product Not Found. Update cancelled");
                        break;
                    }
                    System.out.println("Enter new price for: " + product.getName());
                    double newPrice = sc.nextDouble();
                    ProductService.updatePrice(product, newPrice);
                }
                case 3 -> {
                    System.out.println("Enter product name.");
                    String productName = sc.nextLine();
                    Product product = productRepo.findByName(productName);

                    if (product == null) {
                        System.out.println("Product doesn't exist.");
                        break;
                    }
                    returnService.processReturn(product);
                }
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

}

