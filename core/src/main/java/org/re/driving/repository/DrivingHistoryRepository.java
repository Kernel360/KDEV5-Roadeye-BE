package org.re.driving.repository;

import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.re.car.domain.Car;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingHistoryRepository extends JpaRepository<DrivingHistory, Long>, DrivingHistoryCustomRepository {
    @Nullable
    DrivingHistory findByCarIdAndTxUidAndStatus(Long carId, UUID txid, DrivingHistoryStatus status);

    boolean existsByCarAndTxUid(Car car, UUID txUid);

}
