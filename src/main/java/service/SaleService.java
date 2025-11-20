package service;

import model.*;
import util.FileManager;

import java.util.*;

public class SaleService {
    private final Scanner scan = new Scanner(System.in);
    private final Inventory inventory;
    private final FinanceService finance;
    private final ProductRepository productRepo;

    public SaleService(Inventory inventory, FinanceService finance) {
        this.inventory = inventory;
        this.finance = finance;
        this.productRepo = new ProductRepository("data/products.txt");
    }

    public void completeSale(Product product) {

        if (!inventory.hasStock(product.getId())) {
            System.out.println("Item is out of stock");
            return;
        }

        System.out.println("Item:" + product.getName());
        System.out.println("Price: $" + product.getPrice());

        System.out.println("Enter payment method (Cash/Card)");

        String paymentMethod = scan.nextLine();

        boolean success = processPayment(paymentMethod, product.getPrice());

        if (!success) {
            System.out.println("Payment failed. Sale cancelled");

            return;

        }

        inventory.reduceStock(product.getId());
        finance.addSale(product.getPrice());

        Receipt receipt = new Receipt(product, product.getPrice(), paymentMethod);
        receipt.outputReceipt();
        System.out.println(receipt);
    }

    // Will need to be changed once it can fail. Random chance for now
    private boolean processPayment(String method, double amount) {
        if (method.equalsIgnoreCase("card")) {
            // Simulate declined card
            if (Math.random() < 0.02) {  // 2% chance decline
                System.out.println("Card was declined!");
                return false;
            }
            return true;
        } else if (method.equalsIgnoreCase("cash")) {
            // Simulate counterfeit cash
            if (Math.random() < 0.02) { // 2% chance counterfeit
                System.out.println("Counterfeit cash detected!");
                return false;
            }
            return true;
        }

        System.out.println("Invalid payment method!");
        return false;
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
                    + "Return denied - no proof of purchase.");
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
