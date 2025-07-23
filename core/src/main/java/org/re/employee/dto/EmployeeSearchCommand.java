package org.re.employee.dto;

import lombok.Builder;


@Builder
public record EmployeeSearchCommand(
    String username,
    String name,
    String role,
    String position
) {
}
