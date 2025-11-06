package shipping;

public class Transfer {
    private String id;
    private String from;
    private String to;
    private String status; // Draft, Shipped, Received
    private String carrier;
    private String tracking;
    private String shipDate;

    public Transfer(String id, String from, String to) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.status = "Draft";
    }

    public void ship(String carrier, String tracking, String date) {
        this.status = "Shipped";
        this.carrier = carrier;
        this.tracking = tracking;
        this.shipDate = date;
    }

    public String getStatus() { return status; }
    public String getId() { return id; }

    @Override
    public String toString() {
        return id + "," + from + "," + to + "," + status + "," + carrier + "," + tracking + "," + shipDate;
    }
}
