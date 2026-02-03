package se.lexicon.g58todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Email Data Transfer Object
 * Simple object to hold email information
 *
 * Using @Builder allows us to create emails like:
 * EmailDTO email = EmailDTO.builder()
 *                     .to("user@example.com")
 *                     .subject("Hello")
 *                     .body("Message")
 *                     .build();
 */
@Data  // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Generates constructor with no parameters
@AllArgsConstructor  // Generates constructor with all parameters
@Builder  // Generates builder pattern methods
public class EmailDTO {

    /**
     * Recipient email address
     * Example: "john.doe@example.com"
     */
    private String to;

    /**
     * Carbon Copy recipients (optional)
     * These recipients will see they received a copy
     */
    private List<String> cc;

    /**
     * Blind Carbon Copy recipients (optional)
     * These recipients won't see other recipients
     */
    private List<String> bcc;

    /**
     * Email subject line
     */
    private String subject;

    /**
     * Email body content (can be plain text or HTML)
     */
    private String body;

    /**
     * Flag to indicate if body is HTML
     * true = HTML email, false = plain text email
     */
    private boolean html;

    /**
     * Simple constructor for basic text emails
     *
     * @param to recipient email
     * @param subject email subject
     * @param body email content
     */
    public EmailDTO(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.html = false;
    }

    /**
     * Constructor for HTML emails
     *
     * @param to recipient email
     * @param subject email subject
     * @param body email content (HTML)
     * @param html true for HTML, false for plain text
     */
    public EmailDTO(String to, String subject, String body, boolean html) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.html = html;
    }
}