package org.re.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.QDrivingHistory;
import org.re.processor.DailyStatisticsProcessor;
import org.re.reader.DailyStatisticsReader;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.re.writer.DailyStatisticsWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DailyStatisticsJobConfiguration {

    private final DailyStatisticsReader reader;
    private final DailyStatisticsProcessor processor;
    private final DailyStatisticsWriter writer;


    @Bean
    public Job dailyDrivingStatisticsJob(JobRepository jobRepository, @Qualifier("dailyDrivingStatisticsStep")Step step) {
        return new JobBuilder("dailyDrivingStatisticsJob",jobRepository)
            .start(step).build();
    }

    @Bean
    public Step dailyDrivingStatisticsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     EntityManagerFactory entityManagerFactory) {
        StepBuilder stepBuilder = new StepBuilder("dailyDrivingStatisticsStep", jobRepository);
        return stepBuilder
            .<DrivingHistory, DailyDrivingStatistics>chunk(1000,transactionManager)
            .reader(reader.drivingHistoryReader(entityManagerFactory))
            .processor(processor)
            .writer(writer)
            .build();
    }

}
