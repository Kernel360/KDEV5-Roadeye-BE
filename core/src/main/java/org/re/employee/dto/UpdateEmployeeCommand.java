package org.re.employee.dto;

import org.jspecify.annotations.Nullable;

public record UpdateEmployeeCommand(
    @Nullable
    String name,

    @Nullable
    String position
) {
}
