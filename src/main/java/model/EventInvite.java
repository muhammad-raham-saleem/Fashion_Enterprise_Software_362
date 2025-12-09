package model;

import java.time.LocalDateTime;

/**
 * Represents an invitation sent to a customer for an event
 *
 * @author Sheldon Corkery
 */
public class EventInvite {

    private final int id;
    private final int eventId;
    private final int customerId;
    private LocalDateTime sentDate;
    private boolean vip; // Special guests can be marked as VIP

    public EventInvite(int id, int eventId, int customerId, boolean vip) {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.vip = vip;
        this.sentDate = null;
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

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void markAsSent() {
        this.sentDate = LocalDateTime.now();
    }

    public boolean isSent() {
        return sentDate != null;
    }

    @Override
    public String toString() {
        return "EventInvite{id=" + id + ", eventId=" + eventId +
               ", customerId=" + customerId + ", vip=" + vip +
               ", sent=" + isSent() + "}";
    }

    /**
     * Serialize to CSV
     */
    public String toCsv() {
        return id + "," + eventId + "," + customerId + "," + vip + "," +
               (sentDate != null ? sentDate.toString() : "");
    }

    /**
     * Parse from CSV
     */
    public static EventInvite fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 4) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            int eventId = Integer.parseInt(parts[1].trim());
            int customerId = Integer.parseInt(parts[2].trim());
            boolean vip = Boolean.parseBoolean(parts[3].trim());

            EventInvite invite = new EventInvite(id, eventId, customerId, vip);

            if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                invite.sentDate = LocalDateTime.parse(parts[4].trim());
            }

            return invite;
        } catch (Exception e) {
            return null;
        }
    }
}

