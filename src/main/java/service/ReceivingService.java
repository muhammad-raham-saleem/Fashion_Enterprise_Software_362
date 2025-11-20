package service;

import model.Item;
import model.ManufacturingBatch;
import model.Product;
import model.ProductRepository;
import util.FileManager;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the "Receive Shipment from Manufacturer" use case.
 * - Prompts user for product ID + quantity
 * - Creates a new ManufacturingBatch
 * - Adds it to the in-memory batches map
 * - Appends the new batch to data/batches.csv
 */
public class ReceivingService {

    private final Scanner sc;
    private final HashMap<Integer, ManufacturingBatch> batches;
    private final ProductRepository productRepo;
    private static final String BATCHES_FILE = "data/batches.csv";

    public ReceivingService(Scanner sc, HashMap<Integer, ManufacturingBatch> batches) {
        this.sc = sc;
        this.batches = batches;
        this.productRepo = new ProductRepository("data/products.txt");
    }

    public void receiveShipmentFromManufacturer() {
        System.out.println("\n=== Receive Shipment from Manufacturer ===");

        try {
            // Ask product ID
            System.out.print("Enter product ID: ");
            int productId = sc.nextInt();
            sc.nextLine(); // consume newline

            Product product = productRepo.findById(productId);
            if (product == null) {
                System.out.println("No product found with ID " + productId);
                return;
            }

            // Ask quantity
            System.out.print("Enter quantity received: ");
            int quantity = sc.nextInt();
            sc.nextLine(); // consume newline

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return;
            }

            // Create new unique batch ID
            int newBatchId = batches.keySet().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0) + 1;

            // Create new items list
            List<Item> items = new java.util.ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                items.add(new Item(i + 1, product));
            }

            // New batch object
            ManufacturingBatch newBatch = new ManufacturingBatch(newBatchId, product, items);

            // Add to in memory map
            batches.put(newBatchId, newBatch);

            // Status column default (matches batches.csv: batchId,productId,quantity,status)
            String status = "PENDING";

            // Append to CSV
            String csvLine = newBatchId + "," + productId + "," + quantity + "," + status;
            boolean ok = FileManager.appendLine(BATCHES_FILE, csvLine);
            if (!ok) {
                System.out.println("Warning: failed to append new batch to " + BATCHES_FILE);
            }

            System.out.println("Shipment received and batch created:");
            System.out.println(newBatch);

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
            sc.nextLine(); // clear invalid input
        }
    }
}
