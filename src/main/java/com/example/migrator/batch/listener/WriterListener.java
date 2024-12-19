package com.example.migrator.batch.listener;

import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

import java.time.LocalDateTime;
import java.util.Map;

public class WriterListener implements ItemWriteListener<UserDocument> {
    private final EntityManagerFactory entityManagerFactory;
    private final Map<Long, UserDocument> entityDocumentMap;

    public WriterListener(EntityManagerFactory entityManagerFactory, Map<Long, UserDocument> entityDocumentMap) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityDocumentMap = entityDocumentMap;
    }
    @Override
    public void beforeWrite(Chunk<? extends UserDocument> items) {
        System.out.println("Before Writing: " + items);
    }

    @Override
    public void afterWrite(Chunk<? extends UserDocument> items) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            for (UserDocument userDocument : items.getItems()) {
                // Retrieve the UserDocument from the shared map using its ID
                Long userId = Long.valueOf(userDocument.getId());
                UserEntity userEntity = entityManager.find(UserEntity.class, userId);

                if (userEntity != null) {
                    userEntity.setLastUpdated(LocalDateTime.now());
                    entityManager.merge(userEntity);
                }
            }
            entityManager.getTransaction().commit();
            System.out.println("Successfully updated lastUpdated for users in chunk: " + items.getItems());
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            System.err.println("Error updating lastUpdated field for chunk: " + ex.getMessage());
            throw new RuntimeException("Error updating lastUpdated field for chunk", ex);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void onWriteError(Exception ex, Chunk<? extends UserDocument> items) {
        System.err.println("Error during writing: " + ex.getMessage());
    }
}
