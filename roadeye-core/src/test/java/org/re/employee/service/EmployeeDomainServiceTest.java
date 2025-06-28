package org.re.employee.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.re.common.domain.EntityLifecycleStatus;
import org.re.common.exception.DomainException;
import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.dto.UpdateEmployeeCommand;
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
            .isInstanceOf(DomainException.class);

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
            .forEach((idx) -> {
                var username = "root" + idx;
                var password = "password";
                var credentials = new EmployeeCredentials(username, password);
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
            .mapToObj((idx) -> {
                var username = "root" + idx;
                var password = "password";
                var credentials = new EmployeeCredentials(username, password);
                return employeeDomainService.createNormalAccount(1L, credentials, "root", "root");
            }).toList();

        var employee = employeeDomainService.read(1L, sample.getFirst().getId());
        employeeDomainService.delete(1L, employee.getId());

        var actual = employeeDomainService.readAll(1L, PageRequest.of(0, 10));
        assertThat(actual.getTotalElements()).isEqualTo(29);
        assertThat(actual.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("일반 계정 생성시 로그인 아이디가 중복되면 예외가 발생한다")
    void 일반_계정_생성시_로그인_아이디가_중복되면_예외가_발생한다() {
        var username = "user";
        var password = "password";
        var credentials = new EmployeeCredentials(username, password);

        assertThatThrownBy(() -> {
            employeeDomainService.createNormalAccount(1L, credentials, "User One", "Position One");
            employeeDomainService.createNormalAccount(1L, credentials, "User Two", "Position Two");
        }).isInstanceOf(DomainException.class);
    }
}
