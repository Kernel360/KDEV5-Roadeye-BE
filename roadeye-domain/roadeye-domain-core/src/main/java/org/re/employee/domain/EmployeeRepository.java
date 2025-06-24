package org.re.employee.domain;

import org.re.common.domain.EntityLifecycleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByTenantId(Long tenantId);

    boolean existsByTenantIdAndCredentialsLoginId(Long tenantId, String loginId);

    Optional<Employee> findByIdAndTenantId(Long id, Long tenantId);

    Page<Employee> findByTenantId(Long tenantId, Pageable pageable);

    Page<Employee> findByTenantIdAndStatus(Long tenantId, EntityLifecycleStatus status, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.credentials.loginId = :username AND e.tenantId = :tenantId")
    Optional<Employee> findByUsernameAndTenantId(Long tenantId, String username);

    @Query("SELECT e FROM Employee e WHERE e.tenantId = :tenantId AND e.role = :role")
    Optional<Employee> findByTenantIdAndRole(Long tenantId, EmployeeRole role);
}
