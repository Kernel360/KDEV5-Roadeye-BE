package org.re.employee.api.payload;

import org.re.employee.dto.EmployeeSearchCommand;
import org.re.validation.ValidSearchRequest;

@ValidSearchRequest
public record EmployeeSearchRequest(
    String name,
    String role,
    String position,
    String username
) {
    public EmployeeSearchCommand toCommand() {
        return new EmployeeSearchCommand(name, role, position, username);
    }
}

