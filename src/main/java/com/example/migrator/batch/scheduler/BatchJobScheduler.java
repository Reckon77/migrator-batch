package com.example.migrator.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job job;

    public BatchJobScheduler(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    //@Scheduled(cron = "0 0 * * * ?") // Run every hour
    @Scheduled(cron = "0 */2 * * * ?") // Run every 2 minutes
    public void scheduleJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Unique parameter to avoid re-execution
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
            System.out.println("Batch job executed successfully at " + System.currentTimeMillis());
        } catch (JobExecutionException e) {
            System.err.println("Failed to execute batch job: " + e.getMessage());
        }
    }
}
