package se.lexicon.g58todoapp.service.impl;

import org.springframework.stereotype.Service;
import se.lexicon.g58todoapp.entity.Person;
import se.lexicon.g58todoapp.entity.Todo;
import se.lexicon.g58todoapp.service.EmailService;
import se.lexicon.g58todoapp.service.TodoNotificationService;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Todo Notification Service Implementation
 * Creates and sends email notifications for todo-related events
 */
@Service
public class TodoNotificationServiceImpl implements TodoNotificationService {

    private final EmailService emailService;

    // Date formatter for displaying dates in emails
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");

    /**
     * Constructor injection of EmailService
     */
    public TodoNotificationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Notify when a new todo is created
     */
    @Override
    public boolean notifyTodoCreated(Todo todo, Person recipient) {
        String subject = "New Task Created: " + todo.getTitle();

        String body = String.format(
                "Hello %s,\n\n" +
                        "A new task has been created:\n\n" +
                        "Title: %s\n" +
                        "Description: %s\n" +
                        "Due Date: %s\n\n" +
                        "Best regards,\n" +
                        "Todo App",
                recipient.getName(),
                todo.getTitle(),
                todo.getDescription() != null ? todo.getDescription() : "No description",
                todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date"
        );

        return emailService.sendSimpleEmail(recipient.getEmail(), subject, body);
    }

    /**
     * Notify when a todo is assigned to someone
     */
    @Override
    public boolean notifyTodoAssigned(Todo todo, Person assignee) {
        String subject = "Task Assigned to You: " + todo.getTitle();

        // Create HTML email for better formatting
        String htmlBody = String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<h2 style='color: #4CAF50;'>New Task Assignment</h2>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>A task has been assigned to you:</p>" +
                        "<div style='background-color: #f5f5f5; padding: 15px; border-left: 4px solid #4CAF50;'>" +
                        "<h3 style='margin-top: 0;'>%s</h3>" +
                        "<p><strong>Description:</strong> %s</p>" +
                        "<p><strong>Due Date:</strong> %s</p>" +
                        "<p><strong>Status:</strong> %s</p>" +
                        "</div>" +
                        "<p>Please complete this task by the due date.</p>" +
                        "<p>Best regards,<br>Todo App Team</p>" +
                        "</body>" +
                        "</html>",
                assignee.getName(),
                todo.getTitle(),
                todo.getDescription() != null ? todo.getDescription() : "No description",
                todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date",
                todo.getCompleted() ? "Completed ✅" : "Pending ⏳"
        );

        return emailService.sendHtmlEmail(assignee.getEmail(), subject, htmlBody);
    }

    /**
     * Notify when a todo is completed
     */
    @Override
    public boolean notifyTodoCompleted(Todo todo, Person recipient) {
        String subject = "Task Completed: " + todo.getTitle();

        String htmlBody = String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<h2 style='color: #2196F3;'>Task Completed! 🎉</h2>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>Great news! The following task has been completed:</p>" +
                        "<div style='background-color: #e3f2fd; padding: 15px; border-left: 4px solid #2196F3;'>" +
                        "<h3 style='margin-top: 0;'>%s</h3>" +
                        "<p>%s</p>" +
                        "</div>" +
                        "<p>Keep up the great work!</p>" +
                        "<p>Best regards,<br>Todo App Team</p>" +
                        "</body>" +
                        "</html>",
                recipient.getName(),
                todo.getTitle(),
                todo.getDescription() != null ? todo.getDescription() : ""
        );

        return emailService.sendHtmlEmail(recipient.getEmail(), subject, htmlBody);
    }

    /**
     * Send reminder for upcoming due date
     */
    @Override
    public boolean sendDueDateReminder(Todo todo, Person recipient) {
        String subject = "⚠️ Reminder: Task Due Soon - " + todo.getTitle();

        String htmlBody = String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<h2 style='color: #FF9800;'>Task Reminder</h2>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>This is a friendly reminder that the following task is due soon:</p>" +
                        "<div style='background-color: #fff3e0; padding: 15px; border-left: 4px solid #FF9800;'>" +
                        "<h3 style='margin-top: 0;'>%s</h3>" +
                        "<p><strong>Description:</strong> %s</p>" +
                        "<p><strong>⏰ Due Date:</strong> <span style='color: #FF9800; font-weight: bold;'>%s</span></p>" +
                        "</div>" +
                        "<p>Please complete this task before the deadline.</p>" +
                        "<p>Best regards,<br>Todo App Team</p>" +
                        "</body>" +
                        "</html>",
                recipient.getName(),
                todo.getTitle(),
                todo.getDescription() != null ? todo.getDescription() : "No description",
                todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date"
        );

        return emailService.sendHtmlEmail(recipient.getEmail(), subject, htmlBody);
    }

    /**
     * Send daily summary of pending todos
     */
    @Override
    public boolean sendDailySummary(Person person, List<Todo> todos) {
        String subject = "Daily Todo Summary - " + todos.size() + " Pending Tasks";

        // Build HTML table of todos
        StringBuilder todoList = new StringBuilder();
        todoList.append("<table style='width: 100%; border-collapse: collapse;'>");
        todoList.append("<tr style='background-color: #4CAF50; color: white;'>");
        todoList.append("<th style='padding: 10px; text-align: left;'>Title</th>");
        todoList.append("<th style='padding: 10px; text-align: left;'>Due Date</th>");
        todoList.append("<th style='padding: 10px; text-align: left;'>Status</th>");
        todoList.append("</tr>");

        for (Todo todo : todos) {
            String rowColor = todo.getCompleted() ? "#f1f8e9" : "#fff3e0";
            todoList.append(String.format(
                    "<tr style='background-color: %s;'>" +
                            "<td style='padding: 10px; border-bottom: 1px solid #ddd;'>%s</td>" +
                            "<td style='padding: 10px; border-bottom: 1px solid #ddd;'>%s</td>" +
                            "<td style='padding: 10px; border-bottom: 1px solid #ddd;'>%s</td>" +
                            "</tr>",
                    rowColor,
                    todo.getTitle(),
                    todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date",
                    todo.getCompleted() ? "✅ Done" : "⏳ Pending"
            ));
        }
        todoList.append("</table>");

        String htmlBody = String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<h2 style='color: #4CAF50;'>Your Daily Todo Summary</h2>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>Here's your todo list for today:</p>" +
                        "%s" +
                        "<p style='margin-top: 20px;'>Have a productive day!</p>" +
                        "<p>Best regards,<br>Todo App Team</p>" +
                        "</body>" +
                        "</html>",
                person.getName(),
                todoList.toString()
        );

        return emailService.sendHtmlEmail(person.getEmail(), subject, htmlBody);
    }
}