package org.re.writer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.processor.DailyStatisticsProcessor;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.re.statistics.domain.HourlyDrivingStatistics;
import org.re.statistics.repository.DailyStatisticsRepository;
import org.re.statistics.repository.HourlyStatisticsRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyStatisticsWriter implements ItemWriter<DailyDrivingStatistics> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends DailyDrivingStatistics> items) throws Exception {

        if (!items.isEmpty()) {
            DailyDrivingStatistics stat = items.getItems().getLast();

            String sql = """
            INSERT INTO daily_driving_statistics (
                date, distance, duration, total_trip_count
            ) VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                distance = distance + VALUES(distance),
                duration = duration + VALUES(duration),
                total_trip_count = total_trip_count + VALUES(total_trip_count)
            """;

            jdbcTemplate.update(sql,
                stat.getDate(),
                stat.getDistance(),
                stat.getDuration(),
                stat.getTotalTripCount()
            );
        }

    }
}
