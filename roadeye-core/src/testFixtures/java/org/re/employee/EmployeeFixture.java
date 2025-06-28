package org.re.employee;

import org.re.company.domain.Company;
import org.re.employee.domain.Employee;
import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.domain.EmployeeMetadata;

public class EmployeeFixture {
    public static Employee createNormal(Company company) {
        var loginId = "user";
        var rawPassword = "password";
        return createNormal(company, loginId, rawPassword);
    }

    public static Employee createNormal(Company company, String loginId, String rawPassword) {
        return Employee.createNormal(
            company.getId(),
            EmployeeCredentials.create(loginId, "{noop}" + rawPassword),
            EmployeeMetadata.create("name", "normal")
        );
    }

    public static Employee createRoot(Company company) {
        return Employee.createNormal(
            company.getId(),
            EmployeeCredentials.create("loginId", "{noop}password"),
            EmployeeMetadata.create("name", "root")
        );
    }
}
