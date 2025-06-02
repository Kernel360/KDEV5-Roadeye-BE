package org.re.hq.employee.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.re.hq.domain.common.EntityLifecycleStatus;
import org.re.hq.employee.domain.EmployeeCredentials;
import org.re.hq.employee.dto.UpdateEmployeeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest(properties = {"spring.jpa.show-sql=true"})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EmployeeDomainServiceTest {

    @Autowired
    private EmployeeDomainService employeeDomainService;


    @Test
    void 계정_정보를_업데이트_할_때_계정이_존재하지_않는_경우_에러가_발생한다() {
        var updateEmployeeCommand = new UpdateEmployeeCommand("name", "position");

        assertThatThrownBy(() -> employeeDomainService.updateMetadata(1L, 1L, updateEmployeeCommand))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 계정_정보를_비활성화_합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        var employee = employeeDomainService.createNormalAccount(1L, credentials, "root", "root");

        employeeDomainService.disable(employee.getTenantId(), employee.getId());

        System.out.println(employee.getUpdatedAt());
        assertAll(
            () -> assertThat(employee.getStatus()).isEqualTo(EntityLifecycleStatus.DISABLED),
            () -> assertThat(employee.getUpdatedAt()).isNotNull()
        );

    }

    @Test
    void 계정_정보를_활성화_합니다() {
        var credentials = new EmployeeCredentials("root", "root");
        var employee = employeeDomainService.createNormalAccount(1L, credentials, "root", "root");

        employeeDomainService.disable(employee.getTenantId(), employee.getId());
        employeeDomainService.enable(employee.getTenantId(), employee.getId());

        assertAll(
            () -> assertThat(employee.getStatus()).isEqualTo(EntityLifecycleStatus.ACTIVE),
            () -> assertThat(employee.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 계정_정보를_조회합니다() {
        IntStream.iterate(0, i -> i + 1)
            .limit(30)
            .forEach((ignore) -> {
                var credentials = new EmployeeCredentials("root", "root");
                employeeDomainService.createNormalAccount(1L, credentials, "root", "root");
            });

        var actual = employeeDomainService.readAll(1L, PageRequest.of(0, 10));

        assertThat(actual.getTotalElements()).isEqualTo(30);
        assertThat(actual.getSize()).isEqualTo(10);
    }

    @Test
    void 소프트_딜리트로_삭제된_정보는_전체_조회에서_보이지_않는다() {
        var sample = IntStream.iterate(0, i -> i + 1)
            .limit(30)
            .mapToObj((ignore) -> {
                var credentials = new EmployeeCredentials("root", "root");
                return employeeDomainService.createNormalAccount(1L, credentials, "root", "root");
            }).toList();

        var employee = employeeDomainService.read(1L, sample.getFirst().getId());
        employeeDomainService.delete(1L, employee.getId());

        var actual = employeeDomainService.readAll(1L, PageRequest.of(0, 10));
        assertThat(actual.getTotalElements()).isEqualTo(29);
        assertThat(actual.getSize()).isEqualTo(10);
    }

}
