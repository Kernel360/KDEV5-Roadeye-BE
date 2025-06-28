package org.re.driving.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.re.car.domain.Car;
import org.re.common.exception.DomainException;
import org.re.driving.converter.DrivingHistoryStatusConverter;
import org.re.driving.exception.DrivingHistoryExceptionCode;
import org.re.mdtlog.converter.TransactionIdConverter;
import org.re.mdtlog.domain.TransactionUUID;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "car_driving_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrivingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false, updatable = false)
    private Car car;

    @Column(nullable = false)
    @Convert(converter = DrivingHistoryStatusConverter.class)
    private DrivingHistoryStatus status;

    @Convert(converter = TransactionIdConverter.class)
    @Column(nullable = false, updatable = false)
    private TransactionUUID txUid;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "mileageSum", column = @Column(name = "previous_mileage_sum")),
        @AttributeOverride(name = "datetime", column = @Column(name = "drive_started_at")),
        @AttributeOverride(name = "location.latitude", column = @Column(name = "previous_latitude")),
        @AttributeOverride(name = "location.longitude", column = @Column(name = "previous_longitude"))
    })
    private DrivingSnapShot previousDrivingSnapShot;

    @Nullable
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "mileageSum", column = @Column(name = "next_mileage_sum")),
        @AttributeOverride(name = "datetime", column = @Column(name = "drive_ended_at")),
        @AttributeOverride(name = "location.latitude", column = @Column(name = "next_latitude")),
        @AttributeOverride(name = "location.longitude", column = @Column(name = "next_longitude"))
    })
    private DrivingSnapShot endDrivingSnapShot;

    private DrivingHistory(
        Car car,
        DrivingHistoryStatus status,
        TransactionUUID txUid,
        DrivingSnapShot previousDrivingSnapShot
    ) {
        this.car = car;
        this.status = status;
        this.txUid = txUid;
        this.previousDrivingSnapShot = previousDrivingSnapShot;
    }

    public static DrivingHistory createNew(Car car, LocalDateTime driveStartAt) {
        var snapshot = DrivingSnapShot.from(car, driveStartAt);
        var txUid = car.getMdtStatus().getActiveTuid();
        return new DrivingHistory(car, DrivingHistoryStatus.DRIVING, txUid, snapshot);
    }

    public void end(Car car, LocalDateTime driveEndAt) {
        if (this.status != DrivingHistoryStatus.DRIVING) {
            throw new DomainException(DrivingHistoryExceptionCode.ALREADY_ENDED);

        }
        this.status = DrivingHistoryStatus.ENDED;
        this.endDrivingSnapShot = DrivingSnapShot.from(car, driveEndAt);
    }
}
