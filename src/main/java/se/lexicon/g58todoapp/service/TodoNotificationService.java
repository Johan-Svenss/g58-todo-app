package se.lexicon.g58todoapp.service;

import se.lexicon.g58todoapp.entity.Person;
import se.lexicon.g58todoapp.entity.Todo;

import java.util.List;

/**
 * Todo Notification Service Interface
 * Handles sending email notifications related to todo tasks
 */
public interface TodoNotificationService {

    /**
     * Send notification when a new todo is created
     *
     * @param todo the newly created todo
     * @param recipient the person to notify
     * @return true if notification sent successfully
     */
    boolean notifyTodoCreated(Todo todo, Person recipient);

    /**
     * Send notification when a todo is assigned to someone
     *
     * @param todo the assigned todo
     * @param assignee the person it's assigned to
     * @return true if notification sent successfully
     */
    boolean notifyTodoAssigned(Todo todo, Person assignee);

    /**
     * Send notification when a todo is completed
     *
     * @param todo the completed todo
     * @param recipient the person to notify
     * @return true if notification sent successfully
     */
    boolean notifyTodoCompleted(Todo todo, Person recipient);

    /**
     * Send reminder for todos due soon
     *
     * @param todo the todo that's due soon
     * @param recipient the person to remind
     * @return true if reminder sent successfully
     */
    boolean sendDueDateReminder(Todo todo, Person recipient);

    /**
     * Send daily summary of all pending todos to a person
     *
     * @param person the person to send summary to
     * @param todos list of their pending todos
     * @return true if summary sent successfully
     */
    boolean sendDailySummary(Person person, List<Todo> todos);
}