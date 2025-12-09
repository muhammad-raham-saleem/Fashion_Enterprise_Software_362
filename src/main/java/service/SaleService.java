package service;

import model.*;
import util.FileManager;

import java.time.LocalDate;
import java.util.*;

public class SaleService {
    private final Scanner scan;
    private final Inventory inventory;
    private final FinanceService finance;

    public SaleService(Scanner scan, Inventory inventory, FinanceService finance) {
        this.scan = scan;
        this.inventory = inventory;
        this.finance = finance;
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

        logSale(product);
    }

    public void logSale(Product product) {
        String filePath = "data/salesLogs.txt";
        String entry = LocalDate.now() + ", " + product.getName() + ", " + product.getPrice();
        FileManager.appendLine(filePath, entry);
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
}
