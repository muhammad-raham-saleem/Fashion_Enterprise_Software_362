package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event that can be coordinated by the Event Coordinator.
 * Events require financial approval before invitations can be sent.
 *
 * @author Sheldon Corkery
 */
public class Event {

    public enum EventStatus {
        DRAFT,              // Being created
        PENDING_APPROVAL,   // Submitted to finance for approval
        APPROVED,           // Finance approved, ready for coordination
        LOCKED,             // No more changes allowed
        CANCELLED           // Event cancelled
    }

    private final int id;
    private String name;
    private String venue;
    private LocalDate date;
    private double cost;
    private int capacity;
    private EventStatus status;
    private List<Integer> assignedStaffIds;
    private int rsvpCount;
    private String rejectionNotes;

    public Event(int id, String name, String venue, LocalDate date, double cost, int capacity) {
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.cost = cost;
        this.capacity = capacity;
        this.status = EventStatus.DRAFT;
        this.assignedStaffIds = new ArrayList<>();
        this.rsvpCount = 0;
        this.rejectionNotes = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<Integer> getAssignedStaffIds() {
        return new ArrayList<>(assignedStaffIds);
    }

    public void assignStaff(int staffId) {
        if (!assignedStaffIds.contains(staffId)) {
            assignedStaffIds.add(staffId);
        }
    }

    public void removeStaff(int staffId) {
        assignedStaffIds.remove(Integer.valueOf(staffId));
    }

    public int getRsvpCount() {
        return rsvpCount;
    }

    public void incrementRsvpCount() {
        this.rsvpCount++;
    }

    public void setRsvpCount(int count) {
        this.rsvpCount = count;
    }

    public String getRejectionNotes() {
        return rejectionNotes;
    }

    public void setRejectionNotes(String notes) {
        this.rejectionNotes = notes;
    }

    public boolean isFinanciallyApproved() {
        return status == EventStatus.APPROVED || status == EventStatus.LOCKED;
    }

    public boolean isLocked() {
        return status == EventStatus.LOCKED;
    }

    public boolean isCancelled() {
        return status == EventStatus.CANCELLED;
    }

    public boolean isAtCapacity() {
        return rsvpCount >= capacity;
    }

    @Override
    public String toString() {
        return "Event{id=" + id + ", name='" + name + "', venue='" + venue +
               "', date=" + date + ", cost=" + cost + ", capacity=" + capacity +
               ", status=" + status + ", rsvpCount=" + rsvpCount + "}";
    }

    /**
     * Serialize event to CSV format
     */
    public String toCsv() {
        StringBuilder staffIds = new StringBuilder();
        for (int i = 0; i < assignedStaffIds.size(); i++) {
            staffIds.append(assignedStaffIds.get(i));
            if (i < assignedStaffIds.size() - 1) {
                staffIds.append(";");
            }
        }

        return id + "," + name + "," + venue + "," + date + "," + cost + "," +
               capacity + "," + status + "," + rsvpCount + "," +
               (!staffIds.isEmpty() ? staffIds.toString() : "") + "," +
               (rejectionNotes != null ? rejectionNotes.replace(",", ";") : "");
    }

    /**
     * Parse event from CSV line
     */
    public static Event fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 8) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String venue = parts[2].trim();
            LocalDate date = LocalDate.parse(parts[3].trim());
            double cost = Double.parseDouble(parts[4].trim());
            int capacity = Integer.parseInt(parts[5].trim());

            Event event = new Event(id, name, venue, date, cost, capacity);

            if (!parts[6].trim().isEmpty()) {
                event.setStatus(EventStatus.valueOf(parts[6].trim()));
            }

            if (!parts[7].trim().isEmpty()) {
                event.setRsvpCount(Integer.parseInt(parts[7].trim()));
            }

            if (parts.length > 8 && !parts[8].trim().isEmpty()) {
                String[] staffIds = parts[8].trim().split(";");
                for (String staffId : staffIds) {
                    if (!staffId.isEmpty()) {
                        event.assignStaff(Integer.parseInt(staffId.trim()));
                    }
                }
            }

            if (parts.length > 9 && !parts[9].trim().isEmpty()) {
                event.setRejectionNotes(parts[9].trim().replace(";", ","));
            }

            return event;
        } catch (Exception e) {
            System.err.println("Error parsing event: " + csvLine);
            return null;
        }
    }
}

