package org.re;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class JobExecutor {
    private final JobLauncher jobLauncher;

    private final Job job;

    @Scheduled(cron = "0 0 1 * * *")
    public void runBatchJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .addString("statDate", LocalDateTime.now().toString())
            .toJobParameters();
        jobLauncher.run(job, params);
    }
}
