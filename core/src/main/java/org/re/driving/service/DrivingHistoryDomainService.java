package org.re.driving.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.car.domain.Car;
import org.re.common.exception.DomainException;
import org.re.common.stereotype.DomainService;
import org.re.company.domain.CompanyId;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.driving.domain.DrivingSnapShot;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.re.driving.exception.DrivingHistoryExceptionCode;
import org.re.driving.repository.DrivingHistoryRepository;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.location.service.LocationHistoryDomainService;
import org.re.mdtlog.domain.MdtLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Slf4j
@DomainService
@Transactional
@RequiredArgsConstructor
public class DrivingHistoryDomainService {
    private final DrivingHistoryRepository drivingHistoryRepository;

    public void createNew(Car car, LocalDateTime driveStartAt) {
        if (drivingHistoryRepository.existsByCarAndTxUid(car, car.getMdtStatus().getActiveTuid())) {
            log.warn("Driving history already exists for car: {}, TUID: {}. It may be duplicated message", car.getId(), car.getMdtStatus().getActiveTuid());
            return;
        }

        var drivingHistory = DrivingHistory.createNew(car, driveStartAt);
        drivingHistoryRepository.save(drivingHistory);
    }

    public DrivingHistory findHistoryInProgress(Car car, UUID txid) {
        return drivingHistoryRepository.findByCarIdAndTxUidAndStatus(car.getId(), txid, DrivingHistoryStatus.DRIVING);
    }

    public DrivingHistory findHistoryInProgress(Long carId, UUID txid) {
        return drivingHistoryRepository.findByCarIdAndTxUidAndStatus(carId, txid, DrivingHistoryStatus.DRIVING);
    }

    public Page<DrivingHistory> findAll(CompanyId companyId, Pageable pageable) {
        return drivingHistoryRepository.findDrivingHistoryByCompanyId(companyId.value(), pageable);
    }

    public DrivingHistory findById(Long drivingId) {
        return drivingHistoryRepository.findById(drivingId)
            .orElseThrow(() -> new DomainException(DrivingHistoryExceptionCode.NOT_FOUND));
    }

    public List<DrivingHistoryMonthlyCountResult> getMonthlyCount() {
        return drivingHistoryRepository.countByMonth(LocalDateTime.now().minusMonths(11));
    }

    public void end(DrivingHistory driving, DrivingSnapShot snapShot, LocalDateTime driveEndAt) {
        driving.end(snapShot, driveEndAt);
    }
}
