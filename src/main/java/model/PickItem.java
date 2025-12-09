package model;

public class PickItem {
    private final String productId;
    private final String productName;
    private final int quantity;
    private final String location; 

    public PickItem(String productId, String productName, int quantity, String location) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.location = location == null ? "" : location;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public String getLocation() { return location; }
}
