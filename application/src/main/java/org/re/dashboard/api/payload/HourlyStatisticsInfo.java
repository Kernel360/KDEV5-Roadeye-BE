package org.re.dashboard.api.payload;

import org.re.statistics.domain.HourlyDrivingStatistics;

public record HourlyStatisticsInfo(
    int hour,
    int vehicleCount
) {
    public static HourlyStatisticsInfo from(HourlyDrivingStatistics stat) {
        return new HourlyStatisticsInfo(stat.getHour(), stat.getVehicleCount());
    }
}
