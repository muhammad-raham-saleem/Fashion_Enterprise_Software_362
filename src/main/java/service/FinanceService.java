package service;

import java.util.*;

import model.Product;
import util.FileManager;

public class FinanceService {
    
    private double balance; 
    private String filename;

    public FinanceService(String filename){
        this.filename = filename;
        loadBalance();
    }

    private void loadBalance(){
        List<String> lines = util.FileManager.readLines(filename);

        for (String line : lines){
            if (line.startsWith("balance")) {
                balance = Double.parseDouble(line.split("=")[1]);
            }
        }
    }

    public void addSale(double amount){
        balance += amount;
        saveBalance();
    }

    private void saveBalance() {
        List<String> lines = new ArrayList<>();
        lines.add("balance=" + balance);
        FileManager.writeLines(filename, lines);
    }

    public double getBalance() {
        return balance;
    }

    public void processReturn(Product product, int amount) {
        balance -= product.getPrice() * amount;
        saveBalance();
    }
}
