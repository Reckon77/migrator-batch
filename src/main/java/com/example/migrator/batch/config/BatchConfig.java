package com.example.migrator.batch.config;

import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import com.example.migrator.batch.job.processor.UserProcessor;
import com.example.migrator.batch.job.reader.UserReader;
import com.example.migrator.batch.job.writer.UserWriter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }
    @Bean
    @Qualifier("step")
    public Step migrateStep(
            UserReader userReader,
            UserProcessor userProcessor,
            UserWriter userWriter) {

        JpaPagingItemReader<UserEntity> reader = userReader.userItemReader();
        MongoItemWriter<UserDocument> writer = userWriter.writer();

        return new StepBuilder("migrateStep", jobRepository)
                .<UserEntity, UserDocument>chunk(10, transactionManager)
                .reader(reader)
                .processor(userProcessor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class) // Skip exceptions
                .skipLimit(2)
                .listener(new ItemReadListener<UserEntity>() {
                    @Override
                    public void beforeRead() {
                        System.out.println("Before Read");
                    }

                    @Override
                    public void afterRead(UserEntity item) {
                        System.out.println("Read item: " + item);
                    }

                    @Override
                    public void onReadError(Exception ex) {
                        System.err.println("Error during read: " + ex.getMessage());
                    }
                })
                .listener(new ItemProcessListener<UserEntity, UserDocument>() {
                    @Override
                    public void beforeProcess(UserEntity item) {
                        System.out.println("Before Processing: " + item);
                    }

                    @Override
                    public void afterProcess(UserEntity item, UserDocument result) {
                        System.out.println("Processed item: " + result);
                    }

                    @Override
                    public void onProcessError(UserEntity item, Exception ex) {
                        System.err.println("Error during processing: " + ex.getMessage());
                    }
                })
                .listener(new ItemWriteListener<UserDocument>() {
                    @Override
                    public void beforeWrite(Chunk<? extends UserDocument> items) {
                        System.out.println("Before Writing: " + items);
                    }

                    @Override
                    public void afterWrite(Chunk<? extends UserDocument> items) {
                        System.out.println("Written items: " + items);
                    }

                    @Override
                    public void onWriteError(Exception ex, Chunk<? extends UserDocument> items) {
                        System.err.println("Error during writing: " + ex.getMessage());
                    }
                })
                .build();
    }

    @Bean(name = "job")
    public Job job(@Qualifier("step") Step step, JobCompletionNotificationListener listener) {
        return new JobBuilder("financialMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step)
                .build();
    }
}