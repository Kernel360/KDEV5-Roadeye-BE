package org.re.statistics.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.re.statistics.domain.HourlyDrivingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HourlyStatisticsRepository extends JpaRepository<HourlyDrivingStatistics, Long> {
    List<HourlyDrivingStatistics> findByDate(LocalDateTime date);
}
