package se.lexicon.g58todoapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.lexicon.g58todoapp.entity.Person;
import se.lexicon.g58todoapp.entity.Todo;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    
    // TODO : Tasks assigned to a specific Person
    // TODO : 📌 Count all tasks assigned to a person
    // TODO : ✅ Find completed tasks assigned to a specific person
    
    // TODO : 🔍 Find todos by title keyword (case-insensitive contains)
    // TODO : ✅ Find todos by completed status
    // TODO : 🗓️ Find todos between two due dates
    // TODO :️ Find todo due before a specific date and not completed
    // TODO :🔥 Find unfinished and overdue task
    // TODO : Find tasks that are not assigned to anyone
    // TODO : 📅 Find all with no due date


    // ========================================
    // PERSON-RELATED QUERIES
    // ========================================

    /**
     * Find all tasks assigned to a specific person
     *
     * @param person the person to search for
     * @return List of todos assigned to this person
     */
    List<Todo> findByAssignedTo(Person person);

    /**
     * Count all tasks assigned to a specific person
     * Useful for showing statistics like "You have 5 tasks assigned"
     *
     * @param person the person to count tasks for
     * @return number of tasks assigned to this person
     */
    long countByAssignedTo(Person person);

    /**
     * Find all completed tasks assigned to a specific person
     *
     * Method naming convention:
     * - "findBy" = SELECT
     * - "Completed" = WHERE completed = ?
     * - "And" = SQL AND operator
     * - "AssignedTo" = AND assigned_to = ?
     *
     * @param completed completion status (true for completed, false for not completed)
     * @param person the person to search for
     * @return List of tasks matching both criteria
     */
    List<Todo> findByCompletedAndAssignedTo(boolean completed, Person person);

    // ========================================
    // TITLE AND STATUS QUERIES
    // ========================================

    /**
     * Find todos by title keyword (case-insensitive, partial match)
     *
     * "Containing" means it uses SQL LIKE operator: WHERE title LIKE %keyword%
     * "IgnoreCase" makes it case-insensitive
     *
     * Example: keyword "shop" will find "Shopping", "SHOP", "workshop"
     *
     * @param keyword the text to search for in titles
     * @return List of todos with matching titles
     */
    List<Todo> findByTitleContainingIgnoreCase(String keyword);

    /**
     * Find todos by completed status
     * Simple query: WHERE completed = ?
     *
     * @param completed true to find completed tasks, false for incomplete
     * @return List of todos with the specified completion status
     */
    List<Todo> findByCompleted(boolean completed);

    // ========================================
    // DATE-RELATED QUERIES
    // ========================================

    /**
     * Find todos with due dates between two dates (inclusive)
     *
     * "Between" creates SQL: WHERE due_date BETWEEN ? AND ?
     * This includes both the start and end dates
     *
     * @param startDate the earliest due date to include
     * @param endDate the latest due date to include
     * @return List of todos due within this date range
     */
    List<Todo> findByDueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find todos that are due before a specific date and not yet completed
     * Useful for finding overdue incomplete tasks
     *
     * @param date the cutoff date
     * @param completed completion status (use false to find incomplete tasks)
     * @return List of incomplete todos due before the date
     */
    List<Todo> findByDueDateBeforeAndCompleted(LocalDateTime date, boolean completed);

    /**
     * Find unfinished and overdue tasks using a custom JPQL query
     * This demonstrates writing your own SQL-like query
     *
     * JPQL (Java Persistence Query Language) is similar to SQL but uses entity names
     * - "Todo t" = from the Todo entity, alias it as 't'
     * - "t.completed = false" = task is not completed
     * - "t.dueDate < :now" = due date is in the past
     * - ":now" is a parameter we pass to the query
     *
     * @param now the current date/time to compare against
     * @return List of incomplete todos that are overdue
     */
    @Query("SELECT t FROM Todo t WHERE t.completed = false AND t.dueDate < :now")
    List<Todo> findOverdueTasks(@Param("now") LocalDateTime now);

    // ========================================
    // ASSIGNMENT STATUS QUERIES
    // ========================================

    /**
     * Find tasks that are not assigned to anyone
     *
     * "IsNull" creates SQL: WHERE assigned_to IS NULL
     *
     * @return List of unassigned todos
     */
    List<Todo> findByAssignedToIsNull();

    /**
     * Find all tasks with no due date set
     * Useful for finding tasks that need scheduling
     *
     * @return List of todos without a due date
     */
    List<Todo> findByDueDateIsNull();
}