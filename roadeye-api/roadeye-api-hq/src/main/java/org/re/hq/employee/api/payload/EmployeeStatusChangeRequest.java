package org.re.hq.employee.api.payload;

public record EmployeeStatusChangeRequest(
    AccountStatus status
) {
}
