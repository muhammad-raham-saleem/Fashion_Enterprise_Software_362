package menu;

import service.EventCoordinatorService;
import service.FinanceService;

import java.util.Scanner;

/**
 * Finance Department Menu - handles financial operations and approvals
 *
 * @author Sheldon Corkery
 */
public class FinanceMenu implements Menu {

    private final Scanner sc;
    private final FinanceService financeService;
    private final EventCoordinatorService eventCoordinatorService;

    public FinanceMenu(Scanner sc, FinanceService financeService, EventCoordinatorService eventCoordinatorService) {
        this.sc = sc;
        this.financeService = financeService;
        this.eventCoordinatorService = eventCoordinatorService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "View Financial Summary", this::viewFinancialSummary),
            new MenuOption(2, "Event Approvals", () -> new FinanceEventApprovalMenu(sc, eventCoordinatorService).start()),
            new MenuOption(0, "Return to Main Menu", () -> System.out.println("Returning to Main Menu..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n========== FINANCE DEPARTMENT ==========");
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

    private void viewFinancialSummary() {
        System.out.println("\n=== FINANCIAL SUMMARY ===");
        System.out.println("Total Revenue: $" + String.format("%.2f", financeService.getTotalRevenue()));
        System.out.println("Total Expenses: $" + String.format("%.2f", financeService.getTotalExpenses()));
        System.out.println("Net Profit: $" + String.format("%.2f",
            financeService.getTotalRevenue() - financeService.getTotalExpenses()));
    }
}

