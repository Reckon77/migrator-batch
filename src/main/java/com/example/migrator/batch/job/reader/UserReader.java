package com.example.migrator.batch.job.reader;

import com.example.migrator.batch.domain.UserEntity;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

@Component
public class UserReader {

    private final EntityManagerFactory entityManagerFactory;

    public UserReader(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public JpaPagingItemReader<UserEntity> userItemReader() {
        System.out.println("Initializing JpaPagingItemReader...");
        JpaPagingItemReader<UserEntity> jpaPagingItemReader = new JpaPagingItemReaderBuilder<UserEntity>()
                .name("userItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM UserEntity u LEFT JOIN FETCH u.accounts a LEFT JOIN FETCH a.transactions")
                .pageSize(10) // Chunk size; adjust based on performance needs
                .build();

        jpaPagingItemReader.setSaveState(false); // Disable state tracking for multi-threading
        return jpaPagingItemReader;
    }



}


