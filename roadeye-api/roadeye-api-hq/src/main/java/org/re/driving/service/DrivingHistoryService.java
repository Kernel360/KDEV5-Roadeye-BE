package org.re.driving.service;

import lombok.RequiredArgsConstructor;
import org.re.car.service.CarDomainService;
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
    private final CarDomainService carDomainService;

    public Page<DrivingHistory> getDrivingHistory(Pageable pageable) {
        return drivingHistoryDomainService.findAll(pageable);
    }

    public List<LocationHistory> getDrivingHistoryLogs(Long drivingId) {
        return locationHistoryDomainService.findByDrivingId(drivingId);
    }

    public List<LocationHistory> getDrivingLocationLogs(Long carId) {
        var car = carDomainService.getCarById(carId);
        var drivingHistory = drivingHistoryDomainService.findHistoryInProgress(car, car.getMdtStatus().getActiveTuid());
        return locationHistoryDomainService.findByDrivingId(drivingHistory.getId());
    }
}
