package menu;
import model.HR;

import java.util.Scanner;
import service.HRService;

public class HRMenu implements Menu {

    private final Scanner sc;
    private final HR hr;
    private final HRService hrService;

    public HRMenu (Scanner sc, HR hr, HRService hrs) {
        this.sc = sc;
        this.hr = hr;
        this.hrService = hrs;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "View Staff List", hrService::viewStaffList),
            new MenuOption(2, "Hire Staff", hrService::hireStaff),
            //new MenuOption(3, "View Pending Employee Performance Reviews", hrService::viewReviews),
            new MenuOption(4, "Approve/Disapprove Employee Performance Review", hrService::approveReview),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
    }

    @Override
    public void start() {

        int choice;
        do {
            System.out.println("\n--- HR MENU ---");
            displayOptions();
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

}