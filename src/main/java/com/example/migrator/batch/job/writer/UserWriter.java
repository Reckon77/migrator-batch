package com.example.migrator.batch.job.writer;

import com.example.migrator.batch.domain.UserDocument;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserWriter {

    private final MongoTemplate mongoTemplate;

    public UserWriter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public MongoItemWriter<UserDocument> writer() {
        return new MongoItemWriterBuilder<UserDocument>()
                .template(mongoTemplate)
                .collection("users")
                .build();
    }
}
