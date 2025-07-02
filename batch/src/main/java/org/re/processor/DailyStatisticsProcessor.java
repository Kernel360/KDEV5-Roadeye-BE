package org.re.processor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.re.driving.domain.DrivingHistory;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.re.statistics.domain.HourlyDrivingStatistics;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DailyStatisticsProcessor implements ItemProcessor<DrivingHistory, DailyDrivingStatistics> {
    private int totalTripCount = 0;
    private int totalDistance = 0;
    private long totalDuration = 0;
    private Long maxDrivingId = null;
    private int maxDrivingDistance = 0;
    private final Map<Integer, Integer> hourlyCount = new HashMap<>();

    @Override
    public DailyDrivingStatistics process(DrivingHistory item) throws Exception {
        totalTripCount++;
        assert item.getEndDrivingSnapShot() != null;
        int distance = item.getEndDrivingSnapShot().mileageSum();
        totalDistance += distance;

        Duration duration = Duration.between( item.getPreviousDrivingSnapShot().datetime(), item.getEndDrivingSnapShot().datetime());
        totalDuration += duration.getSeconds();
        if (distance > maxDrivingDistance) {
            maxDrivingId = item.getId();
            maxDrivingDistance = distance;
        }

        int startHour = item.getPreviousDrivingSnapShot().datetime().getHour();
        int endHour = item.getEndDrivingSnapShot().datetime().getHour();
        for(int hour = startHour; hour <= endHour; hour++) {
            hourlyCount.put(hour, hourlyCount.getOrDefault(hour, 0) + 1);
        }

        return null;
    }

    public DailyDrivingStatistics toStatistics() {
        return DailyDrivingStatistics.of(
            LocalDate.now().minusDays(1).atStartOfDay(),
            totalTripCount,
            totalDistance / totalTripCount,
            (int) totalDuration / totalTripCount,
            maxDrivingId
        );
    }

    public List<HourlyDrivingStatistics> toHourlyStatistics() {
        LocalDateTime date = LocalDate.now().minusDays(1).atStartOfDay();
        return hourlyCount.entrySet().stream()
            .map(e -> HourlyDrivingStatistics.of(date, e.getKey(), e.getValue()))
            .toList();
    }
}
