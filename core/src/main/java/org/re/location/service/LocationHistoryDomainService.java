package org.re.location.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.re.common.stereotype.DomainService;
import org.re.location.domain.LocationHistory;
import org.re.location.repository.LocationHistoryRepository;

import java.util.List;

@DomainService
@Transactional
@RequiredArgsConstructor
public class LocationHistoryDomainService {
    private final LocationHistoryRepository locationHistoryRepository;

    public void save(LocationHistory locationHistory) {
        locationHistoryRepository.save(locationHistory);
    }

    public void save(List<LocationHistory> locationHistories) {
        locationHistoryRepository.saveAll(locationHistories);
    }

    public List<LocationHistory> findByDrivingId(Long drivingId) {
        return locationHistoryRepository.findByDrivingId(drivingId);
    }
}
