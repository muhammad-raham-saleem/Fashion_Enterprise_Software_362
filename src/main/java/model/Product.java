package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Product {
    private int id;
    private String name;
    private double price;
    private int lowStockThreshold;
    private List<String> approvedVendorIDs;

    public Product (int id, String name, double price){
        this.id = id;
        this.name = name;
        this.price = price;
        this.lowStockThreshold = 10; // Default threshold
        this.approvedVendorIDs = new ArrayList<>();
    }

    public Product (int id, String name, double price, int lowStockThreshold, List<String> approvedVendorIDs){
        this.id = id;
        this.name = name;
        this.price = price;
        this.lowStockThreshold = lowStockThreshold;
        this.approvedVendorIDs = approvedVendorIDs != null ? approvedVendorIDs : new ArrayList<>();
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public double getPrice() {return price;}
    public int getLowStockThreshold() {return lowStockThreshold;}
    public List<String> getApprovedVendorIDs() {return approvedVendorIDs;}

    public void setPrice(double price){
        this.price = price;
    }

    public void setLowStockThreshold(int threshold) {
        this.lowStockThreshold = threshold;
    }

    public void setApprovedVendorIDs(List<String> vendorIDs) {
        this.approvedVendorIDs = vendorIDs != null ? vendorIDs : new ArrayList<>();
    }

    public void addApprovedVendor(String vendorID) {
        if (!approvedVendorIDs.contains(vendorID)) {
            approvedVendorIDs.add(vendorID);
        }
    }

    public void removeApprovedVendor(String vendorID) {
        approvedVendorIDs.remove(vendorID);
    }

    // CSV serialization helpers
    public String getApprovedVendorIDsAsString() {
        if (approvedVendorIDs == null || approvedVendorIDs.isEmpty()) {
            return "";
        }
        return String.join("|", approvedVendorIDs);
    }

    public static List<String> parseVendorIDs(String vendorIDString) {
        if (vendorIDString == null || vendorIDString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(vendorIDString.split("\\|"))
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.toList());
    }
}
