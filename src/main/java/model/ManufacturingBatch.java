package model;

import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a batch of manufactured products.
 * @author Sheldon Corkery
 */
public class ManufacturingBatch {
    private int id;
    private Product product;
    private List<Item> items;
    private InspectionReport report;

    public ManufacturingBatch(int id, Product product, List<Item> items) {
        this.id = id;
        this.product = product;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    /**
     * Get a sample of items based on the given percentage.
     * @param percentage Percentage of items to sample (0.0 to 1.0)
     * @return List of sampled items
     */
    private List<Item> getSampleItems(double percentage) {
        int length = items.size();
        int count = (int) (length * percentage);

        if (count > length) {
            count = length;
        }

        List<Item> sampledItems = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int randomIndex = rand.nextInt(length);
            Item item = items.get(randomIndex);
            sampledItems.add(item);
        }

        return sampledItems;
    }

    public List<Item> startInspection(double percentage) {
        List<Item> items = getSampleItems(percentage);
        report = new InspectionReport(0, this, new ArrayList<>());
        return items;
    }

    public InspectionReport endInspection(InspectionReport.Status status) {
        report.sealReport(status);
        return report;
    }
}
