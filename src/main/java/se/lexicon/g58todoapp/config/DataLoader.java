package se.lexicon.g58todoapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.lexicon.g58todoapp.entity.Person;
import se.lexicon.g58todoapp.entity.Todo;
import se.lexicon.g58todoapp.repo.PersonRepository;
import se.lexicon.g58todoapp.repo.TodoRepository;
import se.lexicon.g58todoapp.service.TodoNotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Loader Configuration
 * This class runs when the application starts and loads sample data into the database
 * Useful for testing and development
 */
@Configuration
public class DataLoader {

    /**
     * CommandLineRunner is a Spring Boot interface that runs code after application startup
     * This bean method creates sample persons and todos for testing
     *
     * @param personRepo repository for saving persons
     * @param todoRepo repository for saving todos
     * @return CommandLineRunner that executes the data loading logic
     */
    @Bean
    public CommandLineRunner loadData(
            PersonRepository personRepo,
            TodoRepository todoRepo,
            TodoNotificationService notificationService) {
        return args -> {

            // Create and save sample persons
            Person alice = new Person("Alice Johnson", "alice@example.com", LocalDate.of(1990, 5, 15));
            Person bob = new Person("Bob Smith", "bob@example.com", LocalDate.of(1985, 8, 22));
            Person charlie = new Person("Charlie Brown", "charlie@example.com", LocalDate.of(1995, 3, 10));
            System.out.println("✅ Created 3 sample persons");

            // Save persons to database
            personRepo.save(alice);
            personRepo.save(bob);
            personRepo.save(charlie);

            System.out.println("✅ Created 3 sample persons");

            // Create sample todos with different scenarios

            // Todo 1: Assigned task with near due date
            Todo todo1 = new Todo("Buy groceries", "Milk, eggs, bread, vegetables",
                    LocalDateTime.now().plusDays(2), alice);

            // Todo 2: Completed task
            Todo todo2 = new Todo("Finish project report", "Complete the Q4 analysis",
                    true, LocalDateTime.now().minusDays(1));
            todo2.setAssignedTo(bob);

            // Todo 3: Overdue incomplete task
            Todo todo3 = new Todo("Call dentist", "Schedule annual checkup",
                    LocalDateTime.now().minusDays(5), alice);

            // Todo 4: Unassigned task
            Todo todo4 = new Todo("Clean garage", "Organize tools and boxes",
                    LocalDateTime.now().plusDays(7));

            // Todo 5: Task with no due date
            Todo todo5 = new Todo("Learn Spring Boot", "Complete online tutorial", null);
            todo5.setAssignedTo(charlie);



            // Save todos to database
            todoRepo.save(todo1);
            todoRepo.save(todo2);
            todoRepo.save(todo3);
            todoRepo.save(todo4);
            todoRepo.save(todo5);


            System.out.println("✅ Created 5 sample todos");
            System.out.println("📧 Sending sample notifications...");
            notificationService.notifyTodoAssigned(todo1, alice);
            notificationService.sendDueDateReminder(todo3, alice);
            System.out.println("🚀 Application ready! Access H2 console at: http://localhost:8080/h2Console");
        };
    }
}