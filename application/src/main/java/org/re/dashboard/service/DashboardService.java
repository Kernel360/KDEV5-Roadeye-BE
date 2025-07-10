package org.re.dashboard.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.re.dashboard.api.payload.StatisticsInfo;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.re.driving.service.DrivingHistoryDomainService;
import org.re.statistics.service.StatisticsDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardService {
    private final DrivingHistoryDomainService drivingHistoryDomainService;
    private final StatisticsDomainService statisticsDomainService;

    public List<DrivingHistoryMonthlyCountResult> getDrivingHistoryMonthlyCountCommand() {
        return drivingHistoryDomainService.getMonthlyCount();
    }

    public StatisticsInfo getStatisticsInfo() {
        var dailyStatistics = statisticsDomainService.findDailyStatistics();

        var hourlyStatistics = statisticsDomainService.findHourlyStatistics();
        return StatisticsInfo.from(dailyStatistics, hourlyStatistics != null ? hourlyStatistics : List.of());
    }
}
