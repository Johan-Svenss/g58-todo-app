package se.lexicon.g58todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Email Data Transfer Object
 * This class represents an email message with all necessary information
 *
 * @Builder allows us to create EmailDTO objects using builder pattern:
 *   EmailDTO email = EmailDTO.builder()
 *                           .to("user@example.com")
 *                           .subject("Hello")
 *                           .body("Message")
 *                           .build();
 */
@Data  // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Lombok: generates no-argument constructor
@AllArgsConstructor  // Lombok: generates constructor with all fields
@Builder  // Lombok: generates builder pattern methods
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
     * Example: "Your task 'Buy groceries' is due tomorrow"
     */
    private String subject;

    /**
     * Email body content
     * Can be plain text or HTML
     */
    private String body;

    /**
     * Flag to indicate if body content is HTML
     * true = HTML email, false = plain text email
     */
    private boolean isHtml;

    /**
     * Optional: Path to attachment file
     * Example: "/path/to/report.pdf"
     */
    private String attachmentPath;

    /**
     * Convenience constructor for simple text emails
     * Use this when you only need to, subject, and body
     */
    public EmailDTO(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.isHtml = false;  // Default to plain text
    }
}