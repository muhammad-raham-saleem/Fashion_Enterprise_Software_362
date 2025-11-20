package service;

import model.OnlineOrder;
import model.OrderLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the "Fulfill Online Order" use case:
 * - Select an existing online order
 * - Check / confirm lines
 * - Enter carrier + tracking
 * - Mark order as SHIPPED
 *
 * NOTE: Right now this keeps orders in memory.
 * Later you can:
 *   - load/save orders from a file (like products/inventory)
 *   - integrate with Inventory or TransferService to actually reduce stock
 */
public class OnlineOrderService {

    private final Scanner sc;
    private final List<OnlineOrder> orders = new ArrayList<>();

    public OnlineOrderService(Scanner sc) {
        this.sc = sc;
        seedFakeOrders(); // TEMP: demo data so your menu actually does something
    }

    /**
     * Temporary hard-coded orders just so your demo works.
     * You can delete this later once you persist real orders.
     */
    private void seedFakeOrders() {
        OnlineOrder o1 = new OnlineOrder("ORD-1001", "Alice Smith", "123 Main St");
        o1.addLine(new OrderLine("P001", "Classic Jacket", 1));
        o1.addLine(new OrderLine("P010", "Silk Scarf", 2));

        OnlineOrder o2 = new OnlineOrder("ORD-1002", "Bob Lee", "456 Oak Ave");
        o2.addLine(new OrderLine("P005", "Leather Bag", 1));

        orders.add(o1);
        orders.add(o2);
    }

    public void showOpenOrders() {
        System.out.println("\n=== Online Orders ===");
        if (orders.isEmpty()) {
            System.out.println("No online orders in the system yet.");
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
            System.out.println(" - " + line.getProductId() + " | "
                    + line.getProductName() + " | qty " + line.getQuantity());
        }

        // TODO later: check inventory and reserve stock here

        System.out.print("\nConfirm fulfillment of this order? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Order not fulfilled.");
            return;
        }

        // 3. Enter shipping info
        System.out.print("Enter carrier (e.g. UPS, FedEx): ");
        String carrier = sc.nextLine().trim();

        System.out.print("Enter tracking number: ");
        String tracking = sc.nextLine().trim();

        // 4. Mark as shipped
        order.setTrackingNumber(tracking);
        order.setStatus("SHIPPED");

        // TODO later: call shipping department / TransferService here

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
}
