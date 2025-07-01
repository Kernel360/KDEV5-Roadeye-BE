package org.re.employee.domain;

import org.re.common.domain.EntityLifecycleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByCompanyId(Long companyId);

    boolean existsByCompanyIdAndCredentialsLoginId(Long companyId, String loginId);

    Optional<Employee> findByIdAndCompanyId(Long id, Long companyId);

    Page<Employee> findByCompanyId(Long companyId, Pageable pageable);

    Page<Employee> findByCompanyIdAndStatus(Long companyId, EntityLifecycleStatus status, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.credentials.loginId = :username AND e.companyId = :companyId")
    Optional<Employee> findByUsernameAndCompanyId(Long companyId, String username);

    @Query("SELECT e FROM Employee e WHERE e.companyId = :companyId AND e.role = :role")
    Optional<Employee> findByCompanyIdAndRole(Long companyId, EmployeeRole role);
}
