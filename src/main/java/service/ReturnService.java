package service;

import model.*;
import util.FileManager;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ReturnService {
    private final Scanner scan;
    private final FinanceService finance;
    private final ProductRepository productRepo;

    public ReturnService(Scanner scan, FinanceService finance) {
        this.scan = scan;
        this.finance = finance;
        this.productRepo = new ProductRepository("data/products.txt");
    }

    public enum ReturnReason {
        DEFECTIVE,
        WRONG_SIZE,
        NOT_AS_DESCRIBED,
        BETTER_PRICE_FOUND,
        NO_LONGER_NEEDED
    }

    public void enumerateReturnReasons() {
        System.out.println("Return Reasons:");
        for (int i = 0; i < ReturnReason.values().length; i++) {
            System.out.println((i + 1) + ". " + ReturnReason.values()[i]);
        }
    }

    /**
     * Helper to choose a return reason
     */
    public void chooseReturnReason() {
        System.out.println("Choose a return number:");
        enumerateReturnReasons();
        int choice = scan.nextInt();
        scan.nextLine(); // consume newline
        try {
            if (choice < 1 || choice > ReturnReason.values().length) {
                throw new IllegalArgumentException("Invalid return reason selected.");
            }
            ReturnReason reason = ReturnReason.values()[choice - 1];
            System.out.println("You selected: " + reason);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid return reason selected.");
        }
    }

    /**
     * Process a return for the given product
     * from the sales rep perspective.
     * Saves to a batch for QC inspection
     *
     * @param product The product being returned
     */
    public void processReturn(Product product) {
        System.out.println("=== RETURN PROCESS ===");
        System.out.println("Product: " + product.getName());
        System.out.println("Price: $" + product.getPrice());

        Receipt receipt;

        // Check for receipt and validate it
        System.out.println("\nDoes customer have receipt? (y/n)");
        String hasReceiptInput = scan.nextLine();

        if (hasReceiptInput.equalsIgnoreCase("y")) {
            System.out.print("Enter receipt ID (format: R<timestamp>): ");
            String receiptId = scan.nextLine().trim();

            // Load the receipt from file
            receipt = Receipt.loadByReceiptId(receiptId, productRepo);

            if (receipt == null) {
                System.out.println("ERROR: Could not load receipt from file!");
                System.out.println("Return denied.");
                return;
            }

            // Verify receipt matches the product
            if (!receipt.matchesProduct(product)) {
                System.out.println("ERROR: Receipt does not match this product!"
                        + "Receipt is for: " + receipt.getProduct().getName()
                        + "Customer is returning: " + product.getName()
                        + "Return denied - product mismatch.");
                return;
            }

            // Check if within return window (7 days)
            if (!receipt.isWithinReturnWindow()) {
                System.out.println("ERROR: Return window expired!"
                        + "Purchase date: " + receipt.getSaleDate()
                        + "Return window: 7 days from purchase"
                        + "Return denied - outside return window.");
                return;
            }

            System.out.println("  Receipt validated successfully!"
                    + "  Purchase Date: " + receipt.getSaleDate()
                    + "  Original Amount: $" + receipt.getAmount());

        } else {
            // No receipt - attempt alternative verification
            System.out.println("\nAttempting to verify purchase without receipt...");
            System.out.println("Enter customer email or phone number:");
            String customerInfo = scan.nextLine();

            System.out.println("Searching for purchases by: " + customerInfo
                    + "\nReturn denied - no proof of purchase.");
            return;
        }

        // Inspect for damage
        System.out.println("\nInspecting product condition...");
        System.out.println("Is product damaged? (y/n)");
        String isDamaged = scan.nextLine();

        if (isDamaged.equalsIgnoreCase("y")) {
            System.out.println("Product is damaged. Return denied.");
            return;
        }

        chooseReturnReason();

        // Refund processing
        double refundAmount = product.getPrice();
        System.out.println("\nProcess refund? (y/n)");
        String refundConfirm = scan.nextLine();
        if (!refundConfirm.equalsIgnoreCase("y")) {
            System.out.println("Refund cancelled by operator.");
            return;
        }

        finance.processReturn(product, 1);
        System.out.println("Refunded $" + refundAmount + " to method: " + receipt.getPaymentMethod());

        // QC batch registration
        int batchId = FileManager.getNextId("data/batches.csv");
        List<Item> returnedItems = Collections.singletonList(new Item(1, product));
        ManufacturingBatch returnBatch = new ManufacturingBatch(batchId, product, returnedItems);
        returnBatch.saveToBatchFile();

        System.out.println("Added to QC batch #" + batchId + " for inspection.");
        System.out.println("=== RETURN COMPLETED ===");
    }
}
