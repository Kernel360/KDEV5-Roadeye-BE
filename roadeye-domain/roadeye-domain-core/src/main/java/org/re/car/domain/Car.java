package org.re.car.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.car.dto.CarUpdateCommand;
import org.re.common.domain.BaseEntity;
import org.re.company.domain.Company;

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
    private org.re.car.domain.CarProfile profile;

    @Embedded
    private org.re.car.domain.CarLocation location;

    @Embedded
    private org.re.car.domain.CarMileage mileage;

    @Embedded
    private org.re.car.domain.CarMdtStatus mdtStatus;

    @Column(length = 512)
    private String disableReason;

    private LocalDateTime shippedAt;

    private Car(Company company, org.re.car.domain.CarProfile carProfile, int mileageInitial) {
        this.company = company;
        this.profile = carProfile;
        this.location = org.re.car.domain.CarLocation.create();
        this.mdtStatus = org.re.car.domain.CarMdtStatus.create();
        this.mileage = new org.re.car.domain.CarMileage(mileageInitial);
    }

    public static Car of(Company company, org.re.car.domain.CarProfile carProfile, int mileageInitial) {
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

