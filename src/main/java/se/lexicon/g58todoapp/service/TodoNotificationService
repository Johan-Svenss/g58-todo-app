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
 * Creates and sends email notifications for todo events
 */
@Service
public class TodoNotificationServiceImpl implements TodoNotificationService {

    private final EmailService emailService;

    // Date formatter for nice date display in emails
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");

    public TodoNotificationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Send notification when a new todo is created
     */
    @Override
    public boolean notifyTodoCreated(Todo todo, Person recipient) {
        String subject = "📝 New Task Created: " + todo.getTitle();

        String body = String.format(
                "Hello %s,\n\n" +
                        "A new task has been created:\n\n" +
                        "📋 Title: %s\n" +
                        "📄 Description: %s\n" +
                        "📅 Due Date: %s\n\n" +
                        "Best regards,\n" +
                        "Todo App Team",
                recipient.getName(),
                todo.getTitle(),
                todo.getDescription() != null ? todo.getDescription() : "No description",
                todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date"
        );

        return emailService.sendSimpleEmail(recipient.getEmail(), subject, body);
    }

    /**
     * Send notification when a todo is assigned
     */
    @Override
    public boolean notifyTodoAssigned(Todo todo, Person assignee) {
        String subject = "✅ Task Assigned to You: " + todo.getTitle();

        // Create a nice HTML email
        String htmlBody = String.format(
                "<html>" +
                        "<head><style>" +
                        "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                        ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                        ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px; }" +
                        ".content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; border-left: 4px solid #4CAF50; }" +
                        ".footer { text-align: center; color: #777; font-size: 12px; margin-top: 20px; }" +
                        "h2 { margin: 0; }" +
                        ".task-detail { margin: 10px 0; }" +
                        ".label { font-weight: bold; color: #555; }" +
                        "</style></head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'><h2>New Task Assignment</h2></div>" +
                        "<div class='content'>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>A task has been assigned to you:</p>" +
                        "<div class='task-detail'>" +
                        "<p class='label'>📋 Task Title:</p>" +
                        "<h3 style='margin: 5px 0;'>%s</h3>" +
                        "</div>" +
                        "<div class='task-detail'>" +
                        "<p class='label'>📄 Description:</p>" +
                        "<p>%s</p>" +
                        "</div>" +
                        "<div class='task-detail'>" +
                        "<p class='label'>📅 Due Date:</p>" +
                        "<p style='color: #FF9800; font-weight: bold;'>%s</p>" +
                        "</div>" +
                        "<div class='task-detail'>" +
                        "<p class='label'>Status:</p>" +
                        "<p>%s</p>" +
                        "</div>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>This is an automated message from Todo App</p>" +
                        "</div>" +
                        "</div>" +
                        "</body></html>",
                assignee.getName(),
                todo.getTitle(),
                todo.getDescription() != null ? todo.getDescription() : "No description provided",
                todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date set",
                todo.getCompleted() ? "✅ Completed" : "⏳ Pending"
        );

        return emailService.sendHtmlEmail(assignee.getEmail(), subject, htmlBody);
    }

    /**
     * Send notification when a todo is completed
     */
    @Override
    public boolean notifyTodoCompleted(Todo todo, Person recipient) {
        String subject = "🎉 Task Completed: " + todo.getTitle();

        String htmlBody = String.format(
                "<html><body style='font-family: Arial, sans-serif;'>" +
                        "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                        "<h2 style='color: #2196F3;'>Great Job! 🎉</h2>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>Congratulations! The following task has been completed:</p>" +
                        "<div style='background-color: #e3f2fd; padding: 15px; border-left: 4px solid #2196F3; margin: 20px 0;'>" +
                        "<h3 style='margin-top: 0;'>%s</h3>" +
                        "<p>%s</p>" +
                        "</div>" +
                        "<p style='color: #4CAF50; font-weight: bold;'>Status: ✅ Completed</p>" +
                        "<p>Keep up the excellent work!</p>" +
                        "<p style='color: #777; font-size: 12px; margin-top: 30px;'>Best regards,<br>Todo App Team</p>" +
                        "</div>" +
                        "</body></html>",
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
                "<html><body style='font-family: Arial, sans-serif;'>" +
                        "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                        "<h2 style='color: #FF9800;'>⏰ Task Reminder</h2>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>This is a friendly reminder about an upcoming task:</p>" +
                        "<div style='background-color: #fff3e0; padding: 15px; border-left: 4px solid #FF9800; margin: 20px 0;'>" +
                        "<h3 style='margin-top: 0; color: #FF9800;'>%s</h3>" +
                        "<p><strong>Description:</strong> %s</p>" +
                        "<p><strong>⏰ Due Date:</strong> <span style='color: #F44336; font-weight: bold;'>%s</span></p>" +
                        "</div>" +
                        "<p style='background-color: #ffebee; padding: 10px; border-radius: 5px;'>" +
                        "⚠️ Please complete this task before the deadline!" +
                        "</p>" +
                        "<p style='color: #777; font-size: 12px; margin-top: 30px;'>Best regards,<br>Todo App Team</p>" +
                        "</div>" +
                        "</body></html>",
                recipient.getName(),
                todo.getTitle(),
                todo.getDescription() != null ? todo.getDescription() : "No description",
                todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date"
        );

        return emailService.sendHtmlEmail(recipient.getEmail(), subject, htmlBody);
    }

    /**
     * Send daily summary of all pending todos
     */
    @Override
    public boolean sendDailySummary(Person person, List<Todo> todos) {
        String subject = "📊 Daily Todo Summary - " + todos.size() + " Task(s)";

        // Build HTML table of todos
        StringBuilder todoTable = new StringBuilder();
        todoTable.append("<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>");
        todoTable.append("<thead>");
        todoTable.append("<tr style='background-color: #4CAF50; color: white;'>");
        todoTable.append("<th style='padding: 12px; text-align: left; border: 1px solid #ddd;'>Task</th>");
        todoTable.append("<th style='padding: 12px; text-align: left; border: 1px solid #ddd;'>Due Date</th>");
        todoTable.append("<th style='padding: 12px; text-align: center; border: 1px solid #ddd;'>Status</th>");
        todoTable.append("</tr>");
        todoTable.append("</thead>");
        todoTable.append("<tbody>");

        for (Todo todo : todos) {
            String bgColor = todo.getCompleted() ? "#e8f5e9" : "#fff3e0";
            String statusIcon = todo.getCompleted() ? "✅" : "⏳";
            String statusText = todo.getCompleted() ? "Completed" : "Pending";

            todoTable.append(String.format(
                    "<tr style='background-color: %s;'>" +
                            "<td style='padding: 12px; border: 1px solid #ddd;'><strong>%s</strong><br>" +
                            "<small style='color: #666;'>%s</small></td>" +
                            "<td style='padding: 12px; border: 1px solid #ddd;'>%s</td>" +
                            "<td style='padding: 12px; text-align: center; border: 1px solid #ddd;'>%s %s</td>" +
                            "</tr>",
                    bgColor,
                    todo.getTitle(),
                    todo.getDescription() != null ? todo.getDescription() : "",
                    todo.getDueDate() != null ? todo.getDueDate().format(DATE_FORMATTER) : "No due date",
                    statusIcon,
                    statusText
            ));
        }
        todoTable.append("</tbody>");
        todoTable.append("</table>");

        String htmlBody = String.format(
                "<html><body style='font-family: Arial, sans-serif;'>" +
                        "<div style='max-width: 800px; margin: 0 auto; padding: 20px;'>" +
                        "<h2 style='color: #4CAF50;'>📊 Your Daily Todo Summary</h2>" +
                        "<p>Hello <strong>%s</strong>,</p>" +
                        "<p>Here's your task overview for today:</p>" +
                        "%s" +
                        "<div style='background-color: #e3f2fd; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                        "<p style='margin: 0;'><strong>Summary:</strong></p>" +
                        "<ul style='margin: 10px 0;'>" +
                        "<li>Total Tasks: %d</li>" +
                        "<li>Completed: %d</li>" +
                        "<li>Pending: %d</li>" +
                        "</ul>" +
                        "</div>" +
                        "<p>Have a productive day! 💪</p>" +
                        "<p style='color: #777; font-size: 12px; margin-top: 30px;'>Best regards,<br>Todo App Team</p>" +
                        "</div>" +
                        "</body></html>",
                person.getName(),
                todoTable.toString(),
                todos.size(),
                todos.stream().filter(Todo::getCompleted).count(),
                todos.stream().filter(t -> !t.getCompleted()).count()
        );

        return emailService.sendHtmlEmail(person.getEmail(), subject, htmlBody);
    }
}