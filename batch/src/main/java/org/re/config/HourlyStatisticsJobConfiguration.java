package org.re.config;

import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.re.driving.domain.DrivingHistory;
import org.re.processor.HourlyStatisticsProcessor;
import org.re.reader.DailyStatisticsReader;
import org.re.writer.HourlyStatisticsWriter;
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
public class HourlyStatisticsJobConfiguration {

    private final DailyStatisticsReader reader;
    private final HourlyStatisticsProcessor processor;
    private final HourlyStatisticsWriter writer;

    @Bean
    public Job hourlyDrivingStatisticsJob(JobRepository jobRepository, @Qualifier("hourlyDrivingStatisticsStep") Step step) {
        return new JobBuilder("HourlyDrivingStatisticsJob",jobRepository)
            .start(step).build();
    }

    @Bean
    public Step hourlyDrivingStatisticsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     EntityManagerFactory entityManagerFactory) {
        StepBuilder stepBuilder = new StepBuilder("HourlyDrivingStatisticsStep", jobRepository);
        return stepBuilder
            .<DrivingHistory, DrivingHistory>chunk(100,transactionManager)
            .reader(reader.drivingHistoryReader(entityManagerFactory))
            .processor(processor)
            .writer(writer)
            .build();
    }
}
