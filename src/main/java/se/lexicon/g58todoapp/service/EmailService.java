package se.lexicon.g58todoapp.service;

import se.lexicon.g58todoapp.dto.EmailDTO;

/**
 * Email Service Interface
 * Defines methods for sending different types of emails
 */
public interface EmailService {

    /**
     * Send a simple plain text email
     *
     * @param to recipient email address
     * @param subject email subject
     * @param body email content (plain text)
     * @return true if sent successfully, false otherwise
     */
    boolean sendSimpleEmail(String to, String subject, String body);

    /**
     * Send an email using EmailDTO object
     * Supports CC, BCC, and HTML content
     *
     * @param emailDTO object containing all email information
     * @return true if sent successfully, false otherwise
     */
    boolean sendEmail(EmailDTO emailDTO);

    /**
     * Send an HTML formatted email
     *
     * @param to recipient email address
     * @param subject email subject
     * @param htmlBody email content in HTML format
     * @return true if sent successfully, false otherwise
     */
    boolean sendHtmlEmail(String to, String subject, String htmlBody);
}