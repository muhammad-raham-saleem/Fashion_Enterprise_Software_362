package menu;

import model.*;
import service.OnlineOrderService;
import service.QualityControlService;
import service.ReceivingService;
import service.PickListService; // ADD
import util.FileManager;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class WarehouseMenu implements Menu {
    private final Scanner sc;
    private QualityControlService qcService;
    private HashMap<Integer, ManufacturingBatch> batches;

    private final OnlineOrderService onlineOrderService;
    private final ReceivingService receivingService;
    private final PickListService pickListService; // ADD

    public WarehouseMenu(Scanner sc) {
        this.sc = sc;
        this.batches = loadAllBatches();
        this.onlineOrderService = new OnlineOrderService(sc);
        this.receivingService = new ReceivingService(sc, batches);
        this.pickListService = new PickListService(onlineOrderService); // ADD
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "List batches", () -> {
                for (ManufacturingBatch batch : batches.values()) {
                    System.out.println(batch);
                }
            }),
            new MenuOption(2, "Review batches quality control", () -> {
                System.out.print("Enter batch ID to inspect: ");
                int batchId;
                try {
                    batchId = sc.nextInt();
                    sc.nextLine(); // consume newline
                } catch (InputMismatchException e) {
                    System.out.print("Invalid batch ID. Please enter a number. ");
                    sc.next(); // Clear invalid input
                    return;
                }
                ManufacturingBatch batch = batches.get(batchId);
                if (batch != null) {
                    qcService = new QualityControlService(batch);
                    qcService.performBatchInspection();
                } else {
                    System.out.println("Batch ID not found.");
                }
            }),
            new MenuOption(3, "Receive shipment from manufacturer", receivingService::receiveShipmentFromManufacturer),
            new MenuOption(4, "Fulfill online order", onlineOrderService::fulfillOnlineOrder),
            new MenuOption(5, "Generate pick list for an online order", () -> { // ADD
                System.out.print("Enter order ID to generate pick list: ");
                String orderId = sc.nextLine().trim();
                if (orderId.isEmpty()) {
                    System.out.println("Order ID cannot be empty.");
                    return;
                }
                pickListService.generatePickListForOrder(orderId);
            }),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n--- WAREHOUSE MENU ---");
            displayOptions();

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            executeOption(choice);
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
