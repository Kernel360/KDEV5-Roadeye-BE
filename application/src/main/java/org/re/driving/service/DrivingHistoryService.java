package org.re.driving.service;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.re.car.service.CarDomainService;
import org.re.common.exception.AppException;
import org.re.common.exception.CommonAppExceptionCode;
import org.re.company.domain.CompanyId;
import org.re.driving.api.payload.CurrentDrivingInfo;
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

    public CurrentDrivingInfo getCurrentDriving(Long carId) {
        var car = carDomainService.getCarById(carId);
        var driving = drivingHistoryDomainService.findHistoryInProgress(car, car.getMdtStatus().getActiveTuid());
        if (driving == null) {
            throw new AppException(CommonAppExceptionCode.NOT_FOUND);
        }
        return CurrentDrivingInfo.from(car, driving);
    }

    public Page<DrivingHistory> getDrivingHistory(CompanyId companyId, Pageable pageable) {
        return drivingHistoryDomainService.findAll(companyId, pageable);
    }

    public DrivingHistory getDrivingHistoryById(Long drivingId) {
        return drivingHistoryDomainService.findById(drivingId);
    }

    public List<LocationHistory> getDrivingHistoryLogs(Long drivingId) {
        return locationHistoryDomainService.findByDrivingId(drivingId);
    }

    public List<LocationHistory> getDrivingLocationLogs(Long carId, @Nullable Long cursor) {
        var car = carDomainService.getCarById(carId);
        var drivingHistory = drivingHistoryDomainService.findHistoryInProgress(car, car.getMdtStatus().getActiveTuid());
        if (cursor == null) {
            return locationHistoryDomainService.findAll(drivingHistory);
        }
        return locationHistoryDomainService.findAllWithCursor(drivingHistory, cursor);
    }
}
