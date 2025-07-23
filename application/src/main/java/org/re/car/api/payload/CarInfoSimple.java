package org.re.car.api.payload;

import org.re.car.domain.Car;

import java.time.LocalDateTime;

public record CarInfoSimple(
    Long id,
    String name,
    String licenseNumber,
    String imageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CarInfoSimple from(Car car) {
        return new CarInfoSimple(
            car.getId(),
            car.getProfile().getName(),
            car.getProfile().getLicenseNumber(),
            car.getProfile().getImageUrl(),
            car.getCreatedAt(),
            car.getUpdatedAt()
        );
    }
}
