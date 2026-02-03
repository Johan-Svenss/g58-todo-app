package se.lexicon.g58todoapp.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import se.lexicon.g58todoapp.dto.EmailDTO;

import java.io.IOException;
import java.util.Arrays;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Email Service Test Class
 * Uses GreenMail to test email functionality without sending real emails
 *
 * GreenMail creates a fake SMTP server that captures emails
 * We can then inspect these emails to verify they were sent correctly
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    /**
     * GreenMail Extension - creates a fake SMTP server for testing
     * This captures all emails sent during tests
     *
     * @RegisterExtension tells JUnit to use this for all tests
     */
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig()
                    .withUser("test@example.com", "test")
                    .withDisabledAuthentication());

    /**
     * Test 1: Send a simple plain text email
     * This tests the most basic email functionality
     */
    @Test
    void testSendSimpleEmail() throws MessagingException, IOException {
        // Given: We have email details
        String to = "recipient@example.com";
        String subject = "Test Simple Email";
        String body = "This is a test email body.";

        // When: We send the email
        boolean result = emailService.sendSimpleEmail(to, subject, body);

        // Then: The email should be sent successfully
        assertTrue(result, "Email should be sent successfully");

        // Verify: Check the email was actually sent
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length, "Should have received exactly 1 email");

        MimeMessage receivedMessage = receivedMessages[0];

        // Check recipient
        assertEquals(to, receivedMessage.getAllRecipients()[0].toString(),
                "Recipient should match");

        // Check subject
        assertEquals(subject, receivedMessage.getSubject(),
                "Subject should match");

        // Check body content
        assertTrue(receivedMessage.getContent().toString().contains(body),
                "Body should contain the expected text");

        System.out.println("✅ Test Passed: Simple email sent and verified");
    }

    /**
     * Test 2: Send an HTML email
     * Tests that HTML content is preserved and sent correctly
     */
    @Test
    void testSendHtmlEmail() throws MessagingException, IOException {
        // Given: We have HTML email content
        String to = "htmlrecipient@example.com";
        String subject = "Test HTML Email";
        String htmlBody = "<html><body><h1>Hello!</h1><p>This is HTML content.</p></body></html>";

        // When: We send the HTML email
        boolean result = emailService.sendHtmlEmail(to, subject, htmlBody);

        // Then: The email should be sent successfully
        assertTrue(result, "HTML email should be sent successfully");

        // Verify: Check the email content
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length, "Should have received exactly 1 email");

        MimeMessage receivedMessage = receivedMessages[0];

        // Check that content is HTML
        String content = receivedMessage.getContent().toString();
        assertTrue(content.contains("<h1>Hello!</h1>"),
                "HTML content should be preserved");
        assertTrue(content.contains("<p>This is HTML content.</p>"),
                "HTML paragraph should be preserved");

        System.out.println("✅ Test Passed: HTML email sent and verified");
    }

    /**
     * Test 3: Send email with CC and BCC recipients
     * Tests advanced email features
     */
    @Test
    void testSendEmailWithCcAndBcc() throws MessagingException {
        // Given: We have an email with CC and BCC
        EmailDTO emailDTO = EmailDTO.builder()
                .to("primary@example.com")
                .cc(Arrays.asList("cc1@example.com", "cc2@example.com"))
                .bcc(Arrays.asList("bcc1@example.com"))
                .subject("Test Email with CC and BCC")
                .body("This email has multiple recipients")
                .html(false)
                .build();

        // When: We send the email
        boolean result = emailService.sendEmail(emailDTO);

        // Then: The email should be sent successfully
        assertTrue(result, "Email with CC and BCC should be sent successfully");

        // Verify: Check all recipients received the email
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        // Should receive 4 emails total (1 TO, 2 CC, 1 BCC)
        assertEquals(4, receivedMessages.length,
                "Should have sent to all recipients (TO + CC + BCC)");

        System.out.println("✅ Test Passed: Email with CC and BCC sent successfully");
    }

    /**
     * Test 4: Send multiple emails
     * Tests that the service can handle multiple emails in sequence
     */
    @Test
    void testSendMultipleEmails() throws MessagingException {
        // Given: We want to send 3 emails
        int numberOfEmails = 3;

        // When: We send multiple emails
        for (int i = 1; i <= numberOfEmails; i++) {
            boolean result = emailService.sendSimpleEmail(
                    "recipient" + i + "@example.com",
                    "Email " + i,
                    "This is email number " + i
            );
            assertTrue(result, "Email " + i + " should be sent successfully");
        }

        // Then: All emails should be received
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(numberOfEmails, receivedMessages.length,
                "Should have received all " + numberOfEmails + " emails");

        System.out.println("✅ Test Passed: Multiple emails sent successfully");
    }

    /**
     * Test 5: Verify email sender information
     * Tests that the FROM address and name are set correctly
     */
    @Test
    void testEmailSenderInformation() throws MessagingException {
        // Given: We send an email
        emailService.sendSimpleEmail(
                "test@example.com",
                "Test Sender Info",
                "Testing sender information"
        );

        // Then: Check the sender information
        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        String from = receivedMessage.getFrom()[0].toString();

        // Should contain our configured from address
        assertTrue(from.contains("noreply@todoapp.com"),
                "From address should be noreply@todoapp.com");

        System.out.println("✅ Test Passed: Sender information correct");
        System.out.println("   From: " + from);
    }

    /**
     * Test 6: Test email with special characters
     * Ensures encoding works properly
     */
    @Test
    void testEmailWithSpecialCharacters() throws MessagingException, IOException {
        // Given: We have a subject and body with special characters
        String subject = "Tëst Ëmäïl wïth Spëcïäl Chärä¢tërs åäö ñ";
        String body = "Hello! This is a test with émojis 🎉🚀 and special chars: ñ, ü, ö";

        // When: We send the email
        boolean result = emailService.sendSimpleEmail(
                "test@example.com",
                subject,
                body
        );

        // Then: Email should be sent successfully
        assertTrue(result, "Email with special characters should be sent");

        // Verify: Special characters are preserved
        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertTrue(receivedMessage.getSubject().contains("Spëcïäl"),
                "Special characters in subject should be preserved");

        System.out.println("✅ Test Passed: Special characters handled correctly");
    }
}