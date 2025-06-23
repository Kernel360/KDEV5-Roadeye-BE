package org.re.driving.repository;

import org.jspecify.annotations.Nullable;
import org.re.car.domain.Car;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.mdtlog.domain.TransactionUUID;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrivingHistoryRepository extends JpaRepository<DrivingHistory, Long> {
    @Nullable
    DrivingHistory findByCarAndTxUidAndStatus(Car car, TransactionUUID txUid, DrivingHistoryStatus status);
}
