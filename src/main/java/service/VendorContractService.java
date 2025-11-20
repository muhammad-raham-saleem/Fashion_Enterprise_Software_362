package service;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendorContractService {
    private final VendorContractRepository repository;
    private final Scanner sc;

    public VendorContractService(Scanner sc, VendorContractRepository repository) {
        this.repository = repository;
        this.sc = sc;
    }

    public void createVendor() {
        sc.nextLine(); // Consume newline
        System.out.print("Enter vendor name: ");
        String name = sc.nextLine();
        System.out.print("Enter contact info: ");
        String contact = sc.nextLine();

        Vendor vendor = new Vendor(name, contact);

        System.out.print("How many documents to add? ");
        int docCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < docCount; i++) {
            System.out.print("Enter document " + (i + 1) + " name: ");
            String doc = sc.nextLine();
            vendor.addDocument(doc);
        }

        repository.addVendor(vendor);
        System.out.println("Vendor created successfully! Vendor ID: " + vendor.getVendorID());
    }

    public void createContract() {
        List<Vendor> vendors = repository.getAllVendors();
        if (vendors.isEmpty()) {
            System.out.println("No vendors available. Please create a vendor first.");
            return;
        }

        System.out.println("\n=== Available Vendors ===");
        for (Vendor v : vendors) {
            if (!v.isBlocked()) {
                System.out.println(v);
            }
        }

        System.out.print("\nEnter vendor ID: ");
        int vendorId = sc.nextInt();
        sc.nextLine();

        Vendor vendor = repository.findVendorById(vendorId);
        if (vendor == null) {
            System.out.println("Vendor not found!");
            return;
        }

        if (vendor.isBlocked()) {
            System.out.println("This vendor is blocked and cannot have contracts!");
            return;
        }

        System.out.print("Enter pricing terms: ");
        String pricingTerms = sc.nextLine();
        System.out.print("Enter sustainability requirements: ");
        String sustainability = sc.nextLine();
        System.out.print("Enter budget amount: $");
        double budget = sc.nextDouble();

        VendorContract contract = new VendorContract(vendor, pricingTerms, sustainability, budget);
        repository.addContract(contract);
        System.out.println("Contract created successfully! Contract ID: " + contract.getContractID());
    }

    public void viewAllContracts() {
        List<VendorContract> contracts = repository.getAllContracts();
        if (contracts.isEmpty()) {
            System.out.println("No contracts available.");
            return;
        }

        System.out.println("\n=== ALL CONTRACTS ===");
        for (VendorContract contract : contracts) {
            System.out.println(contract);
        }
    }

    public void viewPendingContracts() {
        List<VendorContract> contracts = repository.getAllContracts();
        List<VendorContract> pending = new ArrayList<>();
        for (VendorContract c : contracts) {
            if (c.getStatus().equals("Pending") || c.getStatus().equals("Finance Review")) {
                pending.add(c);
            }
        }

        if (pending.isEmpty()) {
            System.out.println("No pending contracts.");
            return;
        }

        System.out.println("\n=== PENDING CONTRACTS ===");
        for (VendorContract contract : pending) {
            System.out.println(contract);
        }
    }

    public void financeReview(FinanceManager financeManager) {
        viewPendingContracts();

        List<VendorContract> contracts = repository.getAllContracts();
        if (contracts.isEmpty()) {
            return;
        }

        System.out.print("\nEnter contract ID to review: ");
        int contractId = sc.nextInt();
        sc.nextLine();

        VendorContract contract = repository.findContractById(contractId);
        if (contract == null) {
            System.out.println("Contract not found!");
            return;
        }

        contract.displayDetails();

        System.out.print("\nEnter available budget: $");
        double availableBudget = sc.nextDouble();
        sc.nextLine();

        boolean budgetApproved = financeManager.reviewBudget(contract, availableBudget);

        System.out.print("Enter finance notes: ");
        String notes = sc.nextLine();
        financeManager.addFinanceNotes(contract, notes);

        if (!budgetApproved) {
            System.out.println("Budget exceeds available funds. Contract requires revision.");
            System.out.print("Reject contract? (yes/no): ");
            String response = sc.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                contract.setStatus("Rejected");
                repository.updateContract(contract);
                System.out.println("Contract rejected.");
                return;
            }
        }

        System.out.print("Submit for legal review? (yes/no): ");
        String response = sc.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            contract.setStatus("Legal Review");
            repository.updateContract(contract);
            System.out.println("Contract submitted for legal review.");
        }
    }

    public void legalReview(LegalOfficer legalOfficer) {
        List<VendorContract> contracts = repository.getAllContracts();
        List<VendorContract> legalReviewContracts = new ArrayList<>();
        for (VendorContract c : contracts) {
            if (c.getStatus().equals("Legal Review")) {
                legalReviewContracts.add(c);
            }
        }

        if (legalReviewContracts.isEmpty()) {
            System.out.println("No contracts in legal review.");
            return;
        }

        System.out.println("\n=== CONTRACTS IN LEGAL REVIEW ===");
        for (VendorContract contract : legalReviewContracts) {
            System.out.println(contract);
        }

        System.out.print("\nEnter contract ID to review: ");
        int contractId = sc.nextInt();
        sc.nextLine();

        VendorContract contract = repository.findContractById(contractId);
        if (contract == null) {
            System.out.println("Contract not found!");
            return;
        }

        contract.displayDetails();

        boolean compliant = legalOfficer.checkCompliance(contract);
        System.out.println("\nCompliance Check: " + (compliant ? "PASSED" : "FAILED"));

        if (!compliant) {
            System.out.println("Vendor does not meet compliance requirements.");
            System.out.print("Block vendor? (yes/no): ");
            String response = sc.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                contract.getVendor().setBlocked(true);
                contract.setStatus("Rejected");
                repository.updateVendor(contract.getVendor());
                repository.updateContract(contract);
                System.out.println("Vendor blocked and contract rejected.");
                return;
            }
        }

        System.out.print("Enter legal notes: ");
        String notes = sc.nextLine();
        legalOfficer.addLegalNotes(contract, notes);

        System.out.print("Approve contract? (yes/no): ");
        String response = sc.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            contract.setStatus("Approved");
            contract.getVendor().setApproved(true);
            repository.updateVendor(contract.getVendor());
            repository.updateContract(contract);
            System.out.println("Contract approved! Vendor is now an active supplier.");
        } else {
            contract.setStatus("Rejected");
            repository.updateContract(contract);
            System.out.println("Contract rejected.");
        }
    }

    public void viewContractDetails() {
        System.out.print("Enter contract ID: ");
        int contractId = sc.nextInt();

        VendorContract contract = repository.findContractById(contractId);
        if (contract == null) {
            System.out.println("Contract not found!");
            return;
        }

        contract.displayDetails();
    }

    public List<Vendor> getVendors() {
        return repository.getAllVendors();
    }

    public List<VendorContract> getContracts() {
        return repository.getAllContracts();
    }
}
