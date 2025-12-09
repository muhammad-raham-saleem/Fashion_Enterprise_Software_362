package model;

import java.time.LocalDateTime;

/**
 * Represents a customer's RSVP response to an event invitation
 *
 * @author Sheldon Corkery
 */
public class EventRSVP {

    public enum RSVPStatus {
        PENDING,    // Invitation sent, no response yet
        ACCEPTED,   // Customer accepted
        DECLINED,   // Customer declined
        WAITLIST    // Event at capacity, customer on waitlist
    }

    private final int id;
    private final int eventId;
    private final int customerId;
    private RSVPStatus status;
    private LocalDateTime responseDate;
    private int partySize; // How many people they're bringing

    public EventRSVP(int id, int eventId, int customerId) {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.status = RSVPStatus.PENDING;
        this.responseDate = null;
        this.partySize = 1;
    }

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public RSVPStatus getStatus() {
        return status;
    }

    public void setStatus(RSVPStatus status) {
        this.status = status;
        this.responseDate = LocalDateTime.now();
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public boolean hasResponded() {
        return status != RSVPStatus.PENDING;
    }

    @Override
    public String toString() {
        return "EventRSVP{id=" + id + ", eventId=" + eventId +
               ", customerId=" + customerId + ", status=" + status +
               ", partySize=" + partySize + "}";
    }

    /**
     * Serialize to CSV
     */
    public String toCsv() {
        return id + "," + eventId + "," + customerId + "," + status + "," +
               (responseDate != null ? responseDate.toString() : "") + "," + partySize;
    }

    /**
     * Parse from CSV
     */
    public static EventRSVP fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 4) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            int eventId = Integer.parseInt(parts[1].trim());
            int customerId = Integer.parseInt(parts[2].trim());

            EventRSVP rsvp = new EventRSVP(id, eventId, customerId);

            if (!parts[3].trim().isEmpty()) {
                rsvp.status = RSVPStatus.valueOf(parts[3].trim());
            }

            if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                rsvp.responseDate = LocalDateTime.parse(parts[4].trim());
            }

            if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                rsvp.setPartySize(Integer.parseInt(parts[5].trim()));
            }

            return rsvp;
        } catch (Exception e) {
            return null;
        }
    }
}

