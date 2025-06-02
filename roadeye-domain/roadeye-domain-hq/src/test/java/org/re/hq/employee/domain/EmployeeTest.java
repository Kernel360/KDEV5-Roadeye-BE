package org.re.hq.employee.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

}
