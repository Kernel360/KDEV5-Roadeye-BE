package org.re.statistics.repository;

import java.time.LocalDateTime;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatisticsRepository extends JpaRepository<DailyDrivingStatistics,Long> {
    DailyDrivingStatistics findByDate(LocalDateTime date);
}
