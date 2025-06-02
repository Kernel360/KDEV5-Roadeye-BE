package org.re.hq.reservation.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationPeriod {
    private LocalDateTime rentStartAt;

    private LocalDateTime rentEndAt;

    private ReservationPeriod(LocalDateTime rentStartAt, LocalDateTime rentEndAt) {
        if(rentStartAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Rent start time must be after current time");
        }

        if(!rentEndAt.isAfter(rentStartAt)) {
            throw new IllegalArgumentException("Rent end time must be after rent start time");
        }

        this.rentStartAt = rentStartAt;
        this.rentEndAt = rentEndAt;
    }

    public static ReservationPeriod of(LocalDateTime rentStartAt, LocalDateTime rentEndAt) {
        return new ReservationPeriod(rentStartAt, rentEndAt);
    }
}
