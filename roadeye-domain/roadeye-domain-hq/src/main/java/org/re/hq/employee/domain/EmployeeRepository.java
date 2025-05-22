package org.re.hq.employee.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByTenantId(Long tenantId);

    Optional<Employee> findByIdAndTenantId(Long id, Long tenantId);
}
