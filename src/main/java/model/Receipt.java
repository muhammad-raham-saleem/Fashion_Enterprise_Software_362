package model;
import util.FileManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Receipt {
    private Product product;
    private double amount;
    private Date saleDate;
    private String paymentMethod;

    /**
     * Load receipt from file
     * @param filepath
     */
    public Receipt(String filepath) {
        FileManager.readLines(filepath);
        product = null;
        amount = 0.0;
        saleDate = new Date();
        paymentMethod = "";
    }

    public Receipt(Product product, double amount, String paymentMethod) {
        this.product = product;
        this.amount = amount;
        this.saleDate = new Date();
        this.paymentMethod = paymentMethod;
    }

    public String toString() {
        return "----------RECEIPT----------\n" +
                "Date: " + new Date() + "\n" +
                "Item: " + product.getName() + "\n" +
                "Price: $" + product.getPrice() + "\n" +
                "Total Paid: $" + amount + "\n" +
                "---------------------------\n";
    }

    public Product getProduct() {
        return product;
    }

    /**
     * Outputs the receipt into the receipts directory
     */
    public void outputReceipt() {
        List<String> lines = new ArrayList<>();
        Collections.addAll(lines, this.toString().split("\n"));
        FileManager.writeLines("data/receipts/" + product.getName() + Instant.now() + ".txt", lines);
    }
}
