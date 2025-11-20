package model;

import util.FileManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Receipt {
    private String receiptId;
    private Product product;
    private double amount;
    private Date saleDate;
    private String paymentMethod;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String RECEIPT_DIR = "data/receipts/";

    /**
     * Load receipt from file
     */
    public Receipt(String filepath, ProductRepository productRepo) {
        List<String> lines = FileManager.readLines(filepath);
        int productId = -1;

        // Parse receipt file
        for (String line : lines) {
            if (line.startsWith("Receipt ID:")) {
                this.receiptId = line.substring("Receipt ID:".length()).trim();
            } else if (line.startsWith("Date:")) {
                String dateStr = line.substring("Date:".length()).trim();
                try {
                    this.saleDate = DATE_FORMAT.parse(dateStr);
                } catch (ParseException e) {
                    this.saleDate = new Date(); // fallback
                }
            } else if (line.startsWith("Product ID:")) {
                String productIdStr = line.substring("Product ID:".length()).trim();
                try {
                    productId = Integer.parseInt(productIdStr);
                } catch (NumberFormatException e) {
                    // Handle invalid product ID
                }
            } else if (line.startsWith("Total Paid: $")) {
                String amountStr = line.substring("Total Paid: $".length()).trim();
                this.amount = Double.parseDouble(amountStr);
            } else if (line.startsWith("Payment Method:")) {
                this.paymentMethod = line.substring("Payment Method:".length()).trim();
            }
        }

        // Load the actual product from repository
        if (productId != -1 && productRepo != null) {
            this.product = productRepo.findById(productId);
        }
    }

    public Receipt(Product product, double amount, String paymentMethod) {
        this.receiptId = generateReceiptId();
        this.product = product;
        this.amount = amount;
        this.saleDate = new Date();
        this.paymentMethod = paymentMethod;
    }

    private String generateReceiptId() {
        return "R" + System.currentTimeMillis();
    }

    public String toString() {
        return "----------RECEIPT----------\n"
                + "Receipt ID: " + receiptId + "\n"
                + "Date: " + DATE_FORMAT.format(saleDate) + "\n"
                + "Product ID: " + product.getId() + "\n"
                + "Item: " + product.getName() + "\n"
                + "Price: $" + product.getPrice() + "\n"
                + "Total Paid: $" + amount + "\n"
                + "Payment Method: " + paymentMethod + "\n"
                + "---------------------------\n";
    }


    /**
     * Outputs the receipt into the receipts directory
     */
    public void outputReceipt() {
        List<String> lines = new ArrayList<>();
        Collections.addAll(lines, this.toString().split("\n"));
        FileManager.writeLines(RECEIPT_DIR + receiptId + ".txt", lines);
    }

    /**
     * Check if this receipt is within the return window (7 days)
     */
    public boolean isWithinReturnWindow() {
        long daysSincePurchase = (new Date().getTime() - saleDate.getTime()) / (1000 * 60 * 60 * 24);
        return daysSincePurchase <= 7;
    }

    /**
     * Check if this receipt matches the given product
     */
    public boolean matchesProduct(Product product) {
        return this.product.getId() == product.getId();
    }

    public Product getProduct() {
        return product;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public double getAmount() {
        return amount;
    }

    /**
     * Load a receipt from file by receipt ID
     *
     * @param receiptId   Receipt ID to load
     * @param productRepo Product repository to load product details
     * @return Receipt object if found, null otherwise
     */
    public static Receipt loadByReceiptId(String receiptId, ProductRepository productRepo) {
        String filepath = RECEIPT_DIR + receiptId + ".txt";
        java.io.File file = new java.io.File(filepath);
        if (!file.exists()) {
            return null;
        }
        return new Receipt(filepath, productRepo);
    }
}


