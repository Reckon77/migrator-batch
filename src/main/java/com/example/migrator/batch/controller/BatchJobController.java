package com.example.migrator.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchJobController {

    private final JobLauncher jobLauncher;
    private final Job job;

    public BatchJobController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @GetMapping("/startJob")
    public String startJob() {
        try {
            jobLauncher.run(job, new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters());
            return "Job started successfully!";
        } catch (JobExecutionException e) {
            return "Job failed to start: " + e.getMessage();
        }
    }
}
