package org.re.driving.domain;

import jakarta.persistence.Embeddable;
import org.re.car.domain.Car;
import org.re.car.domain.CarLocation;

import java.time.LocalDateTime;

@Embeddable
public record DrivingSnapShot(
    int mileageSum,
    CarLocation location,
    LocalDateTime datetime
) {
    public static DrivingSnapShot from(Car car, LocalDateTime datetime) {
        return new DrivingSnapShot(
            car.getMdtStatus().getMileageSum(),
            car.getMdtStatus().getLocation(),
            datetime
        );
    }
}
