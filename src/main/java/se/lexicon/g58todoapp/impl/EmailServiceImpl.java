package se.lexicon.g58todoapp.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import se.lexicon.g58todoapp.dto.EmailDTO;
import se.lexicon.g58todoapp.service.EmailService;

import java.io.File;
import java.util.Properties;

/**
 * Email Service Implementation
 * This class handles the actual sending of emails using JavaMail API
 *
 * @Service tells Spring this is a service component that should be managed by Spring
 */
@Service
public class EmailServiceImpl implements EmailService {

    // These are injected from EmailConfig class
    private final Properties mailProperties;
    private final String username;
    private final String password;
    private final String fromAddress;
    private final String fromName;

    /**
     * Constructor-based dependency injection
     * Spring automatically injects the beans we created in EmailConfig
     *
     * @Qualifier helps Spring know which bean to inject when multiple beans of same type exist
     */
    public EmailServiceImpl(
            @Qualifier("emailProperties") Properties mailProperties,
            @Qualifier("emailUsername") String username,
            @Qualifier("emailPassword") String password,
            @Qualifier("fromAddress") String fromAddress,
            @Qualifier("fromName") String fromName) {
        this.mailProperties = mailProperties;
        this.username = username;
        this.password = password;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
    }

    /**
     * Send a simple plain text email
     * This is the easiest way to send emails
     */
    @Override
    public boolean sendSimpleEmail(String to, String subject, String body) {
        // Create an EmailDTO and delegate to the main sendEmail method
        EmailDTO emailDTO = EmailDTO.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .isHtml(false)
                .build();

        return sendEmail(emailDTO);
    }

    /**
     * Send an HTML email
     * HTML emails can have formatting, colors, images, links, etc.
     */
    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlBody) {
        EmailDTO emailDTO = EmailDTO.builder()
                .to(to)
                .subject(subject)
                .body(htmlBody)
                .isHtml(true)
                .build();

        return sendEmail(emailDTO);
    }

    /**
     * Main method to send any type of email
     * This method handles all the complexity of JavaMail API
     */
    @Override
    public boolean sendEmail(EmailDTO emailDTO) {
        try {
            // Step 1: Create a mail session with authentication
            Session session = createMailSession();

            // Step 2: Create the email message
            Message message = new MimeMessage(session);

            // Step 3: Set the "From" address and name
            message.setFrom(new InternetAddress(fromAddress, fromName));

            // Step 4: Set the "To" recipient
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailDTO.getTo())
            );

            // Step 5: Add CC recipients if provided
            if (emailDTO.getCc() != null && !emailDTO.getCc().isEmpty()) {
                String ccAddresses = String.join(",", emailDTO.getCc());
                message.setRecipients(
                        Message.RecipientType.CC,
                        InternetAddress.parse(ccAddresses)
                );
            }

            // Step 6: Add BCC recipients if provided
            if (emailDTO.getBcc() != null && !emailDTO.getBcc().isEmpty()) {
                String bccAddresses = String.join(",", emailDTO.getBcc());
                message.setRecipients(
                        Message.RecipientType.BCC,
                        InternetAddress.parse(bccAddresses)
                );
            }

            // Step 7: Set the subject
            message.setSubject(emailDTO.getSubject());

            // Step 8: Set the body content (text or HTML)
            if (emailDTO.isHtml()) {
                // HTML email
                message.setContent(emailDTO.getBody(), "text/html; charset=utf-8");
            } else {
                // Plain text email
                message.setText(emailDTO.getBody());
            }

            // Step 9: Add attachment if provided
            if (emailDTO.getAttachmentPath() != null && !emailDTO.getAttachmentPath().isEmpty()) {
                addAttachment(message, emailDTO.getAttachmentPath());
            }

            // Step 10: Send the email
            Transport.send(message);

            System.out.println("✅ Email sent successfully to: " + emailDTO.getTo());
            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error while sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create a mail session with authentication
     * This session is used to connect to the SMTP server
     */
    private Session createMailSession() {
        // Create an authenticator with username and password
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // Create and return the session
        return Session.getInstance(mailProperties, authenticator);
    }

    /**
     * Add an attachment to the email
     * This method handles file attachments
     */
    private void addAttachment(Message message, String attachmentPath) throws MessagingException {
        try {
            // Create a multipart message to hold both body and attachment
            MimeMultipart multipart = new MimeMultipart();

            // Add the email body as the first part
            MimeBodyPart textPart = new MimeBodyPart();
            if (message.getContent() instanceof String) {
                textPart.setText((String) message.getContent());
            }
            multipart.addBodyPart(textPart);

            // Add the attachment as the second part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            File file = new File(attachmentPath);
            attachmentPart.attachFile(file);
            multipart.addBodyPart(attachmentPart);

            // Set the complete multipart message
            message.setContent(multipart);

        } catch (Exception e) {
            throw new MessagingException("Failed to attach file: " + attachmentPath, e);
        }
    }
}