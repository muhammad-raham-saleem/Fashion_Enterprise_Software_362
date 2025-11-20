package menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import service.CampaignService;
import service.FashionShowService;

public class MarketingMenu implements Menu {
    private final Scanner sc;
    private final FashionShowService fashionShowService;
    private final CampaignService campaignService;

    public MarketingMenu(Scanner sc, FashionShowService fashionShowService, CampaignService campaignService) {
        this.sc = sc;
        this.fashionShowService = fashionShowService;
        this.campaignService = campaignService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "Run Fashion Show", this::startFashionShowProcess),
            new MenuOption(2, "Campaign Management", () -> new CampaignMenu(sc, campaignService).start()),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
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

            executeOption(choice);
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
}
