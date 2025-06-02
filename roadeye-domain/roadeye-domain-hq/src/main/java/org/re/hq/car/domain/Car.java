package org.re.hq.car.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.hq.car.dto.CarUpdateCommand;
import org.re.hq.company.domain.Company;
import org.re.hq.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Company company;

    @Embedded
    private CarProfile profile;

    @Embedded
    private CarLocation location;

    @Embedded
    private CarMileage mileage;

    @Embedded
    private CarMdtStatus mdtStatus;

    @Column(length = 512)
    private String disableReason;

    private LocalDateTime shippedAt;

    private Car(Company company, CarProfile carProfile, int mileageInitial) {
        this.company = company;
        this.profile = carProfile;
        this.location = CarLocation.create();
        this.mdtStatus = CarMdtStatus.create();
        this.mileage = new CarMileage(mileageInitial);
    }

    public static Car of(Company company, CarProfile carProfile, int mileageInitial) {
        return new Car(company, carProfile, mileageInitial);
    }

    public void disable(String reason) {
        this.disableReason = reason;
        super.disable();
    }

    public void turnOnIgnition(UUID transactionId) {
        this.mdtStatus.turnOnIgnition(transactionId);
    }

    public void turnOffIgnition(UUID transactionId) {
        this.mdtStatus.turnOffIgnition(transactionId);
    }

    public void resetIgnitionStatus() {
        this.mdtStatus.resetIgnitionStatus();
    }

    public void update(CarUpdateCommand command) {
        if (command.name() != null) {
            profile.setName(command.name());
        }
        if (command.imageUrl() != null) {
            profile.setImageUrl(command.imageUrl());
        }
    }
}

