package org.re;

import java.time.LocalDateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class JobExecutor {

    private final JobLauncher jobLauncher;
    private final Job dailyJob;
    private final Job hourlyJob;

    public JobExecutor(
        JobLauncher jobLauncher,
        @Qualifier("dailyDrivingStatisticsJob") Job dailyJob,
        @Qualifier("hourlyDrivingStatisticsJob") Job hourlyJob
    ) {
        this.jobLauncher = jobLauncher;
        this.dailyJob = dailyJob;
        this.hourlyJob = hourlyJob;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void runBatchJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addString("statDate", LocalDateTime.now().toString())
            .toJobParameters();
        jobLauncher.run(dailyJob, params);
        jobLauncher.run(hourlyJob, params);
    }
}
