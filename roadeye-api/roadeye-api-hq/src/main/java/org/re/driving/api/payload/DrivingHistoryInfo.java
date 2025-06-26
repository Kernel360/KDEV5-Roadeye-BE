package org.re.driving.api.payload;

import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.mdtlog.domain.TransactionUUID;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DrivingHistoryInfo(
    Long id,
    DrivingHistoryStatus status,
    TransactionUUID txUid,
    Integer previousMileageSum,
    LocalDateTime driveStartedAt,
    BigDecimal previousLatitude,
    BigDecimal previousLongitude,
    Integer nextMileageSum,
    LocalDateTime driveEndedAt,
    BigDecimal nextLatitude,
    BigDecimal nextLongitude,
    String carName,
    String licenseNumber
){
    public static DrivingHistoryInfo from(DrivingHistory history) {
        var endDrivingSnapShot = history.getEndDrivingSnapShot();
        var car = history.getCar();
        return new DrivingHistoryInfo(
            history.getId(),
            history.getStatus(),
            history.getTxUid(),
            history.getPreviousDrivingSnapShot().mileageSum(),
            history.getPreviousDrivingSnapShot().datetime(),
            history.getPreviousDrivingSnapShot().location().getLatitude(),
            history.getPreviousDrivingSnapShot().location().getLongitude(),
            endDrivingSnapShot != null ? endDrivingSnapShot.mileageSum() : null,
            endDrivingSnapShot != null ? endDrivingSnapShot.datetime() : null,
            endDrivingSnapShot != null ? endDrivingSnapShot.location().getLatitude() : null,
            endDrivingSnapShot != null ? endDrivingSnapShot.location().getLongitude() : null,
            car.getProfile().getName(),
            car.getProfile().getLicenseNumber()
        );
    }
}
