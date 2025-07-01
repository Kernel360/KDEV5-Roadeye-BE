package org.re.employee.api.payload;

import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.domain.EmployeeMetadata;

public record EmployeeCreateRequest(
    String loginId,
    String password,
    String name,
    String position
) {

    public EmployeeCredentials toCredentials() {
        return EmployeeCredentials.create(loginId, password);
    }

    public EmployeeMetadata toMetadata() {
        return EmployeeMetadata.create(name, password);
    }
}
