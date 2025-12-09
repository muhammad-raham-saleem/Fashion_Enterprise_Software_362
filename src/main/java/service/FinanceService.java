package service;

import java.util.*;

import model.Product;
import util.FileManager;

public class FinanceService {

    private String filename;
    private double balance;
    private double totalRevenue;
    private double totalExpenses;

    public FinanceService(String filename) {
        this.filename = filename;
        this.balance = 0;
        this.totalRevenue = 0;
        this.totalExpenses = 0;
        loadBalance();
    }

    private void loadBalance() {
        List<String> lines = util.FileManager.readLines(filename);

        for (String line : lines) {
            if (line.startsWith("balance")) {
                balance = Double.parseDouble(line.split("=")[1]);
            } else if (line.startsWith("revenue")) {
                totalRevenue = Double.parseDouble(line.split("=")[1]);
            } else if (line.startsWith("expenses")) {
                totalExpenses = Double.parseDouble(line.split("=")[1]);
            }
        }
    }

    private void saveBalance() {
        List<String> lines = new ArrayList<>();
        lines.add("balance=" + balance);
        lines.add("revenue=" + totalRevenue);
        lines.add("expenses=" + totalExpenses);
        FileManager.writeLines(filename, lines);
    }

    public void addRevenue(double amount) {
        balance += amount;
        totalRevenue += amount;
        saveBalance();
    }

    public void addExpense(double amount) {
        balance -= amount;
        totalExpenses += amount;
        saveBalance();
    }

    /**
     * Process a product return
     *
     * @param product product being returned
     * @param amount  amount of products being returned
     */
    public void processReturn(Product product, int amount) {
        balance -= product.getPrice() * amount;
        totalRevenue -= product.getPrice() * amount;
        saveBalance();
    }

    public double getBalance() {
        return balance;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }
}
