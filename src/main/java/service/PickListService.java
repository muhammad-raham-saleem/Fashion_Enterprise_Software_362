package service;

import model.OnlineOrder;
import model.OrderLine;
import model.PickItem;
import model.PickList;
import util.FileManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickListService {

    private static final String PICKLIST_FILE = "data/picklists.csv";

    private final OnlineOrderService orderService;

    public PickListService(OnlineOrderService orderService) {
        this.orderService = orderService;
        ensureHeader();
        loadLocations();
    }

    private void ensureHeader() {
        List<String> lines = FileManager.readLines(PICKLIST_FILE);
        if (lines == null || lines.isEmpty()) {
            FileManager.appendLine(PICKLIST_FILE, "pickListId,orderId,createdAt,productId,productName,quantity,location");
        }
    }
private Map<Integer, String> locationMap = new HashMap<>();

private void loadLocations() {
    List<String> lines = FileManager.readLines("data/locations.csv");
    if (lines == null || lines.size() <= 1) return;

    for (int i = 1; i < lines.size(); i++) {
        String line = lines.get(i).trim();
        if (line.isEmpty()) continue;

        String[] parts = line.split(",", -1);
        if (parts.length < 2) continue;

        try {
            int productId = Integer.parseInt(parts[0].trim());
            String location = parts[1].trim();
            locationMap.put(productId, location);
        } catch (NumberFormatException ignored) {}
    }
}

    public void generatePickListForOrder(String orderId) {
        OnlineOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId);
            return;
        }
        if (!"READY_TO_FULFILL".equalsIgnoreCase(order.getStatus())) {
            System.out.println("Order " + orderId + " is not READY_TO_FULFILL (current status: " + order.getStatus() + ")");
            return;
        }

        String pickListId = nextPickListId();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        PickList pickList = new PickList(pickListId, order.getId(), date);

       //sets location
        for (OrderLine l : order.getLines()) {
            int pid = Integer.parseInt(l.getProductId());
            String loc = locationMap.getOrDefault(pid, "");
            pickList.addItem(new PickItem(l.getProductId(), l.getProductName(), l.getQuantity(), loc));
                    }

        // Persist each pick item as one row
        for (PickItem it : pickList.getItems()) {
            String row = String.join(",",
                    pickList.getId(),
                    pickList.getOrderId(),
                    pickList.getCreatedAt(),
                    it.getProductId(),
                    escape(it.getProductName()),
                    String.valueOf(it.getQuantity()),
                    it.getLocation()
            );
            FileManager.appendLine(PICKLIST_FILE, row);
        }

        // Update order status and save back to CSV
        order.setStatus("PICKING");
        orderService.saveOrders();

        System.out.println("Pick list " + pickListId + " successfully created.\n");
printPickList(pickList);

    }

    private void printPickList(PickList pickList) {
        System.out.println("\n================ PICK LIST ================");
        System.out.println("Pick List ID : " + pickList.getId());
        System.out.println("Order ID     : " + pickList.getOrderId());
        System.out.println("Created On   : " + pickList.getCreatedAt());
        System.out.println("-------------------------------------------");
        System.out.printf("%-10s %-25s %-10s %-15s%n", "Prod ID", "Product Name", "Qty", "Location");
        System.out.println("-------------------------------------------");
    
        for (PickItem item : pickList.getItems()) {
            System.out.printf(
                    "%-10s %-25s %-10d %-15s%n",
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getLocation()
            );
        }
    
        System.out.println("===========================================\n");
    }
    
    private String nextPickListId() {
        List<String> lines = FileManager.readLines(PICKLIST_FILE);
        int n = (lines == null ? 0 : Math.max(0, lines.size() - 1)); // minus header
        return String.format("PL-%04d", n + 1);
    }

    private String escape(String v) {
        if (v == null) return "";
        return v.contains(",") ? "\"" + v + "\"" : v;
    }
}
