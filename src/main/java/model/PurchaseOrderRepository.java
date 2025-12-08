package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseOrderRepository {
    private final String filePath;
    private List<PurchaseOrder> purchaseOrders;

    public PurchaseOrderRepository(String filePath) {
        this.filePath = filePath;
        this.purchaseOrders = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Purchase orders file not found. Starting with empty list.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                if (line.trim().isEmpty())
                    continue;

                PurchaseOrder po = PurchaseOrder.fromCSV(line);
                if (po != null) {
                    purchaseOrders.add(po);
                }
            }
            System.out.println("Loaded " + purchaseOrders.size() + " purchase orders from file.");
        } catch (IOException e) {
            System.err.println("Error loading purchase orders: " + e.getMessage());
        }
    }

    private void saveToFile() {
        File file = new File(filePath);

        // Ensure parent directory exists
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write(
                    "purchaseOrderId,productId,productName,vendorId,vendorName,contractId,quantity,unitCost,totalCost,expectedDeliveryDate,status,createdDate,receivedDate");
            writer.newLine();

            // Write all purchase orders
            for (PurchaseOrder po : purchaseOrders) {
                writer.write(po.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving purchase orders: " + e.getMessage());
        }
    }

    // CRUD operations
    public void addPurchaseOrder(PurchaseOrder po) {
        purchaseOrders.add(po);
        saveToFile();
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return new ArrayList<>(purchaseOrders);
    }

    public PurchaseOrder findById(int purchaseOrderId) {
        return purchaseOrders.stream()
                .filter(po -> po.getPurchaseOrderId() == purchaseOrderId)
                .findFirst()
                .orElse(null);
    }

    public List<PurchaseOrder> findByProductId(int productId) {
        return purchaseOrders.stream()
                .filter(po -> po.getProductId() == productId)
                .collect(Collectors.toList());
    }

    public List<PurchaseOrder> findByVendorId(int vendorId) {
        return purchaseOrders.stream()
                .filter(po -> po.getVendorId() == vendorId)
                .collect(Collectors.toList());
    }

    public List<PurchaseOrder> findByStatus(String status) {
        return purchaseOrders.stream()
                .filter(po -> po.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public void updatePurchaseOrder(PurchaseOrder po) {
        PurchaseOrder existing = findById(po.getPurchaseOrderId());
        if (existing != null) {
            purchaseOrders.remove(existing);
            purchaseOrders.add(po);
            saveToFile();
        }
    }

    public void deletePurchaseOrder(int purchaseOrderId) {
        purchaseOrders.removeIf(po -> po.getPurchaseOrderId() == purchaseOrderId);
        saveToFile();
    }

    // Utility methods
    public List<PurchaseOrder> getPendingOrders() {
        return findByStatus("Pending Delivery");
    }

    public List<PurchaseOrder> getReceivedOrders() {
        return findByStatus("Received");
    }

    public int getOrderCount() {
        return purchaseOrders.size();
    }
}
