package util;

import model.Event;
import model.Customer;

/**
 * Interface for sending messages (emails, notifications, etc.)
 * Allows for dependency injection and different implementations
 * 
 * @author Sheldon Corkery
 */
public interface MessageHandler {
    
    /**
     * Send an event invitation to a customer
     * 
     * @param event The event details
     * @param customer The customer to invite
     * @param isVip Whether this is a VIP invitation
     * @return true if sent successfully
     */
    boolean sendEventInvitation(Event event, Customer customer, boolean isVip);
    
    /**
     * Send RSVP confirmation to a customer
     * 
     * @param event The event details
     * @param customer The customer
     * @param accepted Whether they accepted or declined
     * @return true if sent successfully
     */
    boolean sendRSVPConfirmation(Event event, Customer customer, boolean accepted);
    
    /**
     * Send waitlist notification to a customer
     * 
     * @param event The event details
     * @param customer The customer
     * @return true if sent successfully
     */
    boolean sendWaitlistNotification(Event event, Customer customer);
    
    /**
     * Send event cancellation notice to a customer
     * 
     * @param event The event details
     * @param customer The customer
     * @return true if sent successfully
     */
    boolean sendCancellationNotice(Event event, Customer customer);
}

