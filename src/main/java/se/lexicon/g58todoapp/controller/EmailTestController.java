package se.lexicon.g58todoapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.g58todoapp.entity.Person;
import se.lexicon.g58todoapp.entity.Todo;
import se.lexicon.g58todoapp.repo.PersonRepository;
import se.lexicon.g58todoapp.repo.TodoRepository;
import se.lexicon.g58todoapp.service.EmailService;
import se.lexicon.g58todoapp.service.TodoNotificationService;

import java.util.List;

/**
 * Email Test Controller
 * Simple REST endpoints to test email functionality
 *
 * Access these endpoints via browser or Postman:
 * - http://localhost:8080/api/email/test-simple
 * - http://localhost:8080/api/email/test-assignment
 * - http://localhost:8080/api/email/test-summary
 */
@RestController
@RequestMapping("/api/email")
public class EmailTestController {

    private final EmailService emailService;
    private final TodoNotificationService notificationService;
    private final PersonRepository personRepository;
    private final TodoRepository todoRepository;

    public EmailTestController(
            EmailService emailService,
            TodoNotificationService notificationService,
            PersonRepository personRepository,
            TodoRepository todoRepository) {
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.personRepository = personRepository;
        this.todoRepository = todoRepository;
    }

    /**
     * Test simple email
     * GET http://localhost:8080/api/email/test-simple?email=your-email@gmail.com
     */
    @GetMapping("/test-simple")
    public ResponseEntity<String> testSimpleEmail(@RequestParam String email) {
        boolean sent = emailService.sendSimpleEmail(
                email,
                "Test Email from Todo App",
                "Hello! This is a test email from your Todo Application. If you receive this, your email service is working correctly!"
        );

        if (sent) {
            return ResponseEntity.ok("✅ Email sent successfully to " + email);
        } else {
            return ResponseEntity.status(500).body("❌ Failed to send email");
        }
    }

    /**
     * Test HTML email
     * GET http://localhost:8080/api/email/test-html?email=your-email@gmail.com
     */
    @GetMapping("/test-html")
    public ResponseEntity<String> testHtmlEmail(@RequestParam String email) {
        String htmlContent = "<html><body>" +
                "<h1 style='color: #4CAF50;'>Hello from Todo App!</h1>" +
                "<p>This is an <strong>HTML</strong> email with <em>formatting</em>.</p>" +
                "<ul><li>Feature 1</li><li>Feature 2</li><li>Feature 3</li></ul>" +
                "</body></html>";

        boolean sent = emailService.sendHtmlEmail(email, "HTML Test Email", htmlContent);

        if (sent) {
            return ResponseEntity.ok("✅ HTML email sent successfully to " + email);
        } else {
            return ResponseEntity.status(500).body("❌ Failed to send HTML email");
        }
    }

    /**
     * Test todo assignment notification
     * GET http://localhost:8080/api/email/test-assignment
     */
    @GetMapping("/test-assignment")
    public ResponseEntity<String> testAssignmentNotification() {
        // Get first person and first incomplete todo from database
        Person person = personRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No persons found in database"));

        Todo todo = todoRepository.findByCompleted(false).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No incomplete todos found"));

        boolean sent = notificationService.notifyTodoAssigned(todo, person);

        if (sent) {
            return ResponseEntity.ok("✅ Assignment notification sent to " + person.getEmail());
        } else {
            return ResponseEntity.status(500).body("❌ Failed to send notification");
        }
    }

    /**
     * Test daily summary
     * GET http://localhost:8080/api/email/test-summary
     */
    @GetMapping("/test-summary")
    public ResponseEntity<String> testDailySummary() {
        Person person = personRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No persons found in database"));

        List<Todo> todos = todoRepository.findByAssignedTo(person);

        boolean sent = notificationService.sendDailySummary(person, todos);

        if (sent) {
            return ResponseEntity.ok("✅ Daily summary sent to " + person.getEmail());
        } else {
            return ResponseEntity.status(500).body("❌ Failed to send summary");
        }
    }
}