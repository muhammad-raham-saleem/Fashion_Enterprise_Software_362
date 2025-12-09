package menu;

import model.*;
import service.EventCoordinatorService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Menu for Event Coordinator to manage events
 *
 * @author Sheldon Corkery
 */
public class EventCoordinatorMenu implements Menu {

    private final Scanner sc;
    private final EventCoordinatorService service;

    public EventCoordinatorMenu(Scanner sc, EventCoordinatorService service) {
        this.sc = sc;
        this.service = service;
    }

    @Override
    public MenuOption[] getOptions() {
        return new MenuOption[] {
            new MenuOption(1, "Create New Event", this::createEvent),
            new MenuOption(2, "Select Existing Event", this::selectAndManageEvent),
            new MenuOption(3, "View All Events", this::viewAllEvents),
            new MenuOption(4, "View Approved Events", this::viewApprovedEvents),
            new MenuOption(0, "Return to Marketing Menu", () -> System.out.println("Returning..."))
        };
    }

    @Override
    public void start() {
        int choice;
        do {
            System.out.println("\n========== EVENT COORDINATOR MENU ==========");
            displayOptions();
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            executeOption(choice);
        } while (choice != 0);
    }

    private void createEvent() {
        System.out.println("\n--- CREATE NEW EVENT ---");

        System.out.print("Event Name: ");
        String name = sc.nextLine();

        System.out.print("Venue: ");
        String venue = sc.nextLine();

        System.out.print("Date (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(sc.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
            return;
        }

        System.out.print("Ticket Cost: $");
        double cost = sc.nextDouble();

        System.out.print("Capacity: ");
        int capacity = sc.nextInt();
        sc.nextLine();

        Event event = service.createEvent(name, venue, date, cost, capacity);
        System.out.println("\n✓ Event created successfully!");
        System.out.println("Event ID: " + event.getId());
        System.out.println("Status: " + event.getStatus());
        System.out.println("\nNext step: Submit for financial approval");
    }

    private void viewAllEvents() {
        System.out.println("\n=== ALL EVENTS ===");
        List<Event> events = service.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events found");
            return;
        }

        for (Event event : events) {
            displayEventSummary(event);
        }
    }

    private void viewApprovedEvents() {
        System.out.println("\n=== APPROVED EVENTS ===");
        List<Event> events = service.getApprovedEvents();

        if (events.isEmpty()) {
            System.out.println("No approved events found");
            return;
        }

        for (Event event : events) {
            displayEventSummary(event);
        }
    }

    private void selectAndManageEvent() {
        System.out.println("\n--- SELECT EVENT ---");
        List<Event> events = service.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events available. Create one first!");
            return;
        }

        System.out.println("\nAvailable Events:");
        for (Event event : events) {
            displayEventSummary(event);
        }

        System.out.print("\nEnter Event ID: ");
        int eventId = sc.nextInt();
        sc.nextLine();

        Event event = events.stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .orElse(null);

        if (event == null) {
            System.out.println("Event not found");
            return;
        }

        manageEvent(event);
    }

    private void manageEvent(Event event) {
        int choice;
        do {
            System.out.println("\n========== MANAGING: " + event.getName() + " ==========");
            System.out.println("Status: " + event.getStatus());
            System.out.println("Date: " + event.getDate());
            System.out.println("Venue: " + event.getVenue());
            System.out.println("Capacity: " + event.getCapacity() + " | RSVPs: " + event.getRsvpCount());

            System.out.println("\n1. Submit for Financial Approval");
            System.out.println("2. Manage Invite List");
            System.out.println("3. Send Invitations");
            System.out.println("4. View RSVPs");
            System.out.println("5. Assign Staff");
            System.out.println("6. Lock Event");
            System.out.println("7. Cancel Event");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> submitForApproval(event);
                case 2 -> manageInviteList(event);
                case 3 -> sendInvitations(event);
                case 4 -> viewRSVPs(event);
                case 5 -> assignStaff(event);
                case 6 -> lockEvent(event);
                case 7 -> cancelEvent(event);
                case 0 -> System.out.println("Returning...");
                default -> System.out.println("Invalid option");
            }

        } while (choice != 0);
    }

    private void submitForApproval(Event event) {
        System.out.println("\n--- SUBMIT FOR FINANCIAL APPROVAL ---");

        // Reload event to get latest status
        Event currentEvent = service.getAllEvents().stream()
                .filter(e -> e.getId() == event.getId())
                .findFirst()
                .orElse(event);

        if (currentEvent.getStatus() != Event.EventStatus.DRAFT) {
            System.out.println("Event is not in DRAFT status");
            return;
        }

        System.out.println("Event: " + currentEvent.getName());
        System.out.println("Estimated Cost: $" + String.format("%.2f", currentEvent.getCost() * currentEvent.getCapacity() * 0.3));
        System.out.print("\nConfirm submission? (Y/N): ");

        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            service.submitForFinancialApproval(currentEvent.getId());
        }
    }

    private void manageInviteList(Event event) {
        int choice;
        do {
            System.out.println("\n--- MANAGE INVITE LIST ---");
            System.out.println("Event: " + event.getName());

            List<EventInvite> invites = service.getInviteList(event.getId());
            System.out.println("\nCurrent Invites: " + invites.size());

            for (EventInvite invite : invites) {
                Customer customer = service.getEligibleCustomers().stream()
                        .filter(c -> c.getId() == invite.getCustomerId())
                        .findFirst()
                        .orElse(null);

                if (customer != null) {
                    String vipLabel = invite.isVip() ? " [VIP]" : "";
                    String sentLabel = invite.isSent() ? " (SENT)" : "";
                    System.out.println("  - " + customer.getName() + " (" + customer.getEmail() + ")" + vipLabel + sentLabel);
                }
            }

            System.out.println("\n1. Add Customer");
            System.out.println("2. Add VIP Customer");
            System.out.println("3. Remove Customer");
            System.out.println("4. Pull Customers from Transactions");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addCustomerToInviteList(event, false);
                case 2 -> addCustomerToInviteList(event, true);
                case 3 -> removeCustomerFromInviteList(event);
                case 4 -> pullCustomersFromTransactions();
                case 0 -> System.out.println("Returning...");
                default -> System.out.println("Invalid option");
            }
        } while (choice != 0);
    }

    private void addCustomerToInviteList(Event event, boolean vip) {
        System.out.println("\n--- SELECT CUSTOMER ---");
        List<Customer> customers = service.getEligibleCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers available. Pull from transactions first!");
            return;
        }

        System.out.println("Available Customers:");
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            System.out.println((i + 1) + ". " + c.getName() + " (" + c.getEmail() + ")");
        }

        System.out.print("\nSelect customer (number) or 0 to cancel: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= customers.size()) {
            Customer selected = customers.get(choice - 1);
            service.addCustomerToInviteList(event.getId(), selected.getId(), vip);
        }
    }

    private void removeCustomerFromInviteList(Event event) {
        List<EventInvite> invites = service.getInviteList(event.getId());
        if (invites.isEmpty()) {
            System.out.println("No invites to remove");
            return;
        }

        System.out.println("\nSelect customer to remove:");
        for (int i = 0; i < invites.size(); i++) {
            EventInvite invite = invites.get(i);
            Customer customer = service.getEligibleCustomers().stream()
                    .filter(c -> c.getId() == invite.getCustomerId())
                    .findFirst()
                    .orElse(null);

            if (customer != null) {
                System.out.println((i + 1) + ". " + customer.getName());
            }
        }

        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= invites.size()) {
            EventInvite selected = invites.get(choice - 1);
            service.removeCustomerFromInviteList(event.getId(), selected.getCustomerId());
        }
    }

    private void pullCustomersFromTransactions() {
        System.out.println("\n--- PULL CUSTOMERS FROM TRANSACTIONS ---");
        System.out.println("This feature extracts customer emails from orders and receipts");
        System.out.println("Note: In production, this would scan actual transaction records");
        System.out.println("\nFeature simulated - customers already in system");
    }

    private void sendInvitations(Event event) {
        System.out.println("\n--- SEND INVITATIONS ---");

        if (!event.isFinanciallyApproved()) {
            System.out.println("X Event must be financially approved before sending invites");
            System.out.println("Current status: " + event.getStatus());
            return;
        }

        List<EventInvite> invites = service.getInviteList(event.getId());
        long unsent = invites.stream().filter(i -> !i.isSent()).count();

        System.out.println("Event: " + event.getName());
        System.out.println("Total invites: " + invites.size());
        System.out.println("Unsent: " + unsent);

        if (unsent == 0) {
            System.out.println("All invitations already sent!");
            return;
        }

        System.out.print("\nSend " + unsent + " invitation(s)? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            int sent = service.sendInvites(event.getId());
            System.out.println("\n✓ " + sent + " invitation(s) sent!");
            System.out.println("Emails opened in browser");
        }
    }

    private void viewRSVPs(Event event) {
        System.out.println("\n--- RSVP SUMMARY ---");
        System.out.println("Event: " + event.getName());
        System.out.println("Capacity: " + event.getCapacity());
        System.out.println("Current RSVPs: " + event.getRsvpCount());

        Map<String, Integer> summary = service.getRSVPSummary(event.getId());
        System.out.println("\nBreakdown:");
        System.out.println("  Accepted: " + summary.get("ACCEPTED"));
        System.out.println("  Declined: " + summary.get("DECLINED"));
        System.out.println("  Pending: " + summary.get("PENDING"));
        System.out.println("  Waitlist: " + summary.get("WAITLIST"));

        List<EventRSVP> waitlist = service.getWaitlist(event.getId());
        if (!waitlist.isEmpty()) {
            System.out.println("\nWaitlist:");
            for (EventRSVP rsvp : waitlist) {
                Customer customer = service.getEligibleCustomers().stream()
                        .filter(c -> c.getId() == rsvp.getCustomerId())
                        .findFirst()
                        .orElse(null);
                if (customer != null) {
                    System.out.println("  - " + customer.getName() + " (party of " + rsvp.getPartySize() + ")");
                }
            }
        }

        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void assignStaff(Event event) {
        System.out.println("\n--- ASSIGN STAFF ---");
        System.out.println("Event: " + event.getName());
        System.out.println("Date: " + event.getDate());

        List<Integer> assigned = event.getAssignedStaffIds();
        System.out.println("\nCurrently Assigned: " + assigned.size());

        List<Staff> available = service.getAvailableStaff(event.getDate());

        if (available.isEmpty()) {
            System.out.println("\nX No staff available for this date!");
            System.out.println("Event cannot proceed without staff.");
            System.out.print("\nPress Enter to continue...");
            sc.nextLine();
            return;
        }

        System.out.println("\nAvailable Staff:");
        for (int i = 0; i < available.size(); i++) {
            Staff staff = available.get(i);
            System.out.println((i + 1) + ". " + staff.getName() + " - " + staff.getRole());
        }

        System.out.print("\nSelect staff to assign (number) or 0 to cancel: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= available.size()) {
            Staff selected = available.get(choice - 1);
            service.assignStaffToEvent(event.getId(), selected.getID());
        }
    }

    private void lockEvent(Event event) {
        System.out.println("\n--- LOCK EVENT ---");
        System.out.println("Locking prevents any further changes to invitations");
        System.out.println("Event: " + event.getName());
        System.out.println("RSVPs: " + event.getRsvpCount() + "/" + event.getCapacity());
        System.out.println("Staff Assigned: " + event.getAssignedStaffIds().size());

        System.out.print("\nLock this event? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = service.lockEvent(event.getId());
            if (success) {
                System.out.println("✓ Event locked successfully!");
            }
        }
    }

    private void cancelEvent(Event event) {
        System.out.println("\n--- CANCEL EVENT ---");
        System.out.println("  WARNING: This will notify all invited guests");
        System.out.println("Event: " + event.getName());

        System.out.print("\nReason for cancellation: ");
        String reason = sc.nextLine();

        System.out.print("Confirm cancellation? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            service.cancelEvent(event.getId(), reason);
            System.out.println("✓ Event cancelled");
        }
    }

    private void displayEventSummary(Event event) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("ID: " + event.getId() + " | " + event.getName());
        System.out.println("Status: " + event.getStatus());
        System.out.println("Date: " + event.getDate() + " | Venue: " + event.getVenue());
        System.out.println("Cost: $" + String.format("%.2f", event.getCost()) +
                         " | Capacity: " + event.getCapacity());
        System.out.println("RSVPs: " + event.getRsvpCount() + "/" + event.getCapacity());
        if (event.getRejectionNotes() != null) {
            System.out.println("Notes: " + event.getRejectionNotes());
        }
    }
}

