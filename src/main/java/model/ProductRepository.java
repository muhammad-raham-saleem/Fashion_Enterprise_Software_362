package model;

import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public record ProductRepository(String filePath) implements Repository<Product> {

    @Override
    public List<Product> getAll() {
        List<String> lines = FileManager.readLines(filePath);
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            try {
                products.add(parseLine(lines.get(i)));
            } catch (Exception _) {
            }
        }

        return products;
    }

    @Override
    public Product findById(int id) {
        List<String> lines = FileManager.readLines(filePath);

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");

            if (Integer.parseInt(parts[0]) == id) {
                return parseLine(lines.get(i));
            }
        }

        return null;
    }

    @Override
    public Product findByName(String name) {
        List<String> lines = FileManager.readLines(filePath);

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");

            if (parts[1].equalsIgnoreCase(name)) {
                return parseLine(lines.get(i));
            }
        }

        return null;
    }

    @Override
    public Product parseLine(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        double price = Double.parseDouble(parts[2]);
        return new Product(id, name, price);
    }
}
