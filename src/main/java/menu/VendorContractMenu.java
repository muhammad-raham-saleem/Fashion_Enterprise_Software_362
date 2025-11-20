package menu;

import model.*;
import service.VendorContractService;
import java.util.Scanner;

public class VendorContractMenu implements Menu {
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

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "Create New Vendor", vendorService::createVendor),
            new MenuOption(2, "Create New Contract", vendorService::createContract),
            new MenuOption(3, "View All Contracts", vendorService::viewAllContracts),
            new MenuOption(4, "View Pending Contracts", vendorService::viewPendingContracts),
            new MenuOption(5, "Finance Review", () -> {
                System.out.println("\n=== FINANCE MANAGER REVIEW ===");
                System.out.println("Logged in as: " + financeManager.getName());
                vendorService.financeReview(financeManager);
            }),
            new MenuOption(6, "Legal Review", () -> {
                System.out.println("\n=== LEGAL OFFICER REVIEW ===");
                System.out.println("Logged in as: " + legalOfficer.getName());
                vendorService.legalReview(legalOfficer);
            }),
            new MenuOption(7, "View Contract Details", vendorService::viewContractDetails),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to main menu..."))
        };
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n=== VENDOR CONTRACT MANAGEMENT MENU ===");
            displayOptions();

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            executeOption(choice);
        } while (choice != 0);
    }
}
