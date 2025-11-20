package service;

import model.OnlineOrder;
import model.OrderLine;
import util.FileManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Handles the "Fulfill Online Order" use case:
 * - Load online orders from CSV file
 * - List open orders
 * - Select an order
 * - Confirm fulfillment
 * - Enter carrier + tracking
 * - Mark order as SHIPPED and save back to CSV
 */
public class OnlineOrderService {

    private static final String ORDERS_FILE = "data/orders.csv";

    private final Scanner sc;
    private final List<OnlineOrder> orders = new ArrayList<>();

    public OnlineOrderService(Scanner sc) {
        this.sc = sc;
        loadOrdersFromCsv();
    }

    /**
     * Loads online orders from data/orders.csv
     * Expected CSV format:
     * orderId,customerName,shippingAddress,status,trackingNumber,productId,productName,quantity
     */
    private void loadOrdersFromCsv() {
        List<String> lines = FileManager.readLines(ORDERS_FILE);
        if (lines == null || lines.size() <= 1) {
            System.out.println("No orders found in " + ORDERS_FILE);
            return;
        }

        Map<String, OnlineOrder> orderMap = new LinkedHashMap<>();

        // Skip header (start from i = 1)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",", -1); // -1 keeps empty columns
            if (parts.length < 8) {
                System.out.println("Skipping malformed order line: " + line);
                continue;
            }

            String orderId = parts[0].trim();
            String customerName = parts[1].trim();
            String shippingAddress = parts[2].trim();
            String status = parts[3].trim();
            String trackingNumber = parts[4].trim();
            String productId = parts[5].trim();
            String productName = parts[6].trim();
            int quantity;

            try {
                quantity = Integer.parseInt(parts[7].trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity in orders file: " + line);
                continue;
            }

            // Get or create OnlineOrder
            OnlineOrder order = orderMap.get(orderId);
            if (order == null) {
                order = new OnlineOrder(orderId, customerName, shippingAddress);
                if (!status.isEmpty()) {
                    order.setStatus(status);
                }
                if (!trackingNumber.isEmpty()) {
                    order.setTrackingNumber(trackingNumber);
                }
                orderMap.put(orderId, order);
            }

            // Add line to order
            OrderLine lineObj = new OrderLine(productId, productName, quantity);
            order.addLine(lineObj);
        }

        orders.clear();
        orders.addAll(orderMap.values());
    }

    public void showOpenOrders() {
        System.out.println("\n=== Online Orders ===");
        if (orders.isEmpty()) {
            System.out.println("No online orders in the system.");
            return;
        }

        for (OnlineOrder order : orders) {
            System.out.println(order.getId() + " | "
                    + order.getCustomerName() + " | "
                    + order.getStatus());
        }
    }

    public void fulfillOnlineOrder() {
        System.out.println("\n=== Fulfill Online Order ===");

        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }

        // 1. Show orders and pick one
        showOpenOrders();
        System.out.print("Enter order ID to fulfill: ");
        String id = sc.nextLine().trim();

        OnlineOrder order = findOrderById(id);
        if (order == null) {
            System.out.println("No order found with ID " + id);
            return;
        }

        if (!"READY_TO_FULFILL".equalsIgnoreCase(order.getStatus())) {
            System.out.println("Order " + id + " is not in READY_TO_FULFILL state.");
            return;
        }

        // 2. Show details
        System.out.println("\nOrder Details:");
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Ship To: " + order.getShippingAddress());
        System.out.println("Lines:");
        for (OrderLine line : order.getLines()) {
            System.out.println(" - Product " + line.getProductId() + " | "
                    + line.getProductName() + " | qty " + line.getQuantity());
        }

        System.out.print("\nConfirm fulfillment of this order? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Order not fulfilled.");
            return;
        }

        // 3. Enter shipping info
        System.out.print("Enter carrier (e.g., UPS, FedEx): ");
        String carrier = sc.nextLine().trim();

        System.out.print("Enter tracking number: ");
        String tracking = sc.nextLine().trim();

        // 4. Mark as shipped (in memory)
        order.setTrackingNumber(tracking);
        order.setStatus("SHIPPED");

        // 5. Save all orders back to CSV
        saveOrdersToCsv();

        System.out.println("\nOrder " + order.getId() + " has been marked as SHIPPED.");
        System.out.println("Carrier: " + carrier);
        System.out.println("Tracking: " + tracking);
    }

    private OnlineOrder findOrderById(String id) {
        for (OnlineOrder order : orders) {
            if (order.getId().equalsIgnoreCase(id)) {
                return order;
            }
        }
        return null;
    }

    /**
     * Writes the current in-memory orders back to data/orders.csv,
     * overwriting the file.
     *
     * CSV format:
     * orderId,customerName,shippingAddress,status,trackingNumber,productId,productName,quantity
     */
    private void saveOrdersToCsv() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            // header
            writer.println("orderId,customerName,shippingAddress,status,trackingNumber,productId,productName,quantity");

            for (OnlineOrder order : orders) {
                String status = order.getStatus() == null ? "" : order.getStatus();
                String tracking = order.getTrackingNumber() == null ? "" : order.getTrackingNumber();

                for (OrderLine line : order.getLines()) {
                    String productId = line.getProductId();
                    String productName = line.getProductName();
                    int quantity = line.getQuantity();

                    writer.println(
                            order.getId() + "," +
                            escape(order.getCustomerName()) + "," +
                            escape(order.getShippingAddress()) + "," +
                            status + "," +
                            tracking + "," +
                            productId + "," +
                            escape(productName) + "," +
                            quantity
                    );
                }
            }

        } catch (IOException e) {
            System.out.println("Error saving orders to " + ORDERS_FILE + ": " + e.getMessage());
        }
    }

    /**
     * CSV escape
     */
    private String escape(String value) {
        if (value == null) return "";
        if (value.contains(",")) {
            return "\"" + value + "\"";
        }
        return value;
    }
}
