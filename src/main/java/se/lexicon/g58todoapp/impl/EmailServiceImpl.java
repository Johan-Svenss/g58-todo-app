package se.lexicon.g58todoapp.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import se.lexicon.g58todoapp.dto.EmailDTO;
import se.lexicon.g58todoapp.service.EmailService;

/**
 * Email Service Implementation
 * Uses Spring's JavaMailSender to send emails
 * Works with both real SMTP servers and test servers
 */
@Service
public class EmailServiceImpl implements EmailService {

    // JavaMailSender is Spring's main interface for sending emails
    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String fromName;

    /**
     * Constructor injection
     * Spring automatically provides these beans
     */
    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Qualifier("fromAddress") String fromAddress,
            @Qualifier("fromName") String fromName) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
    }

    /**
     * Send a simple plain text email
     * This is the easiest way to send emails
     */
    @Override
    public boolean sendSimpleEmail(String to, String subject, String body) {
        try {
            // SimpleMailMessage is for plain text emails only
            SimpleMailMessage message = new SimpleMailMessage();

            // Set email properties
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            mailSender.send(message);

            // Log success
            System.out.println("✅ Simple email sent successfully!");
            System.out.println("   To: " + to);
            System.out.println("   Subject: " + subject);

            return true;

        } catch (Exception e) {
            // Log error
            System.err.println("❌ Failed to send simple email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send an HTML email
     * HTML emails can have colors, formatting, images, etc.
     */
    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlBody) {
        // Create EmailDTO and use the main sendEmail method
        EmailDTO emailDTO = EmailDTO.builder()
                .to(to)
                .subject(subject)
                .body(htmlBody)
                .html(true)
                .build();

        return sendEmail(emailDTO);
    }

    /**
     * Main email sending method
     * Handles both plain text and HTML emails
     * Supports CC and BCC recipients
     */
    @Override
    public boolean sendEmail(EmailDTO emailDTO) {
        try {
            // MimeMessage is for HTML emails and advanced features
            MimeMessage message = mailSender.createMimeMessage();

            // MimeMessageHelper makes it easier to set email properties
            // 'true' parameter enables multipart mode (needed for HTML)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set sender information
            helper.setFrom(fromAddress, fromName);

            // Set recipient
            helper.setTo(emailDTO.getTo());

            // Set subject
            helper.setSubject(emailDTO.getSubject());

            // Set body content (text or HTML)
            helper.setText(emailDTO.getBody(), emailDTO.isHtml());

            // Add CC recipients if provided
            if (emailDTO.getCc() != null && !emailDTO.getCc().isEmpty()) {
                helper.setCc(emailDTO.getCc().toArray(new String[0]));
            }

            // Add BCC recipients if provided
            if (emailDTO.getBcc() != null && !emailDTO.getBcc().isEmpty()) {
                helper.setBcc(emailDTO.getBcc().toArray(new String[0]));
            }

            // Send the email
            mailSender.send(message);

            // Log success
            System.out.println("✅ Email sent successfully!");
            System.out.println("   To: " + emailDTO.getTo());
            System.out.println("   Subject: " + emailDTO.getSubject());
            System.out.println("   Type: " + (emailDTO.isHtml() ? "HTML" : "Plain Text"));

            return true;

        } catch (MessagingException e) {
            // Log error
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}