package se.lexicon.g58todoapp.service;

import se.lexicon.g58todoapp.dto.EmailDTO;

/**
 * Email Service Interface
 * Defines the contract for email sending functionality
 *
 * By using an interface, we can easily swap implementations
 * or create mock versions for testing
 */
public interface EmailService {

    /**
     * Send a simple email
     *
     * @param to recipient email address
     * @param subject email subject
     * @param body email content (plain text)
     * @return true if email sent successfully, false otherwise
     */
    boolean sendSimpleEmail(String to, String subject, String body);

    /**
     * Send an email using EmailDTO object
     * This method supports more advanced features like CC, BCC, HTML, attachments
     *
     * @param emailDTO object containing all email information
     * @return true if email sent successfully, false otherwise
     */
    boolean sendEmail(EmailDTO emailDTO);

    /**
     * Send an HTML email
     * Useful for formatted emails with styling, images, etc.
     *
     * @param to recipient email address
     * @param subject email subject
     * @param htmlBody email content in HTML format
     * @return true if email sent successfully, false otherwise
     */
    boolean sendHtmlEmail(String to, String subject, String htmlBody);
}