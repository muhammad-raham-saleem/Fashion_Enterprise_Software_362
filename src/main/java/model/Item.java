package model;

/**
 * Represents an item in the manufacturing system.
 * @author Sheldon Corkery
 */
public class Item {
    private int id;
    private Product product;
    private int[] measurements;

    public Item(int id, Product product) {
        this.id = id;
        this.product = product;
    }

    public void updateMeasurements(int[] measurements) {
        this.measurements = measurements;
    }

    public int getId() {
        return id;
    }

    public boolean validateSpecification() {
        // TODO read Specification from Product or file?
        return false;
    }
}
