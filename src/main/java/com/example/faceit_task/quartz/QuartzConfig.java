package com.example.faceit_task.quartz;


import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class QuartzConfig {

    /**
     * Creates the job detail for the ParseJobsData job.
     *
     * @return the job detail for ParseJobsData
     */
    @Bean
    public JobDetail parseJobsDataDetail() {
        return JobBuilder.newJob()
                .ofType(ParseJobsData.class)
                .storeDurably()
                .withIdentity("parseJobsData")
                .withDescription("Job to parse and update latest jobs")
                .build();
    }

    /**
     * Creates the trigger for the ParseJobsData job to run every minute.
     *
     * @param parseJobsDataTrigger the job detail for ParseJobsData
     * @return the trigger for ParseJobsData
     */
    @Bean
    public Trigger parseJobsDataTrigger(JobDetail parseJobsDataTrigger) {
        return TriggerBuilder.newTrigger()
                .forJob(parseJobsDataTrigger)
                .withIdentity("parseJobsDataTrigger")
                .withDescription("Trigger to execute parseJobsData")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */20 * ? * *"))
                .build();
    }
}