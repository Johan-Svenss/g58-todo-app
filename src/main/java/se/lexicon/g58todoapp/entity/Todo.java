package se.lexicon.g58todoapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    // TODO: make sure to create/update this info. AUDITING? - Life Cycle methods
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    @ManyToOne
    private Person assignedTo;

    //TODO ATTACHMENT
    // Relationship: One Todo can have many Attachments
    // cascade = CascadeType.ALL means when we save/delete a todo, attachments are also saved/deleted
    // orphanRemoval = true means if we remove an attachment from the set, it's deleted from database
    // mappedBy = "todo" refers to the 'todo' field in the Attachment entity
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Exclude from toString to avoid circular reference issues
    private Set<Attachment> attachments = new HashSet<>();



    // TODO Add one more Constructor, Title, description

    public Todo(String title, String description, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Todo(String title, String description, Boolean completed, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    public Todo(String title, String description, LocalDateTime dueDate, Person assignedTo) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
    }

    // TODO : Equals & Hashcode
    /**
     * Equals method - compares todos based on their ID
     * Two todos are equal if they have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same object reference
        if (o == null || getClass() != o.getClass()) return false; // Null or different class
        Todo todo = (Todo) o;
        return id != null && id.equals(todo.id); // Compare by ID
    }

    /**
     * HashCode method - generates hash based on ID
     * Must be consistent with equals method
     */
    @Override
    public int hashCode() {
        return getClass().hashCode(); // Use class hash for consistency
    }

    /**
     * Lifecycle method - automatically sets createdAt timestamp
     * Runs BEFORE the entity is saved to the database for the first time
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle method - automatically updates updatedAt timestamp
     * Runs BEFORE the entity is updated in the database
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Helper method to add an attachment to this todo
     * Maintains bidirectional relationship between Todo and Attachment
     */
    public void addAttachment(Attachment attachment) {
        attachments.add(attachment); // Add to this todo's attachment set
        attachment.setTodo(this); // Set this todo as the attachment's parent
    }

    /**
     * Helper method to remove an attachment from this todo
     * Maintains bidirectional relationship
     */
    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment); // Remove from this todo's attachment set
        attachment.setTodo(null); // Remove the reference to this todo
    }
}
