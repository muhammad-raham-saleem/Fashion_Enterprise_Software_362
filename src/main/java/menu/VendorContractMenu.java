package menu;

import model.*;
import service.VendorContractService;
import java.util.Scanner;

public class VendorContractMenu {
    private final Scanner sc;
    private final VendorContractService vendorService;
    private final FinanceManager financeManager;
    private final LegalOfficer legalOfficer;

    public VendorContractMenu(Scanner sc, VendorContractService vendorService) {
        this.sc = sc;
        this.vendorService = vendorService;
        // Initialize default finance manager and legal officer for testing
        this.financeManager = new FinanceManager("John Smith", "Finance", "Finance Manager", 85000);
        this.legalOfficer = new LegalOfficer("Sarah Johnson", "Legal", "Legal Officer", 90000);
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n=== VENDOR CONTRACT MANAGEMENT MENU ===");
            System.out.println("1. Create New Vendor");
            System.out.println("2. Create New Contract");
            System.out.println("3. View All Contracts");
            System.out.println("4. View Pending Contracts");
            System.out.println("5. Finance Review");
            System.out.println("6. Legal Review");
            System.out.println("7. View Contract Details");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> vendorService.createVendor();
                case 2 -> vendorService.createContract();
                case 3 -> vendorService.viewAllContracts();
                case 4 -> vendorService.viewPendingContracts();
                case 5 -> {
                    System.out.println("\n=== FINANCE MANAGER REVIEW ===");
                    System.out.println("Logged in as: " + financeManager.getName());
                    vendorService.financeReview(financeManager);
                }
                case 6 -> {
                    System.out.println("\n=== LEGAL OFFICER REVIEW ===");
                    System.out.println("Logged in as: " + legalOfficer.getName());
                    vendorService.legalReview(legalOfficer);
                }
                case 7 -> vendorService.viewContractDetails();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Try again.");
            }
        } while (choice != 0);
    }
}
