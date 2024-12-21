package com.example.migrator.batch.config;

import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import com.example.migrator.batch.job.processor.UserProcessor;
import com.example.migrator.batch.job.reader.UserReader;
import com.example.migrator.batch.job.writer.UserWriter;
import com.example.migrator.batch.listener.JobCompletionNotificationListener;
import com.example.migrator.batch.listener.ProcessorListener;
import com.example.migrator.batch.listener.ReaderListener;
import com.example.migrator.batch.listener.WriterListener;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    // Injecting required dependencies: JobRepository, TransactionManager, and EntityManagerFactory
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    // Constructor for dependency injection
    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, EntityManagerFactory entityManagerFactory) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    // Bean to define the migration step for the batch job
    @Bean
    @Qualifier("step")
    public Step migrateStep(
            UserReader userReader,         // Reader for reading data
            UserProcessor userProcessor,   // Processor for transforming data
            UserWriter userWriter          // Writer for writing data
    ) {
        // Configure the reader to fetch UserEntity objects
        JpaPagingItemReader<UserEntity> reader = userReader.userItemReader();
        // Configure the writer to write UserDocument objects to MongoDB
        MongoItemWriter<UserDocument> writer = userWriter.writer();

        // Define the step
        return new StepBuilder("migrateStep", jobRepository)
                .<UserEntity, UserDocument>chunk(10, transactionManager) // Process chunks of 10 items
                .reader(reader)                // Set the reader
                .processor(userProcessor)      // Set the processor
                .writer(writer)                // Set the writer
                .faultTolerant()               // Enable fault tolerance
                .retry(Exception.class)        // Retry logic for exceptions
                .retryLimit(3)                 // Maximum retry attempts
                .listener(new ReaderListener()) // Listener for the reader
                .listener(new ProcessorListener()) // Listener for the processor
                .listener(new WriterListener(entityManagerFactory, entityDocumentMap())) // Listener for the writer
                .taskExecutor(taskExecutor())  // Use multi-threading
                .build();
    }

    // Bean to define the batch job
    @Bean(name = "job")
    public Job job(@Qualifier("step") Step step, JobCompletionNotificationListener listener) {
        return new JobBuilder("financialMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // Increment job instance ID
                .listener(listener)                  // Listener for job completion
                .start(step)                         // Start the job with the defined step
                .build();
    }

    // Bean to configure the task executor for multi-threading
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);                // Minimum number of threads
        executor.setMaxPoolSize(5);                // Maximum number of threads
        executor.setQueueCapacity(10);             // Queue capacity for tasks
        executor.setThreadNamePrefix("Batch-Thread-"); // Thread name prefix
        executor.initialize();                     // Initialize the executor
        return executor;
    }

    // Bean to provide a shared map for storing mappings between UserEntity and UserDocument
    @Bean
    public Map<Long, UserDocument> entityDocumentMap() {
        return new HashMap<>();
    }
}
