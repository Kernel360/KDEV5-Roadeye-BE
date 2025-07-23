package org.re.driving.api.payload;

import org.re.car.domain.Car;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CurrentDrivingInfo(
    Long id,
    DrivingHistoryStatus status,
    Long carId,
    String carName,
    String carNumber,
    String driver,
    BigDecimal lat,
    BigDecimal lon,
    Integer spd,
    Integer ang,
    Integer bat,
    LocalDateTime driveStartedAt
) {
    public static CurrentDrivingInfo from(Car car, DrivingHistory drivingHistory) {
        return new CurrentDrivingInfo(
            drivingHistory.getId(),
            drivingHistory.getStatus(),
            car.getId(),
            car.getProfile().getName(),
            car.getProfile().getLicenseNumber(),
            null, // driver
            car.getMdtStatus().getLocation().getLatitude(),
            car.getMdtStatus().getLocation().getLongitude(),
            car.getMdtStatus().getSpeed(),
            car.getMdtStatus().getAngle(),
            car.getMdtStatus().getBatteryVoltage(),
            drivingHistory.getPreviousDrivingSnapShot().datetime()
        );
    }
}
