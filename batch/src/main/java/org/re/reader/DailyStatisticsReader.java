package org.re.reader;

import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.re.driving.domain.DrivingHistory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyStatisticsReader  {

    @Bean
    @StepScope
    public JpaPagingItemReader<DrivingHistory> drivingHistoryReader(
        EntityManagerFactory entityManagerFactory
    ) {
        LocalDate day = LocalDate.now();
        LocalDateTime start =day.atStartOfDay();
        LocalDateTime end = day.atTime(LocalTime.MAX);

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);

        return new JpaPagingItemReaderBuilder<DrivingHistory>()
            .name("drivingHistoryReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT d FROM DrivingHistory d WHERE d.status = 'Ended' AND d.previousDrivingSnapShot.datetime BETWEEN :start AND :end")
            .parameterValues(params)
            .pageSize(1000) // chunk size와 동일하게
            .build();
    }

}
