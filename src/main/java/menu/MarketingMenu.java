package menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Campaign;
import service.CampaignService;
import service.FashionShowService;

public class MarketingMenu {
    private final Scanner sc;
    private final FashionShowService fashionShowService;
    private final CampaignService campaignService;

    public MarketingMenu(Scanner sc, FashionShowService fashionShowService, CampaignService campaignService) {
        this.sc = sc;
        this.fashionShowService = fashionShowService;
        this.campaignService = campaignService;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n========== MARKETING MENU ==========");
            System.out.println("1. Run Fashion Show");
            System.out.println("2. Campaign Management");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> startFashionShowProcess();
                case 2 -> campaignManagementMenu();
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

    private void campaignManagementMenu() {
        int choice;
        do {
            System.out.println("\n===== CAMPAIGN MANAGEMENT =====");
            System.out.println("1. Create Campaign");
            System.out.println("2. Submit for Budget Approval");
            System.out.println("3. Approve Budget (Finance)");
            System.out.println("4. Upload Campaign Assets");
            System.out.println("5. Submit for Legal Review");
            System.out.println("6. Approve Legal (Legal Counsel)");
            System.out.println("7. Schedule Campaign Launch");
            System.out.println("8. Track Campaign Sales");
            System.out.println("9. Generate Performance Report");
            System.out.println("10. Update Campaign Status");
            System.out.println("11. View Campaign Details");
            System.out.println("12. List All Campaigns");
            System.out.println("0. Return to Marketing Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> createCampaign();
                case 2 -> submitForBudgetApproval();
                case 3 -> approveBudget();
                case 4 -> uploadAssets();
                case 5 -> submitForLegalReview();
                case 6 -> approveLegal();
                case 7 -> scheduleLaunch();
                case 8 -> trackSales();
                case 9 -> generateReport();
                case 10 -> updateCampaignStatus();
                case 11 -> viewCampaignDetails();
                case 12 -> listAllCampaigns();
                case 0 -> System.out.println("Returning to Marketing Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

    private void startFashionShowProcess() {
        System.out.println("------ RUN FASHION SHOW ------");

        List<String> items = new ArrayList<>();
        String item;

        System.out.println("Enter items to include in the show (type 'done' when finished):");
        while (true) {
            item = sc.nextLine().trim();
            if (item.equalsIgnoreCase("done")) break;
            if (!item.isEmpty()) items.add(item);
        }

        System.out.println("Enter Expected Attendance: ");
        int attendance = Integer.parseInt(sc.nextLine());

        System.out.println("Enter venue name: ");
        String venue = sc.nextLine();

        System.out.println("Enter date (yyyy-mm-dd)");
        LocalDate date = LocalDate.parse(sc.nextLine());


        System.out.println("Enter ticket price");
        double ticketPrice = Double.parseDouble(sc.nextLine());

        double profit = attendance * ticketPrice;

        fashionShowService.runFashionShow(items, attendance, venue, profit, date);
    }

    /* CAMPAIGN ITEMS */

    private void createCampaign() {
        System.out.println("\n------ CREATE NEW CAMPAIGN ------");

        System.out.print("Campaign Name: ");
        String name = sc.nextLine();

        System.out.print("Target Collection: ");
        String targetCollection = sc.nextLine();

        System.out.print("Proposed Budget: $");
        double budget = Double.parseDouble(sc.nextLine());

        System.out.print("Start Date (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(sc.nextLine());

        System.out.print("End Date (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(sc.nextLine());

        campaignService.createCampaign(name, targetCollection, budget, startDate, endDate);
    }

    private void submitForBudgetApproval() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());
        campaignService.submitForBudgetApproval(id);
    }

    private void uploadAssets() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());

        System.out.print("Asset Links (e.g., drive/spring_photos): ");
        String assetLinks = sc.nextLine();

        System.out.print("Ad Copy Text: ");
        String adCopy = sc.nextLine();

        campaignService.uploadAssets(id, assetLinks, adCopy);
    }

    private void submitForLegalReview() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());
        campaignService.submitForLegalReview(id);
    }

    private void scheduleLaunch() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());
        campaignService.scheduleLaunch(id);
    }

    private void trackSales() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());

        System.out.print("Sales Revenue to Track: $");
        double revenue = Double.parseDouble(sc.nextLine());

        campaignService.trackSales(id, revenue);
    }

    private void generateReport() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());
        campaignService.generatePerformanceReport(id);
    }

    private void updateCampaignStatus() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());

        System.out.print("Enter new status: ");
        for (int i = 0; i < Campaign.CampaignStatus.values().length; i++) {
            System.out.println((i + 1) + ". " + Campaign.CampaignStatus.values()[i].toString());
        }
        String status = sc.nextLine();

        try {
            campaignService.updateCampaignState(id, Campaign.CampaignStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status.");
        }
    }

    private void viewCampaignDetails() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());
        campaignService.viewCampaignDetails(id);
    }

    private void listAllCampaigns() {
        System.out.println("\n===== ALL CAMPAIGNS =====");
        List<Campaign> campaigns = campaignService.getAllCampaigns();

        if (campaigns.isEmpty()) {
            System.out.println("No campaigns created yet.");
        } else {
            for (Campaign c : campaigns) {
                System.out.println(c);
            }
        }
    }

    private void approveBudget() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());

        System.out.print("Approve or Reject? " + campaignService.getBudget(id) + " (approve/reject): ");
        String decision = sc.nextLine();

        if (decision.equalsIgnoreCase("approve")) {
            campaignService.approveBudget(id);
        } else if (decision.equalsIgnoreCase("reject")) {
            System.out.print("Rejection notes: ");
            String notes = sc.nextLine();
            campaignService.rejectBudget(id, notes);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void approveLegal() {
        System.out.print("Enter Campaign ID: ");
        long id = Long.parseLong(sc.nextLine());

        System.out.print("Approve or Request Changes? (approve/changes): ");
        String decision = sc.nextLine();

        if (decision.equalsIgnoreCase("approve")) {
            campaignService.approveLegal(id);
        } else if (decision.equalsIgnoreCase("changes")) {
            System.out.print("Compliance notes: ");
            String notes = sc.nextLine();
            campaignService.requestLegalChanges(id, notes);
        } else {
            System.out.println("Invalid choice.");
        }
    }
}
