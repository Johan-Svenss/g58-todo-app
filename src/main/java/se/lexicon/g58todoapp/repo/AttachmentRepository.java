package se.lexicon.g58todoapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.g58todoapp.entity.Attachment;
import se.lexicon.g58todoapp.entity.Todo;

import java.util.List;

/**
 * Repository interface for Attachment entity
 * Provides database operations for file attachments
 */
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    /**
     * Find all attachments associated with a specific todo
     *
     * @param todo the todo task to find attachments for
     * @return List of attachments for this todo
     */
    List<Attachment> findByTodo(Todo todo);

    /**
     * Find attachments by file type
     * Useful for filtering (e.g., "show me all PDF attachments")
     *
     * @param fileType the MIME type to search for (e.g., "application/pdf")
     * @return List of attachments with matching file type
     */
    List<Attachment> findByFileType(String fileType);

    /**
     * Find attachments by file name (case-insensitive partial match)
     *
     * @param fileName the name or part of name to search for
     * @return List of attachments with matching file names
     */
    List<Attachment> findByFileNameContainingIgnoreCase(String fileName);
}