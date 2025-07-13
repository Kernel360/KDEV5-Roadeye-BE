package org.re.driving.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.car.domain.Car;
import org.re.common.exception.DomainException;
import org.re.common.stereotype.DomainService;
import org.re.company.domain.CompanyId;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.re.driving.exception.DrivingHistoryExceptionCode;
import org.re.driving.repository.DrivingHistoryRepository;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.mdtlog.dto.MdtEventMessage;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


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

    public DrivingHistory findHistoryInProgress(Car car, TransactionUUID transactionUUID) {
        return drivingHistoryRepository.findByCarIdAndTxUidAndStatus(car.getId(), transactionUUID, DrivingHistoryStatus.DRIVING);
    }

    public DrivingHistory findHistoryInProgress(Long carId, TransactionUUID transactionUUID) {
        return drivingHistoryRepository.findByCarIdAndTxUidAndStatus(carId, transactionUUID, DrivingHistoryStatus.DRIVING);
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

    public void end(Car car, MdtEventMessage<MdtIgnitionOffMessage> message) {
        var drivingHistory = findHistoryInProgress(car, message.transactionId());
        if (drivingHistory == null) {
            log.warn("No driving history found for car {} and transaction ID {}", car.getId(), message.transactionId());
        }
        else {
            drivingHistory.end(car, message.payload().ignitionOffTime());
        }
    }
}
