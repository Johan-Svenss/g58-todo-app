package se.lexicon.g58todoapp.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import se.lexicon.g58todoapp.entity.Person;
import se.lexicon.g58todoapp.entity.Todo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Todo Notification Service Test Class
 * Tests all todo-related email notifications
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TodoNotificationServiceTest {

    @Autowired
    private TodoNotificationService notificationService;

    /**
     * GreenMail fake SMTP server for testing
     */
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig()
                    .withUser("test@example.com", "test")
                    .withDisabledAuthentication());

    // Test data - created before each test
    private Person testPerson;
    private Todo testTodo;

    /**
     * Set up test data before each test
     * This runs before EVERY test method
     */
    @BeforeEach
    void setUp() {
        // Create a test person
        testPerson = new Person(
                "John Doe",
                "john.doe@example.com",
                LocalDate.of(1990, 1, 1)
        );

        // Create a test todo
        testTodo = new Todo(
                "Complete project",
                "Finish the Spring Boot todo application",
                LocalDateTime.now().plusDays(3),
                testPerson
        );
    }

    /**
     * Test 1: Notify todo created
     */
    @Test
    void testNotifyTodoCreated() throws MessagingException, IOException {
        // When: We send a creation notification
        boolean result = notificationService.notifyTodoCreated(testTodo, testPerson);

        // Then: Notification should be sent successfully
        assertTrue(result, "Todo creation notification should be sent");

        // Verify: Check email was received
        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length, "Should receive 1 email");

        MimeMessage message = messages[0];

        // Check subject contains todo title
        assertTrue(message.getSubject().contains(testTodo.getTitle()),
                "Subject should contain todo title");

        // Check body contains person name
        String content = message.getContent().toString();
        assertTrue(content.contains(testPerson.getName()),
                "Body should contain recipient name");

        System.out.println("✅ Test Passed: Todo created notification sent");
        System.out.println("   Subject: " + message.getSubject());
    }

    /**
     * Test 2: Notify todo assigned
     */
    @Test
    void testNotifyTodoAssigned() throws MessagingException, IOException {
        // When: We send an assignment notification
        boolean result = notificationService.notifyTodoAssigned(testTodo, testPerson);

        // Then: Notification should be sent successfully
        assertTrue(result, "Todo assignment notification should be sent");

        // Verify: Check email content
        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length, "Should receive 1 email");

        MimeMessage message = messages[0];
        String content = message.getContent().toString();

        // Should be HTML email
        assertTrue(content.contains("<html>"), "Should be HTML email");

        // Should contain todo details
        assertTrue(content.contains(testTodo.getTitle()),
                "Should contain todo title");
        assertTrue(content.contains(testTodo.getDescription()),
                "Should contain todo description");

        System.out.println("✅ Test Passed: Todo assigned notification sent");
        System.out.println("   Is HTML: " + content.contains("<html>"));
    }

    /**
     * Test 3: Notify todo completed
     */
    @Test
    void testNotifyTodoCompleted() throws MessagingException, IOException {
        // Given: Todo is completed
        testTodo.setCompleted(true);

        // When: We send completion notification
        boolean result = notificationService.notifyTodoCompleted(testTodo, testPerson);

        // Then: Notification should be sent successfully
        assertTrue(result, "Todo completion notification should be sent");

        // Verify: Check email content
        MimeMessage message = greenMail.getReceivedMessages()[0];
        String subject = message.getSubject();

        // Subject should indicate completion
        assertTrue(subject.contains("Completed") || subject.contains("🎉"),
                "Subject should indicate task completion");

        System.out.println("✅ Test Passed: Todo completed notification sent");
        System.out.println("   Subject: " + subject);
    }

    /**
     * Test 4: Send due date reminder
     */
    @Test
    void testSendDueDateReminder() throws MessagingException, IOException {
        // When: We send a due date reminder
        boolean result = notificationService.sendDueDateReminder(testTodo, testPerson);

        // Then: Reminder should be sent successfully
        assertTrue(result, "Due date reminder should be sent");

        // Verify: Check email content
        MimeMessage message = greenMail.getReceivedMessages()[0];
        String subject = message.getSubject();
        String content = message.getContent().toString();

        // Should mention reminder
        assertTrue(subject.contains("Reminder") || subject.contains("⚠️"),
                "Subject should indicate this is a reminder");

        // Should contain due date information
        assertTrue(content.contains("Due Date") || content.contains("due"),
                "Content should mention due date");

        System.out.println("✅ Test Passed: Due date reminder sent");
    }

    /**
     * Test 5: Send daily summary
     */
    @Test
    void testSendDailySummary() throws MessagingException, IOException {
        // Given: We have multiple todos
        Todo todo2 = new Todo(
                "Review code",
                "Review pull requests",
                LocalDateTime.now().plusDays(1),
                testPerson
        );
        todo2.setCompleted(true);

        List<Todo> todos = Arrays.asList(testTodo, todo2);

        // When: We send daily summary
        boolean result = notificationService.sendDailySummary(testPerson, todos);

        // Then: Summary should be sent successfully
        assertTrue(result, "Daily summary should be sent");

        // Verify: Check email content
        MimeMessage message = greenMail.getReceivedMessages()[0];
        String subject = message.getSubject();
        String content = message.getContent().toString();

        // Should mention it's a summary
        assertTrue(subject.contains("Summary") || subject.contains("📊"),
                "Subject should indicate this is a summary");

        // Should contain task count
        assertTrue(subject.contains("2"),
                "Subject should show number of tasks");

        // Should list all todos
        assertTrue(content.contains(testTodo.getTitle()),
                "Should contain first todo title");
        assertTrue(content.contains(todo2.getTitle()),
                "Should contain second todo title");

        System.out.println("✅ Test Passed: Daily summary sent");
        System.out.println("   Number of tasks: 2");
    }

    /**
     * Test 6: Test with null due date
     * Ensures the service handles missing due dates gracefully
     */
    @Test
    void testNotificationWithNullDueDate() throws MessagingException, IOException {
        // Given: Todo has no due date
        Todo todoNoDueDate = new Todo(
                "Task without due date",
                "This task has no deadline",
                null,  // No due date
                testPerson
        );

        // When: We send notification
        boolean result = notificationService.notifyTodoAssigned(todoNoDueDate, testPerson);

        // Then: Should handle null due date gracefully
        assertTrue(result, "Should handle null due date");

        // Verify: Email should mention "No due date"
        MimeMessage message = greenMail.getReceivedMessages()[0];
        String content = message.getContent().toString();

        assertTrue(content.contains("No due date"),
                "Should indicate no due date is set");

        System.out.println("✅ Test Passed: Handled null due date correctly");
    }

    /**
     * Test 7: Test with null description
     * Ensures the service handles missing descriptions
     */
    @Test
    void testNotificationWithNullDescription() throws MessagingException, IOException {
        // Given: Todo has no description
        Todo todoNoDesc = new Todo(
                "Task without description",
                null,  // No description
                LocalDateTime.now().plusDays(1),
                testPerson
        );

        // When: We send notification
        boolean result = notificationService.notifyTodoCreated(todoNoDesc, testPerson);

        // Then: Should handle null description gracefully
        assertTrue(result, "Should handle null description");

        // Verify: Email should mention "No description"
        MimeMessage message = greenMail.getReceivedMessages()[0];
        String content = message.getContent().toString();

        assertTrue(content.contains("No description"),
                "Should indicate no description is provided");

        System.out.println("✅ Test Passed: Handled null description correctly");
    }
}