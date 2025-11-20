package model;

import java.util.ArrayList;
import java.util.List;

public class VendorApplication {
    private static int count = 1;

    private int applicationID;
    private Vendor vendor;
    private List<String> submittedDocs;
    private String status; // "Pending", "Approved", "Rejected"

    public VendorApplication(Vendor vendor) {
        this.applicationID = count++;
        this.vendor = vendor;
        this.submittedDocs = new ArrayList<>();
        this.status = "Pending";
    }

    public int getApplicationID() {
        return applicationID;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public List<String> getSubmittedDocs() {
        return submittedDocs;
    }

    public void addSubmittedDoc(String doc) {
        this.submittedDocs.add(doc);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Application ID: " + applicationID + ", Vendor: " + vendor.getName() +
               ", Status: " + status + ", Documents: " + submittedDocs.size();
    }
}
