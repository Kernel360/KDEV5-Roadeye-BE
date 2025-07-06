package org.re.employee.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.re.common.exception.DomainException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void 루트_계정의_직원을_생성합니다() {
        var employee = Employee.createRoot(
            1L,
            EmployeeCredentials.create("loginId", "password"),
            EmployeeMetadata.create("name", "password")
        );

        assertThat(employee.getRole()).isEqualTo(EmployeeRole.ROOT);
    }

    @Test
    void 일반_계정의_직원을_생성합니다() {
        var employee = Employee.createNormal(
            1L,
            EmployeeCredentials.create("loginId", "password"),
            EmployeeMetadata.create("name", "password")
        );

        assertThat(employee.getRole()).isEqualTo(EmployeeRole.NORMAL);
    }


    @Test
    @DisplayName("ROOT 계정은 비활성화 할 수 없다.")
    void rootAccountCannotBeDisabled() {
        var employee = Employee.createRoot(
            1L,
            EmployeeCredentials.create("loginId", "password"),
            EmployeeMetadata.create("name", "password")
        );

        assertThrows(
            DomainException.class, () -> {
                employee.disable();
            }
        );
    }
}
