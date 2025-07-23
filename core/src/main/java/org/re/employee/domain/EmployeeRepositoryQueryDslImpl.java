package org.re.employee.domain;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.re.employee.dto.EmployeeSearchCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryQueryDslImpl implements EmployeeRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Employee> search(Long companyId, EmployeeSearchCommand command, Pageable pageable) {
        var employee = QEmployee.employee;

        var builder = new BooleanBuilder();
        builder.and(employee.companyId.eq(companyId));
        if (command.name() != null && !command.name().isEmpty()) {
            builder.and(employee.metadata.name.containsIgnoreCase(command.name()));
        }
        if (command.role() != null && !command.role().isEmpty()) {
            builder.and(employee.role.stringValue().eq(command.role()));
        }
        if (command.position() != null && !command.position().isEmpty()) {
            builder.and(employee.metadata.position.containsIgnoreCase(command.position()));
        }
        if (command.username() != null && !command.username().isEmpty()) {
            builder.and(employee.credentials.loginId.containsIgnoreCase(command.username()));
        }
        if (builder.getValue() == null) {
            return Page.empty(pageable);
        }
        var content = queryFactory.selectFrom(employee)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        long total = queryFactory.selectFrom(employee)
            .where(builder)
            .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }
}
