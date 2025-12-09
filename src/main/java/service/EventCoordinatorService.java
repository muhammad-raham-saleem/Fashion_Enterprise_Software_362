package service;

import model.*;
import util.MessageHandler;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for coordinating events
 *
 * @author Sheldon Corkery
 */
public class EventCoordinatorService {

    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final HR hr;
    private final FinanceService financeService;
    private final MessageHandler messageHandler;

    private final Map<Integer, List<EventInvite>> eventInvites;
    private final Map<Integer, List<EventRSVP>> eventRSVPs;
    private int nextInviteId;
    private int nextRsvpId;

    public EventCoordinatorService(EventRepository eventRepository,
                                   CustomerRepository customerRepository,
                                   HR hr,
                                   FinanceService financeService,
                                   MessageHandler messageHandler) {
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository;
        this.hr = hr;
        this.financeService = financeService;
        this.messageHandler = messageHandler;
        this.eventInvites = new HashMap<>();
        this.eventRSVPs = new HashMap<>();
        this.nextInviteId = 1;
        this.nextRsvpId = 1;
    }

    /**
     * Create a new event (DRAFT status)
     */
    public Event createEvent(String name, String venue, LocalDate date, double cost, int capacity) {
        int eventId = eventRepository.getNextId();
        Event event = new Event(eventId, name, venue, date, cost, capacity);
        eventRepository.add(event);

        eventInvites.put(eventId, new ArrayList<>());
        eventRSVPs.put(eventId, new ArrayList<>());

        System.out.println("Event created: " + event.getName() + " (ID: " + eventId + ")");
        return event;
    }

    /**
     * Submit event for financial approval
     */
    public boolean submitForFinancialApproval(int eventId) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            System.out.println("Event not found");
            return false;
        }

        if (event.getStatus() != Event.EventStatus.DRAFT) {
            System.out.println("Event must be in DRAFT status");
            return false;
        }

        event.setStatus(Event.EventStatus.PENDING_APPROVAL);
        eventRepository.update(event);
        System.out.println("Event submitted for financial approval");
        return true;
    }

    /**
     * Finance Manager approves the event
     */
    public boolean approveEvent(int eventId) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            System.out.println("Event not found");
            return false;
        }

        if (event.getStatus() != Event.EventStatus.PENDING_APPROVAL) {
            System.out.println("Event is not pending approval");
            return false;
        }

        // Deduct budget from finance
        financeService.addExpense(event.getCost() * event.getCapacity() * 0.3); // Estimated 30% upfront cost

        event.setStatus(Event.EventStatus.APPROVED);
        eventRepository.update(event);
        System.out.println("Event approved! Coordinator can now manage invitations.");
        return true;
    }

    /**
     * Finance Manager rejects the event
     */
    public boolean rejectEvent(int eventId, String notes) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            System.out.println("Event not found");
            return false;
        }

        event.setStatus(Event.EventStatus.DRAFT);
        event.setRejectionNotes(notes);
        eventRepository.update(event);
        System.out.println("Event rejected: " + notes);
        return true;
    }

    /**
     * Get all events
     */
    public List<Event> getAllEvents() {
        return eventRepository.getAll();
    }

    /**
     * Get events by status
     */
    public List<Event> getEventsByStatus(Event.EventStatus status) {
        return eventRepository.getAll().stream()
                .filter(e -> e.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Get approved events that coordinator can work with
     */
    public List<Event> getApprovedEvents() {
        return eventRepository.getApprovedEvents();
    }

    /**
     * Get events pending financial approval
     */
    public List<Event> getPendingApprovalEvents() {
        return eventRepository.getPendingApprovalEvents();
    }

    /**
     * Get all eligible customers (from customer repository)
     */
    public List<Customer> getEligibleCustomers() {
        return customerRepository.getAll().stream()
                .filter(Customer::hasValidEmail)
                .collect(Collectors.toList());
    }

    /**
     * Add customer to event invite list
     */
    public boolean addCustomerToInviteList(int eventId, int customerId, boolean vip) {
        Event event = eventRepository.findById(eventId);
        Customer customer = customerRepository.findById(customerId);

        if (event == null || customer == null) {
            System.out.println("Event or customer not found");
            return false;
        }

        List<EventInvite> invites = eventInvites.computeIfAbsent(eventId, k -> new ArrayList<>());

        // Check if already invited
        boolean alreadyInvited = invites.stream()
                .anyMatch(i -> i.getCustomerId() == customerId);

        if (alreadyInvited) {
            System.out.println("Customer already on invite list");
            return false;
        }

        EventInvite invite = new EventInvite(nextInviteId++, eventId, customerId, vip);
        invites.add(invite);

        String vipLabel = vip ? " (VIP)" : "";
        System.out.println("Added " + customer.getName() + " to invite list" + vipLabel);
        return true;
    }

    /**
     * Remove customer from invite list
     */
    public boolean removeCustomerFromInviteList(int eventId, int customerId) {
        List<EventInvite> invites = eventInvites.get(eventId);
        if (invites == null) {
            return false;
        }

        boolean removed = invites.removeIf(i -> i.getCustomerId() == customerId);
        if (removed) {
            System.out.println("Removed customer from invite list");
        }
        return removed;
    }

    /**
     * Get invite list for an event
     */
    public List<EventInvite> getInviteList(int eventId) {
        return new ArrayList<>(eventInvites.getOrDefault(eventId, new ArrayList<>()));
    }

    /**
     * Send invitations to all customers on the invite list
     */
    public int sendInvites(int eventId) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            System.out.println("Event not found");
            return 0;
        }

        if (!event.isFinanciallyApproved()) {
            System.out.println("Event must be financially approved before sending invites");
            return 0;
        }

        List<EventInvite> invites = eventInvites.getOrDefault(eventId, new ArrayList<>());
        int sentCount = 0;

        for (EventInvite invite : invites) {
            if (!invite.isSent()) {
                Customer customer = customerRepository.findById(invite.getCustomerId());
                if (customer != null) {
                    boolean sent = messageHandler.sendEventInvitation(event, customer, invite.isVip());
                    if (sent) {
                        invite.markAsSent();

                        // Create pending RSVP
                        EventRSVP rsvp = new EventRSVP(nextRsvpId++, eventId, customer.getId());
                        eventRSVPs.computeIfAbsent(eventId, k -> new ArrayList<>()).add(rsvp);

                        sentCount++;
                    }
                }
            }
        }

        System.out.println("Sent " + sentCount + " invitations");
        return sentCount;
    }

    /**
     * Process RSVP response from a customer
     */
    public boolean processRSVP(int eventId, int customerId, boolean accepted, int partySize) {
        Event event = eventRepository.findById(eventId);
        Customer customer = customerRepository.findById(customerId);

        if (event == null || customer == null) {
            System.out.println("Event or customer not found");
            return false;
        }

        List<EventRSVP> rsvps = eventRSVPs.getOrDefault(eventId, new ArrayList<>());
        EventRSVP rsvp = rsvps.stream()
                .filter(r -> r.getCustomerId() == customerId)
                .findFirst()
                .orElse(null);

        if (rsvp == null) {
            System.out.println("No invitation found for this customer");
            return false;
        }

        if (accepted) {
            // Check capacity
            if (event.getRsvpCount() + partySize > event.getCapacity()) {
                // Add to waitlist
                rsvp.setStatus(EventRSVP.RSVPStatus.WAITLIST);
                rsvp.setPartySize(partySize);
                messageHandler.sendWaitlistNotification(event, customer);
                System.out.println(customer.getName() + " added to waitlist (event at capacity)");
            } else {
                // Accept RSVP
                rsvp.setStatus(EventRSVP.RSVPStatus.ACCEPTED);
                rsvp.setPartySize(partySize);
                event.setRsvpCount(event.getRsvpCount() + partySize);
                eventRepository.update(event);
                messageHandler.sendRSVPConfirmation(event, customer, true);
                System.out.println(customer.getName() + " accepted (party of " + partySize + ")");
            }
        } else {
            rsvp.setStatus(EventRSVP.RSVPStatus.DECLINED);
            messageHandler.sendRSVPConfirmation(event, customer, false);
            System.out.println(customer.getName() + " declined");
        }

        return true;
    }

    /**
     * Get RSVP summary for an event
     */
    public Map<String, Integer> getRSVPSummary(int eventId) {
        List<EventRSVP> rsvps = eventRSVPs.getOrDefault(eventId, new ArrayList<>());

        Map<String, Integer> summary = new HashMap<>();
        summary.put("PENDING", (int) rsvps.stream().filter(r -> r.getStatus() == EventRSVP.RSVPStatus.PENDING).count());
        summary.put("ACCEPTED", (int) rsvps.stream().filter(r -> r.getStatus() == EventRSVP.RSVPStatus.ACCEPTED).count());
        summary.put("DECLINED", (int) rsvps.stream().filter(r -> r.getStatus() == EventRSVP.RSVPStatus.DECLINED).count());
        summary.put("WAITLIST", (int) rsvps.stream().filter(r -> r.getStatus() == EventRSVP.RSVPStatus.WAITLIST).count());

        return summary;
    }

    /**
     * Get waitlist for an event
     */
    public List<EventRSVP> getWaitlist(int eventId) {
        return eventRSVPs.getOrDefault(eventId, new ArrayList<>()).stream()
                .filter(r -> r.getStatus() == EventRSVP.RSVPStatus.WAITLIST)
                .collect(Collectors.toList());
    }

    /**
     * Assign staff to event
     */
    public boolean assignStaffToEvent(int eventId, int staffId) {
        Event event = eventRepository.findById(eventId);
        Staff staff = hr.getStaffById(staffId);

        if (event == null) {
            System.out.println("Event not found");
            return false;
        }

        if (staff == null) {
            System.out.println("Staff member not found");
            return false;
        }

        if (!staff.isAvailable()) {
            System.out.println("Staff member is not available (at task capacity)");
            return false;
        }

        // Check for date conflicts
        if (hasDateConflict(staffId, event.getDate())) {
            System.out.println("Staff member already assigned to event on this date");
            return false;
        }

        event.assignStaff(staffId);
        eventRepository.update(event);
        System.out.println("Assigned " + staff.getName() + " to " + event.getName());
        return true;
    }

    /**
     * Remove staff from event
     */
    public boolean removeStaffFromEvent(int eventId, int staffId) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            return false;
        }

        event.removeStaff(staffId);
        eventRepository.update(event);
        System.out.println("Removed staff from event");
        return true;
    }

    /**
     * Check if staff has date conflict
     */
    private boolean hasDateConflict(int staffId, LocalDate date) {
        return eventRepository.getAll().stream()
                .filter(e -> e.getDate().equals(date))
                .anyMatch(e -> e.getAssignedStaffIds().contains(staffId));
    }

    /**
     * Get available staff (not at task capacity and no date conflict)
     */
    public List<Staff> getAvailableStaff(LocalDate eventDate) {
        return hr.getStaff().stream()
                .filter(Staff::isAvailable)
                .filter(s -> !hasDateConflict(s.getID(), eventDate))
                .collect(Collectors.toList());
    }

    /**
     * Lock event - no more changes allowed
     */
    public boolean lockEvent(int eventId) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            System.out.println("Event not found");
            return false;
        }

        if (!event.isFinanciallyApproved()) {
            System.out.println("Event must be approved before locking");
            return false;
        }

        if (event.getAssignedStaffIds().isEmpty()) {
            System.out.println("Cannot lock event without assigned staff");
            return false;
        }

        event.setStatus(Event.EventStatus.LOCKED);
        eventRepository.update(event);
        System.out.println("Event locked - no more invitations can be sent");
        return true;
    }

    /**
     * Cancel event and notify all invitees
     */
    public boolean cancelEvent(int eventId, String reason) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            System.out.println("Event not found");
            return false;
        }

        event.setStatus(Event.EventStatus.CANCELLED);
        event.setRejectionNotes(reason);
        eventRepository.update(event);

        // Notify all invited customers
        List<EventInvite> invites = eventInvites.getOrDefault(eventId, new ArrayList<>());
        int notified = 0;
        for (EventInvite invite : invites) {
            if (invite.isSent()) {
                Customer customer = customerRepository.findById(invite.getCustomerId());
                if (customer != null) {
                    messageHandler.sendCancellationNotice(event, customer);
                    notified++;
                }
            }
        }

        System.out.println("Event cancelled. " + notified + " customers notified.");
        System.out.println("Reason: " + reason);
        return true;
    }

    /**
     * Extract customers from online orders and receipts
     */
    public int extractCustomersFromTransactions(List<OnlineOrder> orders, String receiptDir) {
        int extracted = 0;

        // Extract from online orders
        for (OnlineOrder order : orders) {
            String name = order.getCustomerName();
            String email = extractEmailFromOrder(order);

            if (!email.isEmpty()) {
                Customer existing = customerRepository.findByEmail(email);
                if (existing == null) {
                    int id = customerRepository.getNextId();
                    Customer customer = new Customer(id, name, email, null);
                    customerRepository.add(customer);
                    extracted++;
                }
            }
        }

        System.out.println("Extracted " + extracted + " customers from transactions");
        return extracted;
    }

    /**
     *  In a real system, orders would have email field
     *  For now, generate a placeholder
     * @param order
     * @return
     */
    private String extractEmailFromOrder(OnlineOrder order) {
        return order.getCustomerName().toLowerCase().replace(" ", ".") + "@customer.com";
    }
}

