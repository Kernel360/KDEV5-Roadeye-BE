package org.re.hq.employee.api.payload;

import org.re.hq.employee.domain.Employee;

public record EmployeeSearchResponse(
    Long employeeId,
    Long tenantId,
    String loginId,
    String name,
    String password
) {

    public static EmployeeSearchResponse from(Employee employee) {
        return new EmployeeSearchResponse(
            employee.getId(),
            employee.getTenantId(),
            employee.getCredentials().loginId(),
            employee.getMetadata().getName(),
            employee.getMetadata().getPosition()
        );
    }
}
