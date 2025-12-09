package model;

import java.util.ArrayList;
import java.util.List;

public class PickList {
    private final String id;
    private final String orderId;
    private final String createdAt;
    private final List<PickItem> items = new ArrayList<>();

    public PickList(String id, String orderId, String createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getCreatedAt() { return createdAt; }
    public List<PickItem> getItems() { return items; }

    public void addItem(PickItem item) { items.add(item); }
}

