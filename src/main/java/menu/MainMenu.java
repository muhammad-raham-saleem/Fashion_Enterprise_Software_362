package menu;

import model.*;
import service.*;
import util.FileManager;

import java.util.Scanner;

public class MainMenu {
    private final Scanner sc = new Scanner(System.in);

    // Shared resources (repositories and services)
    private final DesignRepository designRepo = new DesignRepository();
    private final SketchService sketchService = new SketchService(designRepo, sc);
    private final MaterialService materialService = new MaterialService(designRepo, sc);
    private final ManufacturingService manufacturingService = new ManufacturingService(designRepo);
    private final CostService costService = new CostService(designRepo, sc);
    private final Inventory inventory = new Inventory("data/inventory.txt");
    private final FinanceService financeService = new FinanceService("data/finance.txt");
    private final SaleService saleService = new SaleService(inventory, financeService);
    private final HR hr = new HR();
    private final HRService hrService = new HRService(hr, sc, "data/staff.txt");
    private final ShippingDepartmentMenu shipMenu = new ShippingDepartmentMenu(sc);
    

    public void start() {
        // Load data before menus
        FileManager.loadData(designRepo.getSketches(), designRepo.getMaterials());

        int choice;
        do {
            System.out.println("\n=== MAIN COMPANY MENU ===");
            System.out.println("1. Design Department");
            System.out.println("2. Warehouse Department");
            System.out.println("3. Sales Department");
            System.out.println("4. HR Department");
            System.out.println("5. Shipping Department");
            System.out.println("0. Exit");
            System.out.print("Choose a department: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> new DesignMenu(sc, sketchService, materialService, manufacturingService, costService, designRepo).start();
                case 2 -> new WarehouseMenu(sc).start();
                case 3 -> new SalesMenu(sc, saleService).start();
                case 4 -> new HRMenu(sc, hr, hrService).start();
                case 5 -> new ShippingDepartmentMenu(sc).start();

                case 0 -> System.out.println("Exiting system...");
                default -> System.out.println("Invalid option. Try again.");
            }

        } while (choice != 0);

        sc.close();
    }
}


