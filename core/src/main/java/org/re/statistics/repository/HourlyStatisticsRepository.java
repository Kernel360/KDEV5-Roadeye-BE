package org.re.statistics.repository;

import org.re.statistics.domain.HourlyDrivingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HourlyStatisticsRepository extends JpaRepository<HourlyDrivingStatistics, Long> {
}
