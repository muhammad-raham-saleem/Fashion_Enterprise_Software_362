package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PurchaseOrder {
    private static int count = 1;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private int purchaseOrderId;
    private int productId;
    private String productName;
    private int vendorId;
    private String vendorName;
    private int contractId;
    private int quantity;
    private double unitCost;
    private double totalCost;
    private String expectedDeliveryDate;
    private String status; // "Draft", "Pending Delivery", "Received", "Completed"
    private String createdDate;
    private String receivedDate;

    // Constructor for creating new PO
    public PurchaseOrder(Product product, Vendor vendor, VendorContract contract, int quantity, double unitCost, String expectedDeliveryDate) {
        this.purchaseOrderId = count++;
        this.productId = product.getId();
        this.productName = product.getName();
        this.vendorId = vendor.getVendorID();
        this.vendorName = vendor.getName();
        this.contractId = contract.getContractID();
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.totalCost = calculateTotalCost();
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.status = "Pending Delivery";
        this.createdDate = LocalDate.now().format(DATE_FORMATTER);
        this.receivedDate = "";
    }

    // Full constructor for loading from CSV
    public PurchaseOrder(int purchaseOrderId, int productId, String productName, int vendorId, String vendorName,
                         int contractId, int quantity, double unitCost, double totalCost, String expectedDeliveryDate,
                         String status, String createdDate, String receivedDate) {
        this.purchaseOrderId = purchaseOrderId;
        this.productId = productId;
        this.productName = productName;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.contractId = contractId;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.status = status;
        this.createdDate = createdDate;
        this.receivedDate = receivedDate;
    }

    // Getters
    public int getPurchaseOrderId() { return purchaseOrderId; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getVendorId() { return vendorId; }
    public String getVendorName() { return vendorName; }
    public int getContractId() { return contractId; }
    public int getQuantity() { return quantity; }
    public double getUnitCost() { return unitCost; }
    public double getTotalCost() { return totalCost; }
    public String getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public String getStatus() { return status; }
    public String getCreatedDate() { return createdDate; }
    public String getReceivedDate() { return receivedDate; }

    // Setters
    public void setStatus(String status) {
        this.status = status;
        if ("Received".equals(status) && (receivedDate == null || receivedDate.isEmpty())) {
            this.receivedDate = LocalDate.now().format(DATE_FORMATTER);
        }
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalCost = calculateTotalCost();
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
        this.totalCost = calculateTotalCost();
    }

    // Helper method
    private double calculateTotalCost() {
        return quantity * unitCost;
    }

    public void displayDetails() {
        System.out.println("\n=== PURCHASE ORDER DETAILS ===");
        System.out.println("PO ID: " + purchaseOrderId);
        System.out.println("Product: " + productName + " (ID: " + productId + ")");
        System.out.println("Vendor: " + vendorName + " (ID: " + vendorId + ")");
        System.out.println("Contract ID: " + contractId);
        System.out.println("Quantity: " + quantity);
        System.out.printf("Unit Cost: $%.2f%n", unitCost);
        System.out.printf("Total Cost: $%.2f%n", totalCost);
        System.out.println("Expected Delivery: " + expectedDeliveryDate);
        System.out.println("Status: " + status);
        System.out.println("Created: " + createdDate);
        if (receivedDate != null && !receivedDate.isEmpty()) {
            System.out.println("Received: " + receivedDate);
        }
        System.out.println("=============================");
    }

    // CSV serialization
    public String toCSV() {
        return String.format("%d,%d,%s,%d,%s,%d,%d,%.2f,%.2f,%s,%s,%s,%s",
                purchaseOrderId,
                productId,
                escapeCsv(productName),
                vendorId,
                escapeCsv(vendorName),
                contractId,
                quantity,
                unitCost,
                totalCost,
                escapeCsv(expectedDeliveryDate),
                escapeCsv(status),
                escapeCsv(createdDate),
                escapeCsv(receivedDate));
    }

    public static PurchaseOrder fromCSV(String line) {
        String[] parts = line.split(",", 13);
        if (parts.length < 13) return null;

        try {
            int poId = Integer.parseInt(parts[0]);
            int prodId = Integer.parseInt(parts[1]);
            String prodName = unescapeCsv(parts[2]);
            int vendId = Integer.parseInt(parts[3]);
            String vendName = unescapeCsv(parts[4]);
            int contractId = Integer.parseInt(parts[5]);
            int qty = Integer.parseInt(parts[6]);
            double unitCost = Double.parseDouble(parts[7]);
            double totalCost = Double.parseDouble(parts[8]);
            String expectedDate = unescapeCsv(parts[9]);
            String status = unescapeCsv(parts[10]);
            String createdDate = unescapeCsv(parts[11]);
            String receivedDate = unescapeCsv(parts[12]);

            PurchaseOrder po = new PurchaseOrder(poId, prodId, prodName, vendId, vendName, contractId,
                    qty, unitCost, totalCost, expectedDate, status, createdDate, receivedDate);

            // Update static counter to avoid ID conflicts
            if (poId >= count) {
                count = poId + 1;
            }

            return po;
        } catch (Exception e) {
            System.err.println("Error parsing PurchaseOrder CSV: " + e.getMessage());
            return null;
        }
    }

    private static String escapeCsv(String value) {
        if (value == null || value.isEmpty()) return "";
        return value.replace(",", "\\,").replace("\n", "\\n");
    }

    private static String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) return "";
        return value.replace("\\,", ",").replace("\\n", "\n");
    }

    @Override
    public String toString() {
        return String.format("PO#%d - %s x%d from %s - %s",
                purchaseOrderId, productName, quantity, vendorName, status);
    }
}
