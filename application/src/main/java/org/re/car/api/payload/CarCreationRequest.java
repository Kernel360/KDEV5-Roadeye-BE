package org.re.car.api.payload;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.re.car.dto.CarCreationCommand;

public record CarCreationRequest(
    @NonNull
    String name,
    @NonNull
    String licenseNumber,
    @Nullable
    String imageUrl,
    @NonNull
    Integer mileageInitial
) {
    public CarCreationCommand toCommand() {
        return new CarCreationCommand(
            name,
            licenseNumber,
            imageUrl,
            mileageInitial
        );
    }
}
