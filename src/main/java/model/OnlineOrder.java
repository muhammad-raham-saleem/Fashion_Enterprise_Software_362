package model;

import java.util.ArrayList;
import java.util.List;

public class OnlineOrder {

    private String id;
    private String customerName;
    private String shippingAddress;
    private String customerEmail;  // Email for event invitations
    private String status;         // e.g. "READY_TO_FULFILL", "SHIPPED"
    private String trackingNumber; // can be null until shipped
    private List<OrderLine> lines;

    public OnlineOrder(String id, String customerName, String shippingAddress) {
        this.id = id;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.customerEmail = null;
        this.status = "READY_TO_FULFILL";
        this.lines = new ArrayList<>();
    }

    public OnlineOrder(String id, String customerName, String shippingAddress, String customerEmail) {
        this.id = id;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.customerEmail = customerEmail;
        this.status = "READY_TO_FULFILL";
        this.lines = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public void addLine(OrderLine line) {
        lines.add(line);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
