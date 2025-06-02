package org.re.hq.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarReservationRepository extends JpaRepository<CarReservation, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM CarReservation r
        WHERE r.carId = :carId
        AND r.reserveStatus IN :statuses
        AND r.reservationPeriod.rentStartAt <= :#{#reservationPeriod.rentEndAt}
        AND r.reservationPeriod.rentEndAt >= :#{#reservationPeriod.rentStartAt}
        """)
    boolean existsCarReservationsByReservationPeriodContaining(Long carId, List<ReserveStatus> statuses, ReservationPeriod reservationPeriod);

    @Query("""
        SELECT DISTINCT r.carId FROM CarReservation r
        WHERE r.reserveStatus IN :statuses
        AND r.reservationPeriod.rentStartAt < :#{#reservationPeriod.rentEndAt}
        AND r.reservationPeriod.rentEndAt > :#{#reservationPeriod.rentStartAt}
        """)
    List<Long> findCarIdsWithReservationPeriod(
        ReservationPeriod reservationPeriod,
        List<ReserveStatus> statuses
    );
}
