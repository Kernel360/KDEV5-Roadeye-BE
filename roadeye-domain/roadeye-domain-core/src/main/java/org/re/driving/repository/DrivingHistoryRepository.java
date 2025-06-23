package org.re.driving.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.re.car.domain.Car;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.mdtlog.domain.TransactionUUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DrivingHistoryRepository extends JpaRepository<DrivingHistory, Long> {
    @Nullable
    DrivingHistory findByCarAndTxUidAndStatus(Car car, TransactionUUID txUid, DrivingHistoryStatus status);

    @Query(value = """
        SELECT
            DATE_FORMAT(dh.previousDrivingSnapShot.datetime, '%Y-%m') AS month,
            COUNT(*) AS count
        FROM
            DrivingHistory dh
        WHERE
            dh.previousDrivingSnapShot.datetime >= :startDate
        GROUP BY FUNCTION('DATE_FORMAT', dh.previousDrivingSnapShot.datetime, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', dh.previousDrivingSnapShot.datetime, '%Y-%m')
        """)
    List<Object[]> countByMonth(LocalDateTime startDate);

}
