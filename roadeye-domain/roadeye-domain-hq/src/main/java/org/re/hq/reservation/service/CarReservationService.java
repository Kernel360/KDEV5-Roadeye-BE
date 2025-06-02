package org.re.hq.reservation.service;

import lombok.RequiredArgsConstructor;
import org.re.hq.reservation.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CarReservationService {
    private final CarReservationRepository carReservationRepository;

    /**
     * 예약 승인 처리
     */
    public void approveReservation(Long reservationId, Long approverId, LocalDateTime processedAt) {
        CarReservation reservation = carReservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID"));

        reservation.approve(approverId, processedAt);
    }

    /**
     * 예약 반려 처리
     */
    public void rejectReservation(Long reservationId, Long approverId, String rejectReason, LocalDateTime processedAt) {
        CarReservation reservation = carReservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID"));

        reservation.reject(approverId, rejectReason, processedAt);
    }

    /**
     * 예약 등록
     */
    public void createReservation(Long carId, Long reserverId, ReservationPeriod reservationPeriod,
                                  ReserveReason reserveReason, LocalDateTime reservedAt){
        checkReservation(carId, reservationPeriod);

        CarReservation carReservation = CarReservation.createReservation(carId, reserverId, reservationPeriod, reserveReason, reservedAt);

        carReservationRepository.save(carReservation);
    }

    /**
     * 차량은 동일한 시간대에 두 개 이상의 예약을 가질 수 없다.
     */
    private void checkReservation(Long carId, ReservationPeriod reservationPeriod) {
        boolean exists = carReservationRepository.existsCarReservationsByReservationPeriodContaining(
                carId,
                List.of(ReserveStatus.APPROVED, ReserveStatus.REQUESTED),
                reservationPeriod
        );

        if (exists) {
            throw new IllegalStateException("Reservation already exists.");
        }
    }

    /**
     * 해당 시간대에 예약이 있는 차량 리스트 반환
     */
    @Transactional(readOnly = true)
    public List<Long> findCarIdsWithReservationPeriod(ReservationPeriod reservationPeriod) {
        return carReservationRepository.findCarIdsWithReservationPeriod(
                reservationPeriod,
                List.of(ReserveStatus.APPROVED, ReserveStatus.REQUESTED)
        );
    }
}
