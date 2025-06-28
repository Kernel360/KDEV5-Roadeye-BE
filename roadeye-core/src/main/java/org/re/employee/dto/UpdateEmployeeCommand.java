package org.re.employee.dto;

public record UpdateEmployeeCommand(
    String name,
    String position
) {
}
