package model;

import java.util.*;
import util.FileManager;

public class Inventory {

    private HashMap<Integer, Integer> stock = new HashMap<>();
    private String filename;

    public Inventory(String filename) {
        this.filename = filename;
        loadStock();
    }
    
    private void loadStock() {
        List<String> lines = util.FileManager.readLines(filename);

        for (String line : lines){
            if(line.startsWith("productId")) continue;

            String [] parts = line.split(",");
            int pid = Integer.parseInt(parts[0]);
            int quantity = Integer.parseInt(parts[1]);
            stock.put(pid, quantity);
        }
    }
    
    public boolean hasStock(int productId){
        return stock.getOrDefault(productId,0) > 0;
    }

    public void reduceStock(int productId){
        stock.put(productId, stock.get(productId) - 1); // Reduces product by 1
        saveStock();
    }

    public void addStock(int productId, int amount){
        stock.put(productId, stock.getOrDefault(productId,0) + amount);
        saveStock();
    }

    private void saveStock() {
        List<String> lines = new ArrayList<>();
        lines.add("productId, quantity");

        for (int pid : stock.keySet()){
            lines.add(pid + "," + stock.get(pid));
        }
        FileManager.writeLines(filename, lines);
    }
}
