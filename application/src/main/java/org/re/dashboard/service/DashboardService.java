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

    public StatisticsInfo getStatisticsInfo(LocalDate date) {
        var dailyStatistics = statisticsDomainService.findDailyStatistics(date);

        if (dailyStatistics == null) {
            return StatisticsInfo.emptyOf(date);
        }

        var hourlyStatistics = statisticsDomainService.findHourlyStatistics(date);
        return StatisticsInfo.from(dailyStatistics, hourlyStatistics);
    }
}
