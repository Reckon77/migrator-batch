package com.example.migrator.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marks this class as a REST controller to handle HTTP requests
public class BatchJobController {

    private final JobLauncher jobLauncher; // Used to launch Spring Batch jobs
    private final Job job; // Reference to the batch job to be executed

    // Constructor for dependency injection of JobLauncher and Job
    public BatchJobController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    // Endpoint to trigger the batch job
    @GetMapping("/startJob") // Maps GET requests to the /startJob URL
    public String startJob() {
        try {
            // Launches the job with the current timestamp as a parameter
            jobLauncher.run(job, new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // Add a unique timestamp parameter
                    .toJobParameters()); // Convert the parameters to JobParameters
            return "Job started successfully!"; // Response when the job starts successfully
        } catch (JobExecutionException e) {
            // Catch any exceptions during job execution and return an error message
            return "Job failed to start: " + e.getMessage();
        }
    }
}
