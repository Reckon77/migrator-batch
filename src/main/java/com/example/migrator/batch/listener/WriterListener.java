package com.example.migrator.batch.listener;

import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Listener implementation for monitoring and handling events during the writing process.
 * It updates the `lastUpdated` field of the associated `UserEntity` in the database after a successful write operation.
 */
public class WriterListener implements ItemWriteListener<UserDocument> {

    private final EntityManagerFactory entityManagerFactory; // Factory to create EntityManager instances
    private final Map<Long, UserDocument> entityDocumentMap; // Map to store processed UserDocuments for reference

    /**
     * Constructor for dependency injection.
     *
     * @param entityManagerFactory Factory to create EntityManager instances for database operations
     * @param entityDocumentMap Map to store mappings between User IDs and UserDocuments
     */
    public WriterListener(EntityManagerFactory entityManagerFactory, Map<Long, UserDocument> entityDocumentMap) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityDocumentMap = entityDocumentMap;
    }

    /**
     * Called before writing the chunk of items.
     *
     * @param items The chunk of UserDocuments about to be written
     */
    @Override
    public void beforeWrite(Chunk<? extends UserDocument> items) {
        System.out.println("Before Writing: " + items);
    }

    /**
     * Called after successfully writing the chunk of items.
     * Updates the `lastUpdated` timestamp of the associated `UserEntity` in the database.
     *
     * @param items The chunk of UserDocuments that were successfully written
     */
    @Override
    public void afterWrite(Chunk<? extends UserDocument> items) {
        // Create an EntityManager for database operations
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin(); // Start a new transaction

        try {
            // Iterate through the written UserDocuments
            for (UserDocument userDocument : items.getItems()) {
                // Convert UserDocument ID to Long and find the corresponding UserEntity
                Long userId = Long.valueOf(userDocument.getId());
                UserEntity userEntity = entityManager.find(UserEntity.class, userId);

                if (userEntity != null) {
                    // Update the `lastUpdated` field to the current timestamp
                    userEntity.setLastUpdated(LocalDateTime.now());
                    entityManager.merge(userEntity); // Merge the changes into the database
                }
            }
            entityManager.getTransaction().commit(); // Commit the transaction
            System.out.println("Successfully updated lastUpdated for users in chunk: " + items.getItems());
        } catch (Exception ex) {
            entityManager.getTransaction().rollback(); // Rollback the transaction in case of error
            System.err.println("Error updating lastUpdated field for chunk: " + ex.getMessage());
            throw new RuntimeException("Error updating lastUpdated field for chunk", ex);
        } finally {
            entityManager.close(); // Close the EntityManager
        }
    }

    /**
     * Called when there is an error during the write operation.
     *
     * @param ex The exception that occurred during writing
     * @param items The chunk of UserDocuments that failed to write
     */
    @Override
    public void onWriteError(Exception ex, Chunk<? extends UserDocument> items) {
        System.err.println("Error during writing: " + ex.getMessage());
    }
}
