package org.re.car.api.payload;

import org.re.car.domain.Car;
import org.re.car.domain.CarIgnitionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CarInfoDetails(
    Long id,
    Long companyId,
    String name,
    String licenseNumber,
    String imageUrl,
    BigDecimal latitude,
    BigDecimal longitude,
    Integer mileageInitial,
    Integer mileageCurrent,
    Integer batteryVoltage,
    CarIgnitionStatus ignitionStatus,
    UUID activeTransactionId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime ignitionOnAt
) {
    public static CarInfoDetails from(Car car) {
        return new CarInfoDetails(
            car.getId(),
            car.getCompany().getId(),
            car.getProfile().getName(),
            car.getProfile().getLicenseNumber(),
            car.getProfile().getImageUrl(),
            car.getMdtStatus().getLocation().getLatitude(),
            car.getMdtStatus().getLocation().getLongitude(),
            car.getMdtStatus().getMileageInitial(),
            car.getMdtStatus().getMileageSum(),
            car.getMdtStatus().getBatteryVoltage(),
            car.getMdtStatus().getIgnition(),
            car.getMdtStatus().getActiveTuid(),
            car.getCreatedAt(),
            car.getUpdatedAt(),
            car.getMdtStatus().getIgnitionOnTime()
        );
    }
}
