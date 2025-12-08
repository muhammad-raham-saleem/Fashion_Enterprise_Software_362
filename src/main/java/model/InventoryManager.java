package model;

public class InventoryManager extends Staff {

    public InventoryManager(String name, String department, String role, int salary) {
        super(name, department, role, salary);
    }

    // Domain-specific business logic methods
    public boolean isLowStock(Product product, int currentStock) {
        return currentStock <= product.getLowStockThreshold();
    }

    public int calculateReorderQuantity(Product product, int currentStock) {
        int threshold = product.getLowStockThreshold();
        return Math.max((threshold * 2) - currentStock, 1);
    }

    public boolean canOrderFromVendor(Product product, Vendor vendor) {
        return product.getApprovedVendorIDs().contains(String.valueOf(vendor.getVendorID()))
               && vendor.isApproved()
               && !vendor.isBlocked();
    }
}
