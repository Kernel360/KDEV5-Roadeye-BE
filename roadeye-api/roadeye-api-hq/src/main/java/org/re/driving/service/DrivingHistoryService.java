package org.re.driving.service;

import lombok.RequiredArgsConstructor;
import org.re.driving.domain.DrivingHistory;
import org.re.location.domain.LocationHistory;
import org.re.location.service.LocationHistoryDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DrivingHistoryService {
    private final DrivingHistoryDomainService drivingHistoryDomainService;
    private final LocationHistoryDomainService locationHistoryDomainService;

    public Page<DrivingHistory> getDrivingHistory(Pageable pageable) {
        return drivingHistoryDomainService.findAll(pageable);
    }

    public List<LocationHistory> getDrivingHistoryLogs(Long drivingId) {
        return locationHistoryDomainService.findByDrivingId(drivingId);
    }
}
