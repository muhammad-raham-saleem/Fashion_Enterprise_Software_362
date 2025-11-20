package model;

import java.util.ArrayList;
import java.util.List;

public class Vendor {
    private static int count = 1;

    private int vendorID;
    private String name;
    private String contactInfo;
    private List<String> documents;
    private boolean approved;
    private boolean blocked;

    public Vendor(String name, String contactInfo) {
        this.vendorID = count++;
        this.name = name;
        this.contactInfo = contactInfo;
        this.documents = new ArrayList<>();
        this.approved = false;
        this.blocked = false;
    }

    public int getVendorID() {
        return vendorID;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void addDocument(String document) {
        this.documents.add(document);
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String toCSV() {
        String docsStr = String.join(";", documents);
        return String.format("%d,%s,%s,%b,%b,%s",
                vendorID, escapeCsv(name), escapeCsv(contactInfo), approved, blocked, escapeCsv(docsStr));
    }

    public static Vendor fromCSV(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length < 6) return null;
        try {
            int id = Integer.parseInt(parts[0]);
            String name = unescapeCsv(parts[1]);
            String contact = unescapeCsv(parts[2]);
            boolean approved = Boolean.parseBoolean(parts[3]);
            boolean blocked = Boolean.parseBoolean(parts[4]);
            String docsStr = unescapeCsv(parts[5]);

            Vendor vendor = new Vendor(name, contact);
            // Set vendorID using reflection or manually
            vendor.vendorID = id;
            vendor.approved = approved;
            vendor.blocked = blocked;

            if (!docsStr.isEmpty()) {
                String[] docs = docsStr.split(";");
                for (String doc : docs) {
                    vendor.addDocument(doc);
                }
            }

            // Update static counter if necessary
            if (id >= count) {
                count = id + 1;
            }

            return vendor;
        } catch (Exception e) {
            System.err.println("Error parsing Vendor from CSV: " + line);
            return null;
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace(",", "\\,").replace("\n", "\\n");
    }

    private static String unescapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\\,", ",").replace("\\n", "\n");
    }

    @Override
    public String toString() {
        return "Vendor ID: " + vendorID + ", Name: " + name +
               ", Contact: " + contactInfo + ", Approved: " + approved +
               ", Blocked: " + blocked + ", Documents: " + documents.size();
    }
}
