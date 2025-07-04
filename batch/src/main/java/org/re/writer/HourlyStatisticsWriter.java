package org.re.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.statistics.domain.HourlyDrivingStatistics;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HourlyStatisticsWriter implements ItemWriter<Map<Integer, Integer>> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Map<Integer,Integer>> items) throws Exception {
        if (!items.isEmpty()) {
            Map<Integer,Integer> stat = items.getItems().getLast();
            LocalDateTime date = LocalDate.now().minusDays(1).atStartOfDay();

            List<HourlyDrivingStatistics> stats = stat.entrySet().stream()
                    .map(e -> new HourlyDrivingStatistics(date, e.getKey(), e.getValue()))
                    .toList();

            String sql = """
            INSERT INTO hourly_driving_statistics (date, hour, vehicle_count)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE
                vehicle_count = vehicle_count + VALUES(vehicle_count)
            """;

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    HourlyDrivingStatistics s = stats.get(i);
                    ps.setTimestamp(1, Timestamp.valueOf(s.getDate()));
                    ps.setInt(2, s.getHour());
                    ps.setInt(3, s.getVehicleCount());
                }

                @Override
                public int getBatchSize() {
                    return stats.size();
                }
            });
        }
    }
}
