package com.example.migrator.batch.job.writer;

import com.example.migrator.batch.domain.UserDocument;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component // Marks this class as a Spring-managed component, enabling dependency injection
public class UserWriter {

    private final MongoTemplate mongoTemplate; // Spring Data MongoTemplate for interacting with MongoDB

    /**
     * Constructor for dependency injection.
     * @param mongoTemplate The MongoTemplate instance for performing MongoDB operations.
     */
    public UserWriter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Creates a MongoItemWriter to write UserDocument objects to MongoDB.
     * @return A configured MongoItemWriter for UserDocument.
     */
    @Bean
    public MongoItemWriter<UserDocument> writer() {
        // Builds and returns a MongoItemWriter
        return new MongoItemWriterBuilder<UserDocument>()
                .template(mongoTemplate) // Sets the MongoTemplate to be used for MongoDB operations
                .collection("users") // Specifies the MongoDB collection to write to
                .build();
    }
}
