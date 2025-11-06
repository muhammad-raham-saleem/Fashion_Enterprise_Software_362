package shipping;

public class Inventory {
    private String sku;
    private String location;
    private int onHand;
    private int inTransit;

    //sm
    public Inventory(String sku, String location, int onHand, int inTransit) {
        this.sku = sku;
        this.location = location;
        this.onHand = onHand;
        this.inTransit = inTransit;
    }

    public String getSku() { return sku; }
    public String getLocation() { return location; }
    public int getOnHand() { return onHand; }
    public int getInTransit() { return inTransit; }

    public void adjustOnHand(int delta) { onHand += delta; }
    public void adjustInTransit(int delta) { inTransit += delta; }

    @Override
    public String toString() {
        return sku + "," + location + "," + onHand + "," + inTransit;
    }
}

