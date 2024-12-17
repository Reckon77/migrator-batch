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
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.repeat.RepeatOperations;
import org.springframework.batch.repeat.support.TaskExecutorRepeatTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

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
                .retry(Exception.class)
                .retryLimit(3)
                .listener(new ReaderListener())
                .listener(new ProcessorListener())
                .listener(new WriterListener())
                .taskExecutor(taskExecutor())
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

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Batch-Thread-");
        executor.initialize();
        return executor;
    }
}