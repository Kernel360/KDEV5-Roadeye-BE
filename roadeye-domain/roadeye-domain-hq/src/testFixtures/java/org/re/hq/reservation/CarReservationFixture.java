package org.re.hq.reservation;

import org.re.hq.reservation.domain.CarReservation;
import org.re.hq.reservation.domain.ReservationPeriod;
import org.re.hq.reservation.domain.ReserveReason;

import java.time.LocalDateTime;
import java.util.List;

public class CarReservationFixture {
    public static CarReservation create(Long carId, int start, int end) {
        return CarReservation.createReservation(
            carId,
            10L,
            ReservationPeriod.of(LocalDateTime.now().plusDays(start), LocalDateTime.now().plusDays(end)),
            ReserveReason.BUSINESS_TRIP,
            LocalDateTime.now()
        );
    }
}
