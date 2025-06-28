package org.re.driving.api.payload;

import org.re.location.domain.LocationHistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DrivingLocationDetail(
    Long id,
    Long drivingId,
    BigDecimal latitude,
    BigDecimal longitude,
    LocalDateTime datetime,
    int speed,
    LocalDateTime createdAt
) {
    public static DrivingLocationDetail from(LocationHistory history) {
        return new DrivingLocationDetail(
            history.getId(),
            history.getDrivingId(),
            history.getCarLocation().getLatitude(),
            history.getCarLocation().getLongitude(),
            history.getDrivingMoment().datetime(),
            history.getDrivingMoment().speed(),
            history.getCreatedAt()
        );
    }
}
