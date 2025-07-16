package org.re.car.api.payload;

import jakarta.validation.constraints.Size;
import org.re.car.dto.CarSearchCommand;
import org.re.validation.ValidSearchRequest;

@ValidSearchRequest
public record CarSearchRequest(
    @Size(max = 100)
    String name,
    @Size(max = 50)
    String licenseNumber
) {
    public CarSearchCommand toCommand() {
        return new CarSearchCommand(name, licenseNumber);
    }
}

