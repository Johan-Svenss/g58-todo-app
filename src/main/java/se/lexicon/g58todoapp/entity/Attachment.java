package se.lexicon.g58todoapp.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Attachment Entity
 * Represents a file attachment that can be associated with a Todo task.
 * This entity stores file information including the file name, type, and binary data.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(exclude = "data") // Exclude binary data from toString to avoid memory issues

@Entity
@Table(name = "attachments")
public class Attachment {

    // Primary key - auto-generated ID for each attachment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The name of the file (e.g., "report.pdf")
    @Column(nullable = false, length = 255)
    private String fileName;

    // The MIME type of the file (e.g., "application/pdf", "image/png")
    @Column(nullable = false, length = 100)
    private String fileType;

    // Binary data of the file stored as a large object (LOB)
    // @Lob tells JPA this is a large object that should be stored as BLOB in database
    @Lob
    @Column(nullable = false)
    private byte[] data;

    // Relationship: Many attachments can belong to one Todo
    // When we delete a todo, we don't automatically delete attachments
    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    /**
     * Custom constructor for creating an attachment without specifying the todo
     * Useful when creating an attachment before assigning it to a task
     */
    public Attachment(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    /**
     * Equals method - compares attachments based on their ID
     * Two attachments are equal if they have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same object reference
        if (o == null || getClass() != o.getClass()) return false; // Null or different class
        Attachment that = (Attachment) o;
        return id != null && id.equals(that.id); // Compare by ID
    }

    /**
     * HashCode method - generates hash based on ID
     * Must be consistent with equals method
     */
    @Override
    public int hashCode() {
        return getClass().hashCode(); // Use class hash for consistency
    }
}