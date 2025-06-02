package org.re.hq.car.dto;

import org.jspecify.annotations.NonNull;

public record CarDisableCommand(
        @NonNull
        String reason
) {
}
