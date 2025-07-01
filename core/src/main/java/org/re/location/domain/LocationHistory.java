package org.re.location.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.re.car.domain.CarLocation;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "car_location_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drivingId;

    @Embedded
    private CarLocation carLocation;

    @Embedded
    private DrivingMoment drivingMoment;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public LocationHistory(Long drivingId, CarLocation carLocation, DrivingMoment drivingMoment) {
        this.drivingId = drivingId;
        this.carLocation = carLocation;
        this.drivingMoment = drivingMoment;
    }

    public static LocationHistory of(Long drivingId, CarLocation carLocation, DrivingMoment drivingMoment) {
        return new LocationHistory(drivingId, carLocation, drivingMoment);
    }
}
