package org.re.employee.api.payload;

import org.re.employee.dto.UpdateEmployeeCommand;

public record EmployeeUpdateRequest(
    String name,
    String position,
    AccountStatus status
) {
    public UpdateEmployeeCommand toCommand() {
        return new UpdateEmployeeCommand(name, position);
    }
}
