package org.re.car.api.payload;

import org.jspecify.annotations.Nullable;
import org.re.car.dto.CarUpdateCommand;

public record CarUpdateRequest(
    @Nullable
    String name,
    @Nullable
    String imageUrl
) {
    public CarUpdateCommand toCommand() {
        return new CarUpdateCommand(
            name,
            imageUrl
        );
    }
}
