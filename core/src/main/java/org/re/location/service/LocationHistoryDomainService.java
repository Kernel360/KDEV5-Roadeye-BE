package org.re.location.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.re.car.domain.CarLocation;
import org.re.common.stereotype.DomainService;
import org.re.driving.domain.DrivingHistory;
import org.re.location.domain.DrivingMoment;
import org.re.location.domain.LocationHistory;
import org.re.location.repository.LocationHistoryRepository;
import org.re.mdtlog.domain.MdtLog;

@DomainService
@Transactional
@RequiredArgsConstructor
public class LocationHistoryDomainService {
    private final LocationHistoryRepository locationHistoryRepository;

    public List<LocationHistory> findAll(DrivingHistory drivingHistory) {
        return locationHistoryRepository.findByDrivingId(drivingHistory.getId());
    }

    public List<LocationHistory> findAllWithCursor(DrivingHistory drivingHistory, @NonNull Long cursor) {
        return locationHistoryRepository.findByDrivingIdAndIdGreaterThanEqual(drivingHistory.getId(), cursor);
    }

    public void save(LocationHistory locationHistory) {
        locationHistoryRepository.save(locationHistory);
    }

    public void save(List<LocationHistory> locationHistories) {
        locationHistoryRepository.saveAll(locationHistories);
    }

    public List<LocationHistory> findByDrivingId(Long drivingId) {
        return locationHistoryRepository.findByDrivingId(drivingId);
    }

    public void sampling(@NonNull DrivingHistory drivingHistory, List<MdtLog> logs) {
        var sampled = new ArrayList<LocationHistory>();
        for (int i = 0; i < logs.size(); i++) {
            if (i % 10 == 0 || i == logs.size() - 1) {
                DrivingMoment drivingMoment = new DrivingMoment(logs.get(i).getOccurredAt(), logs.get(i).getMdtSpeed());
                CarLocation carLocation = logs.get(i).toCarLocation();
                LocationHistory history = LocationHistory.of(drivingHistory.getId(), carLocation, drivingMoment);
                sampled.add(history);
            }
        }
        save(sampled);
    }
}
