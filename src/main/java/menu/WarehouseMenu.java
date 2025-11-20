package menu;

import model.*;
import service.OnlineOrderService;
import service.QualityControlService;
import util.FileManager;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class WarehouseMenu {
    private final Scanner sc;
    private QualityControlService qcService;
    private HashMap<Integer, ManufacturingBatch> batches;

    private final OnlineOrderService onlineOrderService; // NEW

    public WarehouseMenu(Scanner sc) {
        this.sc = sc;
        this.batches = loadAllBatches();
        this.onlineOrderService = new OnlineOrderService(sc); // NEW
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n--- WAREHOUSE MENU ---");
            System.out.println("1. List batches");
            System.out.println("2. Review batches quality control");
            System.out.println("3. Fulfill online order");      // NEW
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // IMPORTANT: consume newline so nextLine() works

            switch (choice) {
                case 1 -> {
                    for (ManufacturingBatch batch : batches.values()) {
                        System.out.println(batch);
                    }
                }
                case 2 -> {
                    System.out.print("Enter batch ID to inspect: ");
                    int batchId;
                    try {
                        batchId = sc.nextInt();
                        sc.nextLine(); // consume newline
                    } catch (InputMismatchException e) {
                        System.out.print("Invalid batch ID. Please enter a number. ");
                        sc.next(); // Clear invalid input
                        break;
                    }
                    ManufacturingBatch batch = batches.get(batchId);
                    if (batch != null) {
                        qcService = new QualityControlService(batch);
                        qcService.performBatchInspection();
                    } else {
                        System.out.println("Batch ID not found.");
                    }
                }
                case 3 -> onlineOrderService.fulfillOnlineOrder(); // NEW
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

    /**
     * Loads all manufacturing batches from the CSV file.
     * @return HashMap of batch ID to ManufacturingBatch object.
     */
    public HashMap<Integer, ManufacturingBatch> loadAllBatches() {
        List<String> lines = FileManager.readLines("data/batches.csv");
        HashMap<Integer, ManufacturingBatch> batchesById = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) { // Skip header line
            ManufacturingBatch batch = loadBatchFromString(lines.get(i));
            batchesById.put(batch.getId(), batch);
        }
        return batchesById;
    }

    public ManufacturingBatch loadBatchFromString(String batchString) {
        ProductRepository productRepo = new ProductRepository("data/products.txt");
        String[] parts = batchString.split(",");
        int batchId = Integer.parseInt(parts[0]);
        int productId = Integer.parseInt(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        InspectionReport.Status.valueOf(parts[3]);

        Product product = productRepo.findById(productId);
        List<Item> items = new java.util.ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            items.add(new Item(i + 1, product));
        }

        return new ManufacturingBatch(batchId, product, items);
    }
}
