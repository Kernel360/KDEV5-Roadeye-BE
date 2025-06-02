package org.re.hq.car.dto;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.re.hq.car.domain.Car;
import org.re.hq.car.domain.CarProfile;
import org.re.hq.company.domain.Company;

public record CarCreationCommand(
    @NonNull
    String name,
    @NonNull
    String licenseNumber,
    @Nullable
    String imageUrl,
    @NonNull
    Integer mileageInitial
) {
    public Car toEntity(Company company) {
        var carProfile = new CarProfile(name, licenseNumber, imageUrl);
        return Car.of(company, carProfile, mileageInitial);
    }
}
