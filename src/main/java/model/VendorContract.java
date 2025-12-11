package model;

import java.util.HashMap;
import java.util.Map;

public class VendorContract {
    private static int count = 1;

    private int contractID;
    private Vendor vendor;
    private String status; // "Pending", "Finance Review", "Legal Review", "Approved", "Rejected"
    private String pricingTerms;
    private String sustainabilityReqs;
    private double budgetAmount;
    private String financeNotes;
    private String legalNotes;
    private Map<Integer, Double> catalogPrices; // productId -> vendor price

    public VendorContract(Vendor vendor, String pricingTerms, String sustainabilityReqs, double budgetAmount) {
        this.contractID = count++;
        this.vendor = vendor;
        this.pricingTerms = pricingTerms;
        this.sustainabilityReqs = sustainabilityReqs;
        this.budgetAmount = budgetAmount;
        this.status = "Pending";
        this.financeNotes = "";
        this.legalNotes = "";
        this.catalogPrices = new HashMap<>();
    }

    public void setCatalogPrice(int productId, double price) {
        catalogPrices.put(productId, price);
    }

    public Double getCatalogPrice(int productId) {
        return catalogPrices.get(productId);
    }

    public Map<Integer, Double> getCatalogPrices() {
        return new HashMap<>(catalogPrices);
    }

    public int getContractID() {
        return contractID;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPricingTerms() {
        return pricingTerms;
    }

    public String getSustainabilityReqs() {
        return sustainabilityReqs;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public String getFinanceNotes() {
        return financeNotes;
    }

    public void setFinanceNotes(String financeNotes) {
        this.financeNotes = financeNotes;
    }

    public String getLegalNotes() {
        return legalNotes;
    }

    public void setLegalNotes(String legalNotes) {
        this.legalNotes = legalNotes;
    }

    public void displayDetails() {
        System.out.println("\n=== CONTRACT DETAILS ===");
        System.out.println("Contract ID: " + contractID);
        System.out.println("Vendor: " + vendor.getName());
        System.out.println("Status: " + status);
        System.out.println("Pricing Terms: " + pricingTerms);
        System.out.println("Sustainability Requirements: " + sustainabilityReqs);
        System.out.println("Budget Amount: $" + budgetAmount);
        System.out.println("Finance Notes: " + financeNotes);
        System.out.println("Legal Notes: " + legalNotes);
    }

    public String toCSV() {
        // Serialize catalog prices as "productId:price;productId:price"
        StringBuilder catalogStr = new StringBuilder();
        for (Map.Entry<Integer, Double> entry : catalogPrices.entrySet()) {
            if (catalogStr.length() > 0) catalogStr.append(";");
            catalogStr.append(entry.getKey()).append(":").append(String.format("%.2f", entry.getValue()));
        }

        return String.format("%d,%d,%s,%s,%s,%.2f,%s,%s,%s",
                contractID,
                vendor.getVendorID(),
                escapeCsv(status),
                escapeCsv(pricingTerms),
                escapeCsv(sustainabilityReqs),
                budgetAmount,
                escapeCsv(financeNotes),
                escapeCsv(legalNotes),
                escapeCsv(catalogStr.toString()));
    }

    public static VendorContract fromCSV(String line, java.util.List<Vendor> vendors) {
        String[] parts = line.split(",", 9);
        if (parts.length < 8) return null;
        try {
            int contractId = Integer.parseInt(parts[0]);
            int vendorId = Integer.parseInt(parts[1]);
            String status = unescapeCsv(parts[2]);
            String pricingTerms = unescapeCsv(parts[3]);
            String sustainability = unescapeCsv(parts[4]);
            double budget = Double.parseDouble(parts[5]);
            String financeNotes = unescapeCsv(parts[6]);
            String legalNotes = unescapeCsv(parts[7]);
            String catalogPricesStr = parts.length > 8 ? unescapeCsv(parts[8]) : "";

            // Find vendor
            Vendor vendor = vendors.stream()
                    .filter(v -> v.getVendorID() == vendorId)
                    .findFirst()
                    .orElse(null);

            if (vendor == null) {
                System.err.println("Warning: Vendor not found for contract " + contractId);
                return null;
            }

            VendorContract contract = new VendorContract(vendor, pricingTerms, sustainability, budget);
            contract.contractID = contractId;
            contract.status = status;
            contract.financeNotes = financeNotes;
            contract.legalNotes = legalNotes;

            // Parse catalog prices "productId:price;productId:price"
            if (!catalogPricesStr.isEmpty()) {
                String[] priceEntries = catalogPricesStr.split(";");
                for (String entry : priceEntries) {
                    String[] parts2 = entry.split(":");
                    if (parts2.length == 2) {
                        try {
                            int productId = Integer.parseInt(parts2[0]);
                            double price = Double.parseDouble(parts2[1]);
                            contract.setCatalogPrice(productId, price);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            // Update static counter
            if (contractId >= count) {
                count = contractId + 1;
            }

            return contract;
        } catch (Exception e) {
            System.err.println("Error parsing VendorContract from CSV: " + line);
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
        return "Contract ID: " + contractID + ", Vendor: " + vendor.getName() +
               ", Status: " + status + ", Budget: $" + budgetAmount;
    }
}
