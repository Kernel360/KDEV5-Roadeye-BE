package org.re.hq.car.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.hq.car.converter.CarIgnitionStatusConverter;
import org.re.util.Integers;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarMdtStatus {
    @Column(nullable = false)
    private int batteryVoltage;

    @Convert(converter = CarIgnitionStatusConverter.class)
    @Column(nullable = false)
    private CarIgnitionStatus ignition;

    @Column(columnDefinition = "BINARY(16)")
    private UUID activeTuid;

    public CarMdtStatus(int batteryVoltage, CarIgnitionStatus ignition, UUID activeTuid) {
        this.batteryVoltage = batteryVoltage;
        this.ignition = ignition;
        this.activeTuid = activeTuid;
    }

    public static CarMdtStatus create() {
        return new CarMdtStatus(Integers.ZERO, CarIgnitionStatus.OFF, null);
    }

    public void turnOnIgnition(UUID transactionId) {
        if (this.ignition != CarIgnitionStatus.OFF) {
            throw new IllegalStateException("Ignition is already ON or in an invalid state.");
        }
        this.ignition = CarIgnitionStatus.ON;
        this.activeTuid = transactionId;
    }

    public void turnOffIgnition(UUID transactionId) {
        if (this.ignition != CarIgnitionStatus.ON) {
            throw new IllegalStateException("Ignition is already OFF or in an invalid state.");
        }
        if (!this.activeTuid.equals(transactionId)) {
            throw new IllegalArgumentException("Transaction ID does not match the active transaction.");
        }
        this.ignition = CarIgnitionStatus.OFF;
        this.activeTuid = null;
    }

    public void resetIgnitionStatus() {
        this.ignition = CarIgnitionStatus.OFF;
        this.activeTuid = null;
    }
}

