package com.example.migrator.batch.listener;

import com.example.migrator.batch.domain.MigrationMetadata;
import com.example.migrator.batch.repository.MigrationMetadataRepository;
import jakarta.persistence.EntityManager;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    private final EntityManager entityManager;
    private final MigrationMetadataRepository migrationMetadataRepository;

    public JobCompletionNotificationListener(EntityManager entityManager, MigrationMetadataRepository migrationMetadataRepository) {
        this.entityManager = entityManager;
        this.migrationMetadataRepository = migrationMetadataRepository;
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LocalDateTime latestUpdatedTime = fetchLatestUpdatedTime();

            // Save the new migration metadata
            MigrationMetadata metadata = new MigrationMetadata(latestUpdatedTime);
            migrationMetadataRepository.save(metadata);

            System.out.println("Job finished! Last migration timestamp updated to: " + latestUpdatedTime);
        }
    }

    private LocalDateTime fetchLatestUpdatedTime() {
        // Use JPA to find the latest `lastUpdated` time from UserEntity
        return entityManager.createQuery("SELECT MAX(u.lastUpdated) FROM UserEntity u", LocalDateTime.class)
                .getSingleResult();
    }
}

