package org.re.employee.api.payload;

import org.re.employee.domain.Employee;
import org.re.employee.domain.EmployeeRole;

import java.time.LocalDateTime;

public record EmployeeSearchResponse(
    Long employeeId,
    Long companyId,
    String loginId,
    String name,
    EmployeeRole role,
    String position,
    LocalDateTime createdAt,
    String status
) {

    public static EmployeeSearchResponse from(Employee employee) {
        return new EmployeeSearchResponse(
            employee.getId(),
            employee.getCompanyId(),
            employee.getCredentials().loginId(),
            employee.getMetadata().getName(),
            employee.getRole(),
            employee.getMetadata().getPosition(),
            employee.getCreatedAt(),
            employee.getStatus().name()
        );
    }
}
