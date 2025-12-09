package menu;

import java.util.Scanner;

import service.CampaignService;
import service.EventCoordinatorService;
import service.FashionShowService;

public class MarketingMenu implements Menu {
    private final Scanner sc;
    private final CampaignService campaignService;
    private final EventCoordinatorService eventCoordinatorService;
    private final FashionShowService fashionShowService;

    public MarketingMenu(Scanner sc, CampaignService campaignService,
                        EventCoordinatorService eventCoordinatorService,
                        FashionShowService fashionShowService) {
        this.sc = sc;
        this.campaignService = campaignService;
        this.eventCoordinatorService = eventCoordinatorService;
        this.fashionShowService = fashionShowService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "Campaign Management", () -> new CampaignMenu(sc, campaignService).start()),
            new MenuOption(2, "Event Coordination", () -> new EventCoordinatorMenu(sc, eventCoordinatorService).start()),
            new MenuOption(3, "Fashion Show Management", () -> new FashionShowMenu(sc, fashionShowService).start()),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n========== MARKETING MENU ==========");
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
}
