package menu;

import java.util.Scanner;

import model.Product;
import model.ProductRepository;
import service.ProductService;
import service.ReturnService;
import service.SaleService;
import util.LogManager;

public class SalesMenu implements Menu {
    private final Scanner sc;

    private final SaleService saleService;
    private final ReturnService returnService;
    private final ProductRepository productRepo = new ProductRepository("data/products.txt");
    private final ProductService ProductService = new ProductService(productRepo, new LogManager("data/productLogs.txt"));

    public SalesMenu(Scanner sc, SaleService saleService, ReturnService returnService) {
        this.sc = sc;
        this.saleService = saleService;
        this.returnService = returnService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[]{
                new MenuOption(1, "Start Sale", this::handleSale),
                new MenuOption(2, "Update Product Price", this::handleUpdatePrice),
                new MenuOption(3, "Start Return", this::handleReturn),
                new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n--- SALES MENU ---");
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

    public void handleSale() {
        System.out.println("Enter product name.");
        String productName = sc.nextLine();
        Product product = productRepo.findByName(productName);

        if (product == null) {
            System.out.println("Product doesn't exist.");
            productRepo.getAll().forEach(p -> System.out.println(p.getId() + ": " + p.getName()));
            return;
        }
        saleService.completeSale(product);
    }

    public void handleUpdatePrice() {
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
            return;
        }
        System.out.println("Enter new price for: " + product.getName());
        double newPrice = sc.nextDouble();
        ProductService.updatePrice(product, newPrice);
    }

    public void handleReturn() {
        System.out.println("Enter product name.");
        String productName = sc.nextLine();
        Product product = productRepo.findByName(productName);

        if (product == null) {
            System.out.println("Product doesn't exist.");
            return;
        }
        returnService.processReturn(product);
    }
}

