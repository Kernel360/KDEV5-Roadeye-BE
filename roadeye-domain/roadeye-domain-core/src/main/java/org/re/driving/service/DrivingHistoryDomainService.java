package org.re.driving.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.re.car.domain.Car;
import org.re.common.stereotype.DomainService;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.re.driving.repository.DrivingHistoryRepository;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.tenant.TenantId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@DomainService
@Transactional
@RequiredArgsConstructor
public class DrivingHistoryDomainService {
    private final DrivingHistoryRepository drivingHistoryRepository;

    public void createNew(Car car, LocalDateTime driveStartAt) {
        var drivingHistory = DrivingHistory.createNew(car, driveStartAt);
        drivingHistoryRepository.save(drivingHistory);
    }

    public DrivingHistory findHistoryInProgress(Car car, TransactionUUID transactionUUID) {
        return drivingHistoryRepository.findByCarAndTxUidAndStatus(car, transactionUUID, DrivingHistoryStatus.DRIVING);
    }

    public Page<DrivingHistory> findAll(TenantId tenantId, Pageable pageable) {
        return drivingHistoryRepository.findDrivingHistoryByCompanyId(tenantId.value(), pageable);
    }

    public List<DrivingHistoryMonthlyCountResult> getMonthlyCount() {
        return drivingHistoryRepository.countByMonth(LocalDateTime.now().minusMonths(11));
    }
}
