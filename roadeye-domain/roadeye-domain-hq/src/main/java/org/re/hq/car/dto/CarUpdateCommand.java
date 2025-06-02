package org.re.hq.car.dto;

import org.jspecify.annotations.Nullable;

public record CarUpdateCommand(
        @Nullable
        String name,
        @Nullable
        String imageUrl
) {
}
