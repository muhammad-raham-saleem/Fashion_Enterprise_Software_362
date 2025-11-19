package service;

import model.*;
import util.FileManager;

import java.util.*;

public class SaleService {
    private Scanner scan = new Scanner(System.in);
    private Inventory inventory;
    private FinanceService finance;

    public SaleService(Inventory inventory, FinanceService finance){
        this.inventory = inventory;
        this.finance = finance;
    }

    public void completeSale(Product product){

        if(!inventory.hasStock(product.getId())){
            System.out.println("Item is out of stock");
            return;
        }

        System.out.println("Item:" + product.getName());
        System.out.println("Price: $" + product.getPrice());

        System.out.println("Enter payment method (Cash/Card)");

        String paymentMethod = scan.nextLine();

        boolean success = processPayment(paymentMethod, product.getPrice());

        if (!success){
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
        }
        else if (method.equalsIgnoreCase("cash")) {
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
     * @param product The product being returned
     */
    public void processReturn(Product product) {
        System.out.println("=== RETURN PROCESS ===");
        System.out.println("Product: " + product.getName());
        System.out.println("Price: $" + product.getPrice());

        // Step 1: Check for receipt
        System.out.println("\nDoes customer have receipt? (y/n)");
        String hasReceipt = scan.nextLine();

        if (!hasReceipt.equalsIgnoreCase("y")) {
            System.out.println("\nAttempting to verify purchase...");
            System.out.println("Enter customer email or phone number:");
            String customerInfo = scan.nextLine();
            System.out.println("Purchase found in system!");
        }

        // Step 2: Check return window
        System.out.println("\nIs purchase within 7 days? (y/n)");
        String withinWindow = scan.nextLine();

        if (!withinWindow.equalsIgnoreCase("y")) {
            System.out.println("Return window expired. Return denied.");
            return;
        }

        // Step 3: Inspect for damage
        System.out.println("\nInspecting product condition...");
        System.out.println("Is product damaged? (y/n)");
        String isDamaged = scan.nextLine();

        if (isDamaged.equalsIgnoreCase("y")) {
            System.out.println("Product is damaged. Return denied.");
            return;
        }

        // Step 4: Process refund
        System.out.println("\nProcessing refund...");
        System.out.println("Original payment method (Cash/Card):");
        String paymentMethod = scan.nextLine();

        System.out.println("Refunding $" + product.getPrice() + " via " + paymentMethod);
        finance.processReturn(product, 1); // TODO: quantity needs to come from receipt

        // Step 5: Create a batch for the returned item to be re-inspected by QC
        int batchId = FileManager.getNextId("data/batches.csv");
        List<Item> returnedItems = Collections.singletonList(new Item(1, product));
        ManufacturingBatch returnBatch = new ManufacturingBatch(batchId, product, returnedItems);

        // Save the batch to batches.csv for QC inspection
        returnBatch.saveToBatchFile();

        System.out.println("\nReturned item added to QC batch #" + batchId + " for inspection.");
        System.out.println("=== RETURN COMPLETED ===");
    }

}
