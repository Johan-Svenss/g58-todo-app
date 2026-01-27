package se.lexicon.g58todoapp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@ToString

@Entity
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NonNull
    @Column(nullable = false, length = 100)
    private String name;

    @Setter
    @NonNull
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Setter
    @NonNull
    private LocalDate birthDate;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    // TODO : Equals & Hashcode
    /**
     * Equals method - compares persons based on their email
     * Email is unique, so we use it for equality comparison
     * This is better than using ID because ID might be null for new entities
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same object reference
        if (o == null || getClass() != o.getClass()) return false; // Null or different class
        Person person = (Person) o;
        // Compare by email since it's unique and always present
        return email != null && email.equals(person.email);
    }

    /**
     * HashCode method - generates hash based on email
     * Must be consistent with equals method
     */
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
    // TODO : Life Cycle for createdAt;
    /**
     * Lifecycle method - automatically sets createdAt timestamp
     * @PrePersist is a JPA annotation that runs this method BEFORE
     * the entity is saved to the database for the first time
     */
    @PrePersist
    protected void onCreate() {
        // Set the creation timestamp to the current date
        this.createdAt = LocalDate.now();
    }
}
