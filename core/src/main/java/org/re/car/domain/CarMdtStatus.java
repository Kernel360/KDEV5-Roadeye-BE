package org.re.car.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.re.car.converter.CarIgnitionStatusConverter;
import org.re.car.exception.CarDomainException;
import org.re.common.exception.DomainException;
import org.re.mdtlog.converter.MdtLogGpsConditionConverter;
import org.re.mdtlog.domain.MdtLogGpsCondition;
import org.re.util.Integers;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarMdtStatus {
    @Setter(AccessLevel.PACKAGE)
    @Column(nullable = false)
    private int batteryVoltage;

    @Setter(AccessLevel.PACKAGE)
    @Column(nullable = false)
    private int angle;

    @Setter(AccessLevel.PACKAGE)
    @Column(nullable = false)
    private int speed;

    @Column(nullable = false)
    private int mileageInitial;

    @Setter(AccessLevel.PACKAGE)
    @Column(nullable = false)
    private int mileageSum;

    @Setter(AccessLevel.PACKAGE)
    @Embedded
    private CarLocation location = CarLocation.create();

    @Setter(AccessLevel.PACKAGE)
    @Convert(converter = MdtLogGpsConditionConverter.class)
    @Column(nullable = false)
    private MdtLogGpsCondition gpsCondition = MdtLogGpsCondition.NOT_ATTACHED;

    @Setter(AccessLevel.PACKAGE)
    @Column
    private LocalDateTime ignitionOnTime;

    @Setter(AccessLevel.PACKAGE)
    @Column
    private LocalDateTime ignitionOffTime;

    @Convert(converter = CarIgnitionStatusConverter.class)
    @Column(nullable = false)
    private CarIgnitionStatus ignition;

    @Column(columnDefinition = "BINARY(16)")
    private UUID activeTuid;

    public CarMdtStatus(int batteryVoltage, CarIgnitionStatus ignition, UUID txid) {
        this.batteryVoltage = batteryVoltage;
        this.ignition = ignition;
        this.activeTuid = txid;
    }

    public static CarMdtStatus create() {
        return new CarMdtStatus(Integers.ZERO, CarIgnitionStatus.OFF, null);
    }

    public void turnOnIgnition(UUID txid) {
        this.ignition = CarIgnitionStatus.ON;
        this.activeTuid = txid;
    }

    public void turnOffIgnition(UUID txid) {
        if (!Objects.equals(this.activeTuid, txid)) {
            throw new DomainException(CarDomainException.TRANSACTION_ID_MISMATCH);
        }
        this.ignition = CarIgnitionStatus.OFF;
        this.activeTuid = null;
    }

    public void resetIgnitionStatus() {
        this.ignition = CarIgnitionStatus.OFF;
        this.activeTuid = null;
    }
}
