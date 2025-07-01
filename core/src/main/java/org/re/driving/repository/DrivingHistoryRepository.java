package org.re.driving.repository;

import org.jspecify.annotations.Nullable;
import org.re.car.domain.Car;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.mdtlog.domain.TransactionUUID;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingHistoryRepository extends JpaRepository<DrivingHistory, Long>, DrivingHistoryCustomRepository {
    @Nullable
    DrivingHistory findByCarAndTxUidAndStatus(Car car, TransactionUUID txUid, DrivingHistoryStatus status);

    boolean existsByCarAndTxUid(Car car, TransactionUUID txUid);
}
