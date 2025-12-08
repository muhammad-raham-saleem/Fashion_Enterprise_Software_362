package service;

import model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepo;
    private final ProductRepository productRepo;
    private final VendorContractRepository vendorContractRepo;
    private final Inventory inventory;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepo,
                                 ProductRepository productRepo,
                                 VendorContractRepository vendorContractRepo,
                                 Inventory inventory) {
        this.purchaseOrderRepo = purchaseOrderRepo;
        this.productRepo = productRepo;
        this.vendorContractRepo = vendorContractRepo;
        this.inventory = inventory;
    }

    /**
     * Get all products that are at or below their low-stock threshold
     */
    public List<Product> getLowStockProducts() {
        List<Product> allProducts = productRepo.getAll();
        List<Product> lowStockProducts = new ArrayList<>();

        for (Product product : allProducts) {
            int currentStock = inventory.getStock(product.getId());
            if (currentStock <= product.getLowStockThreshold()) {
                lowStockProducts.add(product);
            }
        }

        return lowStockProducts;
    }

    /**
     * Display low-stock products with their current stock and recommended order quantity
     */
    public void displayLowStockProducts() {
        List<Product> lowStockProducts = getLowStockProducts();

        if (lowStockProducts.isEmpty()) {
            System.out.println("\n✓ No low-stock products found. All inventory levels are adequate.");
            return;
        }

        System.out.println("\n=== LOW STOCK PRODUCTS ===");
        System.out.println(String.format("%-5s %-25s %-12s %-12s %-20s",
                "ID", "Product Name", "Current", "Threshold", "Recommended Order"));
        System.out.println("─".repeat(80));

        for (Product product : lowStockProducts) {
            int currentStock = inventory.getStock(product.getId());
            int recommended = calculateRecommendedOrderQuantity(product.getId());
            System.out.println(String.format("%-5d %-25s %-12d %-12d %-20d",
                    product.getId(),
                    product.getName(),
                    currentStock,
                    product.getLowStockThreshold(),
                    recommended));
        }
        System.out.println("=".repeat(80));
    }

    /**
     * Calculate recommended order quantity: (threshold × 2) - currentStock
     */
    public int calculateRecommendedOrderQuantity(int productId) {
        Product product = productRepo.findById(productId);
        if (product == null) return 0;

        int currentStock = inventory.getStock(productId);
        int threshold = product.getLowStockThreshold();
        int recommended = (threshold * 2) - currentStock;

        return Math.max(recommended, 1); // At least order 1 unit
    }

    /**
     * Get all approved vendors for a specific product
     */
    public List<Vendor> getApprovedVendorsForProduct(int productId) {
        Product product = productRepo.findById(productId);
        if (product == null) {
            return new ArrayList<>();
        }

        List<String> approvedVendorIDs = product.getApprovedVendorIDs();
        List<Vendor> approvedVendors = new ArrayList<>();

        for (String vendorIdStr : approvedVendorIDs) {
            try {
                int vendorId = Integer.parseInt(vendorIdStr);
                Vendor vendor = vendorContractRepo.findVendorById(vendorId);
                if (vendor != null && vendor.isApproved() && !vendor.isBlocked()) {
                    approvedVendors.add(vendor);
                }
            } catch (NumberFormatException e) {
                // Skip invalid vendor IDs
            }
        }

        return approvedVendors;
    }

    /**
     * Get approved contract for a vendor (if exists)
     */
    public VendorContract getApprovedContract(int vendorId) {
        List<VendorContract> contracts = vendorContractRepo.getAllContracts();
        return contracts.stream()
                .filter(c -> c.getVendor().getVendorID() == vendorId)
                .filter(c -> "Approved".equals(c.getStatus()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Create a new purchase order
     * Returns error message if validation fails, null if successful
     */
    public String createPurchaseOrder(int productId, int vendorId, int quantity, double unitCost, String expectedDeliveryDate) {
        // Validation 1: Product exists
        Product product = productRepo.findById(productId);
        if (product == null) {
            return "Error: Product not found.";
        }

        // Validation 2: Vendor exists and is approved
        Vendor vendor = vendorContractRepo.findVendorById(vendorId);
        if (vendor == null) {
            return "Error: Vendor not found.";
        }
        if (!vendor.isApproved()) {
            return "Error: Vendor is not approved.";
        }
        if (vendor.isBlocked()) {
            return "Error: Vendor is blocked.";
        }

        // Validation 3: Product has this vendor in approved list
        if (!product.getApprovedVendorIDs().contains(String.valueOf(vendorId))) {
            return "Error: Vendor is not approved for this product.";
        }

        // Validation 4: Approved contract exists
        VendorContract contract = getApprovedContract(vendorId);
        if (contract == null) {
            return "Error: No approved vendor contract exists for this vendor.";
        }

        // Validation 5: Valid quantity
        if (quantity <= 0) {
            return "Error: Order quantity must be greater than zero.";
        }

        // Validation 6: Valid unit cost
        if (unitCost <= 0) {
            return "Error: Unit cost must be greater than zero.";
        }

        // Create purchase order
        PurchaseOrder po = new PurchaseOrder(product, vendor, contract, quantity, unitCost, expectedDeliveryDate);
        purchaseOrderRepo.addPurchaseOrder(po);

        System.out.println("\n✓ Purchase Order created successfully!");
        po.displayDetails();

        return null; // Success
    }

    /**
     * Receive a purchase order and update inventory
     */
    public String receivePurchaseOrder(int purchaseOrderId) {
        PurchaseOrder po = purchaseOrderRepo.findById(purchaseOrderId);
        if (po == null) {
            return "Error: Purchase Order not found.";
        }

        if (!"Pending Delivery".equals(po.getStatus())) {
            return "Error: Only orders with status 'Pending Delivery' can be received.";
        }

        // Update inventory
        inventory.addStock(po.getProductId(), po.getQuantity());

        // Update PO status
        po.setStatus("Received");
        purchaseOrderRepo.updatePurchaseOrder(po);

        System.out.println("\n✓ Purchase Order received successfully!");
        System.out.println("Added " + po.getQuantity() + " units of " + po.getProductName() + " to inventory.");
        System.out.println("New stock level: " + inventory.getStock(po.getProductId()));

        return null; // Success
    }

    /**
     * Mark a received order as completed
     */
    public String completePurchaseOrder(int purchaseOrderId) {
        PurchaseOrder po = purchaseOrderRepo.findById(purchaseOrderId);
        if (po == null) {
            return "Error: Purchase Order not found.";
        }

        if (!"Received".equals(po.getStatus())) {
            return "Error: Only received orders can be marked as completed.";
        }

        po.setStatus("Completed");
        purchaseOrderRepo.updatePurchaseOrder(po);

        System.out.println("\n✓ Purchase Order marked as completed!");
        return null; // Success
    }

    /**
     * View all purchase orders
     */
    public void viewAllPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderRepo.getAllPurchaseOrders();
        if (orders.isEmpty()) {
            System.out.println("\nNo purchase orders found.");
            return;
        }

        System.out.println("\n=== ALL PURCHASE ORDERS ===");
        for (PurchaseOrder po : orders) {
            System.out.println(po);
        }
        System.out.println("Total: " + orders.size() + " orders");
    }

    /**
     * View purchase orders by status
     */
    public void viewPurchaseOrdersByStatus(String status) {
        List<PurchaseOrder> orders = purchaseOrderRepo.findByStatus(status);
        if (orders.isEmpty()) {
            System.out.println("\nNo purchase orders found with status: " + status);
            return;
        }

        System.out.println("\n=== PURCHASE ORDERS - " + status.toUpperCase() + " ===");
        for (PurchaseOrder po : orders) {
            po.displayDetails();
        }
        System.out.println("Total: " + orders.size() + " orders");
    }

    // Getters for repositories 
    public PurchaseOrderRepository getPurchaseOrderRepo() {
        return purchaseOrderRepo;
    }

    public ProductRepository getProductRepo() {
        return productRepo;
    }

    public VendorContractRepository getVendorContractRepo() {
        return vendorContractRepo;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
