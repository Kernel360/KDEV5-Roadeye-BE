package org.re.employee.domain;

import org.re.employee.dto.EmployeeSearchCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeRepositoryQueryDsl {
    Page<Employee> search(Long companyId, EmployeeSearchCommand command, Pageable pageable);
}

