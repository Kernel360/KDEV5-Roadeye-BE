package org.re.hq.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.hq.domain.common.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarReservation extends BaseEntity {

    @Column(nullable = false)
    private Long carId;

    @Column(nullable = false)
    private Long reserverId;

    private Long approverId;

    @Embedded
    private ReservationPeriod reservationPeriod;

    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus;

    @Enumerated(EnumType.STRING)
    private ReserveReason reserveReason;

    private LocalDateTime reservedAt;

    private LocalDateTime processedAt;

    private String rejectReason;

    private CarReservation(Long carId, Long reserverId, ReservationPeriod reservationPeriod,
                           ReserveReason reserveReason, LocalDateTime reservedAt) {
        this.carId = carId;
        this.reserverId = reserverId;
        this.reservationPeriod = reservationPeriod;
        this.reserveStatus = ReserveStatus.REQUESTED;
        this.reserveReason = reserveReason;
        this.reservedAt = reservedAt;
    }

    public static CarReservation createReservation(Long carId, Long reserverId, ReservationPeriod reservationPeriod,
                                                   ReserveReason reserveReason, LocalDateTime reservedAt) {
        return new CarReservation(carId, reserverId, reservationPeriod, reserveReason, reservedAt);
    }

    public void approve(Long approverId, LocalDateTime processedAt) {
        if (this.reserveStatus != ReserveStatus.REQUESTED) {
            throw new IllegalStateException("Only reservations with REQUESTED status can be approved.");
        }
        this.reserveStatus = ReserveStatus.APPROVED;
        this.approverId = approverId;
        this.processedAt = processedAt;
    }

    public void reject(Long approverId, String rejectReason, LocalDateTime processedAt) {
        if (this.reserveStatus != ReserveStatus.REQUESTED) {
            throw new IllegalStateException("Only reservations with REQUESTED status can be approved.");
        }
        this.reserveStatus = ReserveStatus.REJECTED;
        this.approverId = approverId;
        this.rejectReason = rejectReason;
        this.processedAt = processedAt;
    }
}
