package org.re.dashboard.api.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.re.statistics.domain.HourlyDrivingStatistics;

public record StatisticsInfo(
    LocalDateTime date,
    int distance,
    int duration,
    int totalDrivingCount,
    List<HourlyStatisticsInfo> hourlyStatisticsInfos
) {
    public static StatisticsInfo emptyOf(LocalDate date) {
        return new StatisticsInfo(
                date.atStartOfDay(), 0, 0, 0, List.of()
        );
    }

    public static StatisticsInfo from(@NonNull DailyDrivingStatistics daily, List<HourlyDrivingStatistics> hourlyStatisticsInfos) {
        List<HourlyStatisticsInfo> hourlyInfos = hourlyStatisticsInfos.stream()
            .map(HourlyStatisticsInfo::from)
            .toList();

        return new StatisticsInfo(
            daily.getDate(),
            daily.getDistance(),
            daily.getDuration(),
            daily.getTotalTripCount(),
            hourlyInfos
        );
    }
}
