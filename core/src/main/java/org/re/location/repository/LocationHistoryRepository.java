package org.re.location.repository;

import org.re.location.domain.LocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationHistoryRepository extends JpaRepository<LocationHistory, Long> {
    List<LocationHistory> findByDrivingId(Long drivingId);

    List<LocationHistory> findByDrivingIdAndIdGreaterThanEqual(Long drivingId, Long id);
}
