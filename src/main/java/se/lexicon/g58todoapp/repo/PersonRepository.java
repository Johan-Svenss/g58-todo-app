package se.lexicon.g58todoapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.g58todoapp.entity.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    // TODO: Find person with a email
    // TODO: Is there a person with a specific email? - return boolean

    /**
     * Find a person by their email address
     * Spring Data JPA automatically implements this method based on the method name
     *
     * Method naming convention:
     * - "findBy" = SELECT query
     * - "Email" = WHERE email = ?
     *
     * @param email the email to search for
     * @return Optional containing the Person if found, empty otherwise
     */
    Optional<Person> findByEmail(String email);

    /**
     * Check if a person exists with a specific email
     * Returns true if at least one person has this email, false otherwise
     *
     * Method naming convention:
     * - "existsBy" = SELECT COUNT query
     * - "Email" = WHERE email = ?
     *
     * @param email the email to check
     * @return true if person exists, false otherwise
     */
    boolean existsByEmail(String email);
}