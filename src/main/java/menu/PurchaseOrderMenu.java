package menu;

import model.*;
import service.PurchaseOrderService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PurchaseOrderMenu implements Menu {
    private final Scanner sc;
    private final PurchaseOrderService poService;

    public PurchaseOrderMenu(Scanner sc, PurchaseOrderService poService) {
        this.sc = sc;
        this.poService = poService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "View Low Stock Products", this::viewLowStockProducts),
            new MenuOption(2, "Create Purchase Order", this::createPurchaseOrder),
            new MenuOption(3, "View All Purchase Orders", poService::viewAllPurchaseOrders),
            new MenuOption(4, "View Pending Deliveries", () -> poService.viewPurchaseOrdersByStatus("Pending Delivery")),
            new MenuOption(5, "Receive Purchase Order", this::receivePurchaseOrder),
            new MenuOption(6, "View Purchase Order Details", this::viewPurchaseOrderDetails),
            new MenuOption(7, "Complete Purchase Order", this::completePurchaseOrder),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to main menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n=== PURCHASE ORDER MANAGEMENT ===");
            System.out.println("(Inventory Manager)");
            displayOptions();

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            executeOption(choice);
        } while (choice != 0);
    }

    private void viewLowStockProducts() {
        poService.displayLowStockProducts();
    }

    private void createPurchaseOrder() {
        System.out.println("\n=== CREATE PURCHASE ORDER ===");

        // Step 1: Display low stock products
        List<Product> lowStockProducts = poService.getLowStockProducts();
        if (lowStockProducts.isEmpty()) {
            System.out.println("No low-stock products to order.");
            return;
        }

        poService.displayLowStockProducts();

        // Step 2: Select product
        System.out.print("\nEnter Product ID to reorder (or 0 to cancel): ");
        int productId = sc.nextInt();
        sc.nextLine();

        if (productId == 0) {
            System.out.println("Cancelled.");
            return;
        }

        Product product = poService.getProductRepo().findById(productId);
        if (product == null) {
            System.out.println("Error: Product not found.");
            return;
        }

        // Check if still low stock (alternate scenario 3a)
        int currentStock = poService.getInventory().getStock(productId);
        if (currentStock > product.getLowStockThreshold()) {
            System.out.println("\nProduct is no longer low-stock (another user may have restocked it).");
            System.out.println("Current stock: " + currentStock + ", Threshold: " + product.getLowStockThreshold());
            System.out.println("Cancelling order process.");
            return;
        }

        // Step 3: Get approved vendors for this product
        List<Vendor> approvedVendors = poService.getApprovedVendorsForProduct(productId);

        if (approvedVendors.isEmpty()) {
            // Alternate scenario 4a: No approved vendors
            System.out.println("\nError: No approved vendor assigned to this product.");
            System.out.println("Purchase order cannot be created.");
            return;
        }

        // Step 4: Display approved vendors
        System.out.println("\n=== APPROVED VENDORS FOR " + product.getName() + " ===");
        System.out.println(String.format("%-5s %-25s %-30s", "ID", "Vendor Name", "Contact Info"));
        System.out.println("─".repeat(65));

        for (Vendor vendor : approvedVendors) {
            VendorContract contract = poService.getApprovedContract(vendor.getVendorID());
            String contractInfo = contract != null ? " [Contract #" + contract.getContractID() + "]" : "";
            System.out.println(String.format("%-5d %-25s %-30s %s",
                    vendor.getVendorID(),
                    vendor.getName(),
                    vendor.getContactInfo(),
                    contractInfo));
        }

        // Step 5: Select vendor
        System.out.print("\nEnter Vendor ID (or 0 to cancel): ");
        int vendorId = sc.nextInt();
        sc.nextLine();

        if (vendorId == 0) {
            System.out.println("Cancelled.");
            return;
        }

        Vendor selectedVendor = poService.getVendorContractRepo().findVendorById(vendorId);
        if (selectedVendor == null || !approvedVendors.contains(selectedVendor)) {
            System.out.println("Error: Invalid vendor selection.");
            return;
        }

        // Step 6: Display recommended quantity and pricing info
        int recommendedQty = poService.calculateRecommendedOrderQuantity(productId);
        VendorContract contract = poService.getApprovedContract(vendorId);

        System.out.println("\n=== ORDER DETAILS ===");
        System.out.println("Product: " + product.getName());
        System.out.println("Vendor: " + selectedVendor.getName());
        System.out.println("Current Stock: " + currentStock);
        System.out.println("Low Stock Threshold: " + product.getLowStockThreshold());
        System.out.println("Recommended Order Quantity: " + recommendedQty);
        if (contract != null) {
            System.out.println("Contract Pricing Terms: " + contract.getPricingTerms());
        }

        // Step 7: Enter quantity
        System.out.print("\nEnter order quantity [" + recommendedQty + "]: ");
        String qtyInput = sc.nextLine().trim();
        int quantity = qtyInput.isEmpty() ? recommendedQty : Integer.parseInt(qtyInput);

        // Validation: quantity must be > 0 (alternate scenario 6a)
        if (quantity <= 0) {
            System.out.println("\nError: Order quantity must be greater than zero.");
            return;
        }

        // Step 8: Enter unit cost
        System.out.print("Enter unit cost per item (e.g., 12.99): $");
        double unitCost = sc.nextDouble();
        sc.nextLine();

        if (unitCost <= 0) {
            System.out.println("\nError: Unit cost must be greater than zero.");
            return;
        }

        double totalCost = quantity * unitCost;
        System.out.printf("Total Cost: $%.2f%n", totalCost);

        // Step 9: Enter expected delivery date
        System.out.print("Enter expected delivery date (YYYY-MM-DD) [default: 7 days from today]: ");
        String dateInput = sc.nextLine().trim();
        String expectedDate;

        if (dateInput.isEmpty()) {
            expectedDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            // Validate date format
            try {
                LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                expectedDate = dateInput;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using default (7 days from today).");
                expectedDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }

        // Step 10: Confirm creation
        System.out.println("\n=== CONFIRM PURCHASE ORDER ===");
        System.out.println("Product: " + product.getName() + " (ID: " + productId + ")");
        System.out.println("Vendor: " + selectedVendor.getName() + " (ID: " + vendorId + ")");
        System.out.println("Quantity: " + quantity);
        System.out.printf("Unit Cost: $%.2f%n", unitCost);
        System.out.printf("Total Cost: $%.2f%n", totalCost);
        System.out.println("Expected Delivery: " + expectedDate);
        System.out.print("\nConfirm creation? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (!confirm.equals("y") && !confirm.equals("yes")) {
            System.out.println("Purchase order creation cancelled.");
            return;
        }

        // Step 11: Create purchase order
        String error = poService.createPurchaseOrder(productId, vendorId, quantity, unitCost, expectedDate);
        if (error != null) {
            System.out.println("\n" + error);
        }
    }

    private void receivePurchaseOrder() {
        System.out.println("\n=== RECEIVE PURCHASE ORDER ===");

        // Display pending orders
        List<PurchaseOrder> pendingOrders = poService.getPurchaseOrderRepo().findByStatus("Pending Delivery");
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending purchase orders to receive.");
            return;
        }

        System.out.println("\n=== PENDING DELIVERIES ===");
        for (PurchaseOrder po : pendingOrders) {
            System.out.println(po);
        }

        System.out.print("\nEnter Purchase Order ID to receive (or 0 to cancel): ");
        int poId = sc.nextInt();
        sc.nextLine();

        if (poId == 0) {
            System.out.println("Cancelled.");
            return;
        }

        String error = poService.receivePurchaseOrder(poId);
        if (error != null) {
            System.out.println("\n" + error);
        }
    }

    private void viewPurchaseOrderDetails() {
        System.out.print("\nEnter Purchase Order ID: ");
        int poId = sc.nextInt();
        sc.nextLine();

        PurchaseOrder po = poService.getPurchaseOrderRepo().findById(poId);
        if (po == null) {
            System.out.println("Purchase Order not found.");
        } else {
            po.displayDetails();
        }
    }

    private void completePurchaseOrder() {
        System.out.println("\n=== COMPLETE PURCHASE ORDER ===");

        // Display received orders
        List<PurchaseOrder> receivedOrders = poService.getPurchaseOrderRepo().findByStatus("Received");
        if (receivedOrders.isEmpty()) {
            System.out.println("No received purchase orders to complete.");
            return;
        }

        System.out.println("\n=== RECEIVED ORDERS ===");
        for (PurchaseOrder po : receivedOrders) {
            System.out.println(po);
        }

        System.out.print("\nEnter Purchase Order ID to complete (or 0 to cancel): ");
        int poId = sc.nextInt();
        sc.nextLine();

        if (poId == 0) {
            System.out.println("Cancelled.");
            return;
        }

        String error = poService.completePurchaseOrder(poId);
        if (error != null) {
            System.out.println("\n" + error);
        }
    }
}
