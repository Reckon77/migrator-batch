package com.example.migrator.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
//            log.info("!!! JOB FINISHED! Time to verify the results");
            System.out.println("Job completed...................");
   }
    }
}

