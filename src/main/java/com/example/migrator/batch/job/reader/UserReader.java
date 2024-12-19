package com.example.migrator.batch.job.reader;

import com.example.migrator.batch.domain.MigrationMetadata;
import com.example.migrator.batch.domain.UserEntity;
import com.example.migrator.batch.repository.MigrationMetadataRepository;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class UserReader {

    private final EntityManagerFactory entityManagerFactory;
    private final MigrationMetadataRepository migrationMetadataRepository;

    public UserReader(EntityManagerFactory entityManagerFactory, MigrationMetadataRepository migrationMetadataRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.migrationMetadataRepository = migrationMetadataRepository;
    }
    @Bean
    public JpaPagingItemReader<UserEntity> userItemReader() {
        // Fetch the last migration time
        LocalDateTime lastMigrationTime = fetchLastMigrationTime();

        return new JpaPagingItemReaderBuilder<UserEntity>()
                .name("userItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM UserEntity u LEFT JOIN FETCH u.accounts a LEFT JOIN FETCH a.transactions WHERE u.lastUpdated > :lastMigrationTime")
                .parameterValues(Map.of("lastMigrationTime", lastMigrationTime))
                .pageSize(10) // Chunk size
                .build();
    }

    private LocalDateTime fetchLastMigrationTime() {
        MigrationMetadata metadata = migrationMetadataRepository.findTopByOrderByIdDesc();
        if (metadata != null) {
            System.out.println("Last Migration Time: " + metadata.getLastMigrationTime());
            return metadata.getLastMigrationTime();
        } else {
            System.out.println("Default Migration Time: 1950-01-01");
            return LocalDateTime.of(1950, 1, 1, 0, 0);
        }
    }



}


