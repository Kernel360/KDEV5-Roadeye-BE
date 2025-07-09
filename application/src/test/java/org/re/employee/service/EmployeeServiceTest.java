package org.re.employee.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.re.common.exception.DomainException;
import org.re.company.domain.CompanyId;
import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.domain.EmployeeMetadata;
import org.re.employee.domain.EmployeeRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class EmployeeServiceTest {

    private final CompanyId defaultCompanyId = new CompanyId(1L);

    @Autowired
    private EmployeeService employeeService;

    @Test
    void 루트_계정을_생성합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        var employeeId = employeeService.createRoot(defaultCompanyId, credentials, EmployeeMetadata.create("root", "root"));
        var employee = employeeService.findById(defaultCompanyId, employeeId);

        assertThat(employee.getRole()).isEqualTo(EmployeeRole.ROOT);
    }

    @Test
    void 루트_계정은_하나만_존재합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        employeeService.createRoot(defaultCompanyId, credentials, EmployeeMetadata.create("root", "root"));

        assertThatThrownBy(() -> employeeService.createRoot(defaultCompanyId, credentials, EmployeeMetadata.create("root", "root")))
            .isInstanceOf(DomainException.class);
    }

    @Test
    void 일반_계정을_생성합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        var employeeId = employeeService.createNormal(defaultCompanyId, credentials, EmployeeMetadata.create("root", "root"));

        var employee = employeeService.findById(defaultCompanyId, employeeId);

        assertThat(employee.getRole()).isEqualTo(EmployeeRole.NORMAL);
    }

}
