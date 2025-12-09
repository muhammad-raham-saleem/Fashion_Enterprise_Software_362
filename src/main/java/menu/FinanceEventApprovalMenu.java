package menu;

import model.Event;
import service.EventCoordinatorService;

import java.util.List;
import java.util.Scanner;

/**
 * Menu for Finance Manager to approve/reject events
 *
 * @author Sheldon Corkery
 */
public class FinanceEventApprovalMenu implements Menu {

    private final Scanner sc;
    private final EventCoordinatorService eventService;

    public FinanceEventApprovalMenu(Scanner sc, EventCoordinatorService eventService) {
        this.sc = sc;
        this.eventService = eventService;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "View Pending Approvals", this::viewPendingApprovals),
            new MenuOption(2, "Approve Event", this::approveEvent),
            new MenuOption(3, "Reject Event", this::rejectEvent),
            new MenuOption(0, "Return", () -> System.out.println("Returning..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n========== EVENT APPROVAL (FINANCE) ==========");
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

    private void viewPendingApprovals() {
        System.out.println("\n=== PENDING EVENT APPROVALS ===");
        List<Event> pending = eventService.getPendingApprovalEvents();

        if (pending.isEmpty()) {
            System.out.println("No events pending approval");
            return;
        }

        for (Event event : pending) {
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Event ID: " + event.getId());
            System.out.println("Name: " + event.getName());
            System.out.println("Date: " + event.getDate());
            System.out.println("Venue: " + event.getVenue());
            System.out.println("Ticket Price: $" + String.format("%.2f", event.getCost()));
            System.out.println("Capacity: " + event.getCapacity());
            System.out.println("Estimated Upfront Cost: $" + String.format("%.2f", event.getCost() * event.getCapacity() * 0.3));
            System.out.println("Projected Revenue: $" + String.format("%.2f", event.getCost() * event.getCapacity()));
        }
    }

    private void approveEvent() {
        System.out.println("\n--- APPROVE EVENT ---");
        List<Event> pending = eventService.getPendingApprovalEvents();

        if (pending.isEmpty()) {
            System.out.println("No events pending approval");
            return;
        }

        System.out.print("Enter Event ID to approve: ");
        int eventId = sc.nextInt();
        sc.nextLine();

        Event event = pending.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .orElse(null);

        if (event == null) {
            System.out.println("Event not found in pending approvals");
            return;
        }

        System.out.println("\nApproving: " + event.getName());
        System.out.println("This will deduct $" + String.format("%.2f", event.getCost() * event.getCapacity() * 0.3) + " from budget");
        System.out.print("Confirm? (Y/N): ");

        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = eventService.approveEvent(eventId);
            if (success) {
                System.out.println("✓ Event approved!");
            }
        }
    }

    private void rejectEvent() {
        System.out.println("\n--- REJECT EVENT ---");
        List<Event> pending = eventService.getPendingApprovalEvents();

        if (pending.isEmpty()) {
            System.out.println("No events pending approval");
            return;
        }

        System.out.print("Enter Event ID to reject: ");
        int eventId = sc.nextInt();
        sc.nextLine();

        Event event = pending.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .orElse(null);

        if (event == null) {
            System.out.println("Event not found in pending approvals");
            return;
        }

        System.out.println("\nRejecting: " + event.getName());
        System.out.print("Reason for rejection: ");
        String reason = sc.nextLine();

        System.out.print("Confirm rejection? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = eventService.rejectEvent(eventId, reason);
            if (success) {
                System.out.println("✓ Event rejected");
            }
        }
    }
}

