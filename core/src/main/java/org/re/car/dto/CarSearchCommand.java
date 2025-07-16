package org.re.car.dto;

import lombok.Builder;

@Builder
public record CarSearchCommand(
    String name,
    String licenseNumber
) {
}
