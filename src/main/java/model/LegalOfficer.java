package model;

public class LegalOfficer extends Staff {

    public LegalOfficer(String name, String department, String role, int salary) {
        super(name, department, role, salary);
    }

    public void addLegalNotes(VendorContract contract, String notes) {
        contract.setLegalNotes(notes);
    }

    public boolean checkCompliance(VendorContract contract) {
        // Simple compliance check - vendor must have documents and not be blocked
        return contract.getVendor().getDocuments().size() > 0 &&
               !contract.getVendor().isBlocked();
    }
}
