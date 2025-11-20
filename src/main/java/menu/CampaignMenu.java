package menu;

import model.Campaign;
import service.CampaignService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Menu for managing marketing campaigns.
 * @author Sheldon Corkery
 */
public class CampaignMenu implements Menu {
    private final Scanner sc;
    private final CampaignService campaignService;

    public CampaignMenu(Scanner sc, CampaignService campaignService) {
        this.sc = sc;
        this.campaignService = campaignService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
                new MenuOption(1, "Create Campaign", this::createCampaign),
                new MenuOption(2, "Submit for Budget Approval", this::submitForBudgetApproval),
                new MenuOption(3, "Approve Budget (Finance)", this::approveBudget),
                new MenuOption(4, "Upload Campaign Assets", this::uploadAssets),
                new MenuOption(5, "Submit for Legal Review", this::submitForLegalReview),
                new MenuOption(6, "Approve Legal (Legal Counsel)", this::approveLegal),
                new MenuOption(7, "Schedule Campaign Launch", this::scheduleLaunch),
                new MenuOption(8, "Track Campaign Sales", this::trackSales),
                new MenuOption(9, "Generate Performance Report", this::generateReport),
                new MenuOption(10, "Update Campaign Status", this::updateCampaignStatus),
                new MenuOption(11, "View Campaign Details", this::viewCampaignDetails),
                new MenuOption(12, "List All Campaigns", this::listAllCampaigns),
                new MenuOption(0, "Return to Marketing Menu", () -> System.out.println("Returning to Marketing Menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n===== CAMPAIGN MANAGEMENT =====");
            displayOptions();

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            executeOption(choice);
        } while (choice != 0);
    }

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
