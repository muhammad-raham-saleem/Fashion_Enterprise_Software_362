package util;

import model.Event;
import model.Customer;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Email handler that displays HTML emails in the default browser
 *
 * @author Sheldon Corkery
 */
public class BrowserEmailHandler implements MessageHandler {

    private static final String TEMP_EMAIL_DIR = "temp/emails/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");

    public BrowserEmailHandler() {
        // Ensure temp directory exists
        File dir = new File(TEMP_EMAIL_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public boolean sendEventInvitation(Event event, Customer customer, boolean isVip) {
        String html = generateInvitationHTML(event, customer, isVip);
        String filename = TEMP_EMAIL_DIR + "invitation_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
    }

    @Override
    public boolean sendRSVPConfirmation(Event event, Customer customer, boolean accepted) {
        String html = generateRSVPConfirmationHTML(event, customer, accepted);
        String filename = TEMP_EMAIL_DIR + "rsvp_confirmation_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
    }

    @Override
    public boolean sendWaitlistNotification(Event event, Customer customer) {
        String html = generateWaitlistHTML(event, customer);
        String filename = TEMP_EMAIL_DIR + "waitlist_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
    }

    @Override
    public boolean sendCancellationNotice(Event event, Customer customer) {
        String html = generateCancellationHTML(event, customer);
        String filename = TEMP_EMAIL_DIR + "cancellation_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
    }

    private String generateInvitationHTML(Event event, Customer customer, boolean isVip) {
        String vipBadge = isVip ? "<span style='background: gold; color: black; padding: 5px 10px; border-radius: 5px; font-weight: bold;'>VIP GUEST</span>" : "";

        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Event Invitation</title>\n" +
            "    <style>\n" +
            "        body { font-family: 'Arial', sans-serif; background: #f4f4f4; margin: 0; padding: 20px; }\n" +
            "        .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 0 20px rgba(0,0,0,0.1); }\n" +
            "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px 20px; text-align: center; }\n" +
            "        .header h1 { margin: 0; font-size: 32px; }\n" +
            "        .content { padding: 40px 30px; }\n" +
            "        .event-details { background: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; }\n" +
            "        .detail-row { margin: 10px 0; font-size: 16px; }\n" +
            "        .label { font-weight: bold; color: #667eea; }\n" +
            "        .button { display: inline-block; background: #667eea; color: white; padding: 15px 40px; text-decoration: none; border-radius: 5px; margin: 20px 10px; font-weight: bold; }\n" +
            "        .button:hover { background: #764ba2; }\n" +
            "        .footer { background: #f8f9fa; padding: 20px; text-align: center; font-size: 14px; color: #666; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>You're Invited!</h1>\n" +
            "            " + vipBadge + "\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <p>Dear " + customer.getName() + ",</p>\n" +
            "            <p>We are delighted to invite you to our exclusive event:</p>\n" +
            "            \n" +
            "            <div class=\"event-details\">\n" +
            "                <div class=\"detail-row\"><span class=\"label\">Event:</span> " + event.getName() + "</div>\n" +
            "                <div class=\"detail-row\"><span class=\"label\">Venue:</span> " + event.getVenue() + "</div>\n" +
            "                <div class=\"detail-row\"><span class=\"label\">Date:</span> " + event.getDate().format(DATE_FORMATTER) + "</div>\n" +
            "                <div class=\"detail-row\"><span class=\"label\">Ticket Price:</span> $" + String.format("%.2f", event.getCost()) + "</div>\n" +
            "            </div>\n" +
            "            \n" +
            "            <p>This promises to be an unforgettable experience showcasing the latest in fashion innovation.</p>\n" +
            "            \n" +
            "            <div style=\"text-align: center;\">\n" +
            "                <a href=\"#\" class=\"button\" style=\"background: #28a745;\">ACCEPT</a>\n" +
            "                <a href=\"#\" class=\"button\" style=\"background: #dc3545;\">DECLINE</a>\n" +
            "            </div>\n" +
            "            \n" +
            "            <p style=\"margin-top: 30px; font-size: 14px; color: #666;\">\n" +
            "                Please respond by clicking one of the buttons above. Capacity is limited!\n" +
            "            </p>\n" +
            "        </div>\n" +
            "        <div class=\"footer\">\n" +
            "            <p>Fashion Enterprise | Event Coordination Department</p>\n" +
            "            <p>Email: " + customer.getEmail() + "</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
    }

    private String generateRSVPConfirmationHTML(Event event, Customer customer, boolean accepted) {
        String status = accepted ? "CONFIRMED" : "DECLINED";
        String color = accepted ? "#28a745" : "#dc3545";
        String message = accepted
            ? "We're excited to see you at the event! Please arrive 15 minutes early."
            : "We're sorry you can't make it. We hope to see you at future events!";

        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>RSVP Confirmation</title>\n" +
            "    <style>\n" +
            "        body { font-family: 'Arial', sans-serif; background: #f4f4f4; margin: 0; padding: 20px; }\n" +
            "        .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 0 20px rgba(0,0,0,0.1); }\n" +
            "        .header { background: " + color + "; color: white; padding: 40px 20px; text-align: center; }\n" +
            "        .content { padding: 40px 30px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>RSVP " + status + "</h1>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <p>Dear " + customer.getName() + ",</p>\n" +
            "            <p>Thank you for your response to <strong>" + event.getName() + "</strong>.</p>\n" +
            "            <p>" + message + "</p>\n" +
            "            <p><strong>Event Details:</strong></p>\n" +
            "            <ul>\n" +
            "                <li>Venue: " + event.getVenue() + "</li>\n" +
            "                <li>Date: " + event.getDate().format(DATE_FORMATTER) + "</li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
    }

    private String generateWaitlistHTML(Event event, Customer customer) {
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Waitlist Notification</title>\n" +
            "    <style>\n" +
            "        body { font-family: 'Arial', sans-serif; background: #f4f4f4; margin: 0; padding: 20px; }\n" +
            "        .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 0 20px rgba(0,0,0,0.1); }\n" +
            "        .header { background: #ffc107; color: #000; padding: 40px 20px; text-align: center; }\n" +
            "        .content { padding: 40px 30px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>You're on the Waitlist</h1>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <p>Dear " + customer.getName() + ",</p>\n" +
            "            <p>Thank you for your interest in <strong>" + event.getName() + "</strong>.</p>\n" +
            "            <p>Unfortunately, the event has reached capacity. You have been added to the waitlist and will be notified if a spot becomes available.</p>\n" +
            "            <p><strong>Event Details:</strong></p>\n" +
            "            <ul>\n" +
            "                <li>Venue: " + event.getVenue() + "</li>\n" +
            "                <li>Date: " + event.getDate().format(DATE_FORMATTER) + "</li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
    }

    private String generateCancellationHTML(Event event, Customer customer) {
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Event Cancelled</title>\n" +
            "    <style>\n" +
            "        body { font-family: 'Arial', sans-serif; background: #f4f4f4; margin: 0; padding: 20px; }\n" +
            "        .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 0 20px rgba(0,0,0,0.1); }\n" +
            "        .header { background: #dc3545; color: white; padding: 40px 20px; text-align: center; }\n" +
            "        .content { padding: 40px 30px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>Event Cancelled</h1>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <p>Dear " + customer.getName() + ",</p>\n" +
            "            <p>We regret to inform you that <strong>" + event.getName() + "</strong> has been cancelled.</p>\n" +
            "            <p>If you have already paid, a full refund will be processed within 5-7 business days.</p>\n" +
            "            <p>We apologize for any inconvenience and look forward to welcoming you at future events.</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
    }

    private boolean writeAndOpenHTML(String html, String filename) {
        try {
            File file = new File(filename);
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(html);
            }

            // Open in default browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(file.toURI());
                return true;
            } else {
                System.out.println("Email HTML saved to: " + file.getAbsolutePath());
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
}

