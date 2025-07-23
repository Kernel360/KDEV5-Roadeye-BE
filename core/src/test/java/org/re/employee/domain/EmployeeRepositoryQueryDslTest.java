package org.re.employee.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.re.config.QueryDslConfig;
import org.re.employee.dto.EmployeeSearchCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
    QueryDslConfig.class
})
@DataJpaTest
class EmployeeRepositoryQueryDslTest {
    @Autowired
    EmployeeRepository repository;

    @Test
    @DisplayName("이름으로 검색이 가능해야 한다.")
    void search_by_name_returns_correct_employee() {
        // given
        var employee = Employee.createNormal(
            1L,
            new EmployeeCredentials("username", "password"),
            EmployeeMetadata.create("name", "pos")
        );
        repository.save(employee);

        var command = EmployeeSearchCommand.builder()
            .name("name")
            .build();
        var result = repository.search(1L, command, PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getMetadata().getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("역할로 검색이 가능해야 한다.")
    void search_by_role_returns_correct_employee() {
        var employee = Employee.createNormal(
            1L,
            new EmployeeCredentials("username", "password"),
            EmployeeMetadata.create("name", "pos")
        );
        repository.save(employee);

        var command = EmployeeSearchCommand.builder()
            .role("NORMAL")
            .build();
        var result = repository.search(1L, command, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getRole().name()).isEqualTo("NORMAL");
    }

    @Test
    @DisplayName("직책으로 검색이 가능해야 한다.")
    void search_by_position_returns_correct_employee() {
        var employee = Employee.createNormal(
            1L,
            new EmployeeCredentials("username", "password"),
            EmployeeMetadata.create("name", "pos")
        );
        repository.save(employee);

        var command = EmployeeSearchCommand.builder()
            .position("pos")
            .build();
        var result = repository.search(1L, command, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getMetadata().getPosition()).isEqualTo("pos");
    }

    @Test
    @DisplayName("사용자명으로 검색이 가능해야 한다.")
    void search_by_username_returns_correct_employee() {
        var employee = Employee.createNormal(
            1L,
            new EmployeeCredentials("username", "password"),
            EmployeeMetadata.create("name", "pos")
        );
        repository.save(employee);

        var command = EmployeeSearchCommand.builder()
            .username("username")
            .build();
        var result = repository.search(1L, command, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getCredentials().loginId()).isEqualTo("username");
    }
}