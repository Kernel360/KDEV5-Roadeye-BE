package org.re.writer;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.statistics.domain.DailyDrivingStatistics;
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
        int totalTripCount = 0;
        int totalDistance = 0;
        long totalDuration = 0;

        for (DailyDrivingStatistics stat : items) {
            totalTripCount += stat.getTotalTripCount();
            totalDistance += stat.getDistance();
            totalDuration += stat.getDuration();
        }

        DailyDrivingStatistics lastStat = new DailyDrivingStatistics(
            LocalDate.now().minusDays(1).atStartOfDay(),
            totalTripCount,
            totalDistance,
            (int) totalDuration
        );

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
            lastStat.getDate(),
            lastStat.getDistance(),
            lastStat.getDuration(),
            lastStat.getTotalTripCount()
        );

    }
}
