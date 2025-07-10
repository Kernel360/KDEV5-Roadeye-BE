package org.re.statistics.service;

import static org.re.statistics.domain.QDailyDrivingStatistics.dailyDrivingStatistics;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.stat.Statistics;
import org.jspecify.annotations.Nullable;
import org.re.common.stereotype.DomainService;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.re.statistics.domain.HourlyDrivingStatistics;
import org.re.statistics.repository.DailyStatisticsRepository;
import org.re.statistics.repository.HourlyStatisticsRepository;
import org.springframework.retry.stats.StatisticsRepository;

@DomainService
@Transactional
@RequiredArgsConstructor
public class StatisticsDomainService {

    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final HourlyStatisticsRepository hourlyStatisticsRepository;

    @Nullable
    public DailyDrivingStatistics findDailyStatistics(LocalDate localDate) {
        var date = localDate.atStartOfDay();
        return dailyStatisticsRepository.findByDate(date);
    }

    public List<HourlyDrivingStatistics> findHourlyStatistics(LocalDate localDate) {
        return hourlyStatisticsRepository.findByDate(localDate.atStartOfDay());
    }

    public void save(DailyDrivingStatistics statistics) {
        dailyStatisticsRepository.save(statistics);
    }
}
