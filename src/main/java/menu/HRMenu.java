package menu;
import model.HR;

import java.util.Scanner;
import service.HRService;

public class HRMenu {

    private final Scanner sc;
    private final HR hr;
    private final HRService hrService;

    public HRMenu (Scanner sc, HR hr, HRService hrs) {
        this.sc = sc;
        this.hr = hr;
        this.hrService = hrs;
    }

    public void start() {

        int choice;
        do {
            System.out.println("\n--- HR MENU ---");
            System.out.println("1. View Staff List");
            System.out.println("2. Hire Staff");
            System.out.println("0. Return to Main Menu");
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> hrService.viewStaffList();
                case 2 -> hrService.hireStaff();
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);

    }

}