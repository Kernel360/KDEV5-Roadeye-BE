package org.re.driving.repository;

import org.jspecify.annotations.Nullable;
import org.re.car.domain.Car;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DrivingHistoryRepository extends JpaRepository<DrivingHistory, Long>, DrivingHistoryCustomRepository {
    @Nullable
    DrivingHistory findByCarIdAndTxUidAndStatus(Long carId, UUID txid, DrivingHistoryStatus status);

    boolean existsByCarAndTxUid(Car car, UUID txUid);

}
