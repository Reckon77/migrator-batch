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

@Component // Marks this class as a Spring-managed component, making it eligible for dependency injection
public class UserReader {

    private final EntityManagerFactory entityManagerFactory; // Factory for creating JPA EntityManager instances
    private final MigrationMetadataRepository migrationMetadataRepository; // Repository to fetch migration metadata

    /**
     * Constructor for dependency injection.
     * @param entityManagerFactory EntityManagerFactory for managing JPA entities.
     * @param migrationMetadataRepository Repository to fetch metadata about previous migrations.
     */
    public UserReader(EntityManagerFactory entityManagerFactory, MigrationMetadataRepository migrationMetadataRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.migrationMetadataRepository = migrationMetadataRepository;
    }

    /**
     * Creates a JPA paging item reader to fetch `UserEntity` records from the database.
     * It uses a query to fetch only the records that were updated after the last migration time.
     * @return A configured JpaPagingItemReader for UserEntity.
     */
    @Bean
    public JpaPagingItemReader<UserEntity> userItemReader() {
        // Fetch the last migration time from the metadata repository
        LocalDateTime lastMigrationTime = fetchLastMigrationTime();

        // Build the JPA paging item reader
        return new JpaPagingItemReaderBuilder<UserEntity>()
                .name("userItemReader") // Set a name for the reader (useful for debugging)
                .entityManagerFactory(entityManagerFactory) // EntityManagerFactory to manage entities
                .queryString(
                        "SELECT u FROM UserEntity u LEFT JOIN FETCH u.accounts a LEFT JOIN FETCH a.transactions " +
                                "WHERE u.lastUpdated > :lastMigrationTime" // Query to fetch updated records
                )
                .parameterValues(Map.of("lastMigrationTime", lastMigrationTime)) // Pass the last migration time as a parameter
                .pageSize(10) // Set the page size (chunk size)
                .build();
    }

    /**
     * Fetches the last migration time from the `MigrationMetadataRepository`.
     * If no metadata exists, a default time of 1950-01-01 is returned.
     * @return The last migration time as a LocalDateTime.
     */
    private LocalDateTime fetchLastMigrationTime() {
        // Get the latest migration metadata record
        MigrationMetadata metadata = migrationMetadataRepository.findTopByOrderByIdDesc();

        if (metadata != null) {
            // If metadata is found, print and return the last migration time
            System.out.println("Last Migration Time: " + metadata.getLastMigrationTime());
            return metadata.getLastMigrationTime();
        } else {
            // If no metadata is found, use a default value and print it
            System.out.println("Default Migration Time: 1950-01-01");
            return LocalDateTime.of(1950, 1, 1, 0, 0);
        }
    }
}
