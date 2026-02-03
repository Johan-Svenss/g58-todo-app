package se.lexicon.g58todoapp.service;

import se.lexicon.g58todoapp.entity.Person;
import se.lexicon.g58todoapp.entity.Todo;

import java.util.List;

/**
 * Todo Notification Service Interface
 * Handles sending email notifications related to todos
 */
public interface TodoNotificationService {

    /**
     * Notify when a new todo is created
     */
    boolean notifyTodoCreated(Todo todo, Person recipient);

    /**
     * Notify when a todo is assigned to someone
     */
    boolean notifyTodoAssigned(Todo todo, Person assignee);

    /**
     * Notify when a todo is completed
     */
    boolean notifyTodoCompleted(Todo todo, Person recipient);

    /**
     * Send reminder for todos due soon
     */
    boolean sendDueDateReminder(Todo todo, Person recipient);

    /**
     * Send daily summary of all pending todos
     */
    boolean sendDailySummary(Person person, List<Todo> todos);
}