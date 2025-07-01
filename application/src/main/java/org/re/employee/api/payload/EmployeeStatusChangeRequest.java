package org.re.employee.api.payload;

public record EmployeeStatusChangeRequest(
    AccountStatus status
) {
}
