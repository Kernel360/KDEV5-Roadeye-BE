package org.re.hq.employee.api.payload;

import org.re.hq.employee.dto.UpdateEmployeeCommand;

public record EmployeeUpdateRequest(
    String name,
    String position
) {

    public UpdateEmployeeCommand toCommand() {
        return new UpdateEmployeeCommand(name, position);
    }
}
