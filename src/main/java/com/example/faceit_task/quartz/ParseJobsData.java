package com.example.faceit_task.quartz;

import com.example.faceit_task.services.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParseJobsData implements Job {

    private final JobService jobService;

    /**
     * Executes the job to parse and update job data.
     *
     * @param jobExecutionContext the context in which the job is executed
     * @throws JobExecutionException if there is an error during job execution
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing ParseJobsData...");
        try {
            jobService.updateJob();
            log.info("ParseJobsData completed successfully.");
        } catch (Exception e) {
            log.error("Error occurred during ParseJobsData execution:", e);
            throw new JobExecutionException("Error occurred during ParseJobsData execution", e);
        }
    }
}