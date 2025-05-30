package org.re.hq.employee;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.re.hq.employee.domain.EmployeeCredentials;
import org.re.hq.employee.domain.EmployeeMetadata;
import org.re.hq.employee.domain.EmployeeRole;
import org.re.hq.tenant.TenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    private TenantId defaultTenantId;

    @Test
    void 루트_계정을_생성합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        var employeeId = employeeService.createRoot(defaultTenantId, credentials, EmployeeMetadata.create("root", "root"));
        var employee = employeeService.read(defaultTenantId, employeeId);

        assertThat(employee.getRole()).isEqualTo(EmployeeRole.ROOT);
    }

    @Test
    void 루트_계정은_하나만_존재합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        employeeService.createRoot(defaultTenantId, credentials, EmployeeMetadata.create("root", "root"));

        assertThatThrownBy(() -> employeeService.createRoot(defaultTenantId, credentials, EmployeeMetadata.create("root", "root")))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 일반_계정을_생성합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        var employeeId = employeeService.createNormal(defaultTenantId, credentials, EmployeeMetadata.create("root", "root"));

        var employee = employeeService.read(defaultTenantId, employeeId);

        assertThat(employee.getRole()).isEqualTo(EmployeeRole.NORMAL);
    }

}
