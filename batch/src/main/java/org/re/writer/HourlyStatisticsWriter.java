package org.re.writer;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.driving.domain.DrivingHistory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HourlyStatisticsWriter implements ItemWriter<DrivingHistory> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends DrivingHistory> items) throws Exception {
        if (!items.isEmpty()) {
            LocalDateTime date = LocalDate.now().minusDays(1).atStartOfDay();

            String sql = """
                INSERT INTO hourly_driving_statistics (date, hour, vehicle_count)
                VALUES (?, ?, 1)
                ON DUPLICATE KEY UPDATE vehicle_count = vehicle_count + 1
            """;

            for (DrivingHistory item : items) {
                int startHour = item.getPreviousDrivingSnapShot().datetime().getHour();
                int endHour = item.getEndDrivingSnapShot().datetime().getHour();
                if(startHour > endHour) {
                    for (int hour = startHour; hour <= 23; hour++) {
                        jdbcTemplate.update(sql, Timestamp.valueOf(date), hour);
                    }
                    for (int hour = 0; hour <= endHour; hour++) {
                        jdbcTemplate.update(sql, Timestamp.valueOf(date.plusDays(1)), hour);
                    }
                } else {
                    for (int hour = startHour; hour <= endHour; hour++) {
                        jdbcTemplate.update(sql, Timestamp.valueOf(date), hour);
                    }
                }
            }
        }
    }
}
