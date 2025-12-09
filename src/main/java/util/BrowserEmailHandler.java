package util;

import model.Event;
import model.Customer;

import java.awt.Desktop;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Email handler that displays HTML emails in the default browser
 * Loads HTML templates from resources/email_templates
 *
 * @author Sheldon Corkery
 */
public class BrowserEmailHandler implements MessageHandler {

    private static final String TEMP_EMAIL_DIR = "temp/emails/";
    private static final String TEMPLATE_BASE_PATH = "/email_templates/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");

    public BrowserEmailHandler() {
        // Ensure temp directory exists
        File dir = new File(TEMP_EMAIL_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Load HTML template from resources
     */
    private String loadTemplate(String templateName) {
        try (InputStream is = getClass().getResourceAsStream(TEMPLATE_BASE_PATH + templateName)) {
            if (is == null) {
                throw new IOException("Template not found: " + templateName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            System.err.println("Error loading template: " + e.getMessage());
            return "<html><body><h1>Error loading email template</h1></body></html>";
        }
    }

    @Override
    public boolean sendEventInvitation(Event event, Customer customer, boolean isVip) {
        String template = loadTemplate("invitation.html");

        String vipBadge = isVip
            ? "<p style='background-color: #ffd700; color: #000000; padding: 8px; margin: 10px 0; font-weight: bold; text-align: center;'> VIP GUEST </p>"
            : "";

        String html = template
            .replace("{{VIP_BADGE}}", vipBadge)
            .replace("{{CUSTOMER_NAME}}", customer.getName())
            .replace("{{EVENT_NAME}}", event.getName())
            .replace("{{EVENT_VENUE}}", event.getVenue())
            .replace("{{EVENT_DATE}}", event.getDate().format(DATE_FORMATTER))
            .replace("{{EVENT_COST}}", String.format("%.2f", event.getCost()))
            .replace("{{CUSTOMER_EMAIL}}", customer.getEmail());

        String filename = TEMP_EMAIL_DIR + "invitation_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
    }

    @Override
    public boolean sendRSVPConfirmation(Event event, Customer customer, boolean accepted) {
        String template = loadTemplate("rsvp_confirmation.html");

        String status = accepted ? "CONFIRMED" : "DECLINED";
        String color = accepted ? "#28a745" : "#dc3545";
        String message = accepted
            ? "We're excited to see you at the event! Please arrive 15 minutes early."
            : "We're sorry you can't make it. We hope to see you at future events!";

        String html = template
            .replace("{{STATUS}}", status)
            .replace("{{STATUS_COLOR}}", color)
            .replace("{{MESSAGE}}", message)
            .replace("{{CUSTOMER_NAME}}", customer.getName())
            .replace("{{EVENT_NAME}}", event.getName())
            .replace("{{EVENT_VENUE}}", event.getVenue())
            .replace("{{EVENT_DATE}}", event.getDate().format(DATE_FORMATTER));

        String filename = TEMP_EMAIL_DIR + "rsvp_confirmation_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
    }

    @Override
    public boolean sendWaitlistNotification(Event event, Customer customer) {
        String template = loadTemplate("waitlist.html");

        String html = template
            .replace("{{CUSTOMER_NAME}}", customer.getName())
            .replace("{{EVENT_NAME}}", event.getName())
            .replace("{{EVENT_VENUE}}", event.getVenue())
            .replace("{{EVENT_DATE}}", event.getDate().format(DATE_FORMATTER));

        String filename = TEMP_EMAIL_DIR + "waitlist_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
    }

    @Override
    public boolean sendCancellationNotice(Event event, Customer customer) {
        String template = loadTemplate("cancellation.html");

        String html = template
            .replace("{{CUSTOMER_NAME}}", customer.getName())
            .replace("{{EVENT_NAME}}", event.getName());

        String filename = TEMP_EMAIL_DIR + "cancellation_" + event.getId() + "_" + customer.getId() + ".html";
        return writeAndOpenHTML(html, filename);
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

