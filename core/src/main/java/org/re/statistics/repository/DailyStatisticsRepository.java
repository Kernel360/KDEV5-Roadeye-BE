package org.re.statistics.repository;

import org.re.statistics.domain.DailyDrivingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatisticsRepository extends JpaRepository<DailyDrivingStatistics,Long> {
}
