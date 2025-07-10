package org.re.employee.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.re.company.domain.CompanyId;
import org.re.employee.api.payload.AccountStatus;
import org.re.employee.domain.Employee;
import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.domain.EmployeeMetadata;
import org.re.employee.dto.UpdateEmployeeCommand;
import org.re.security.userdetails.CompanyUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeDomainService employeeDomainService;
    private final PasswordEncoder passwordEncoder;

    public Employee findById(CompanyId companyId, Long employeeId) {
        return employeeDomainService.findById(companyId.value(), employeeId);
    }

    public Employee findByCurrentPrincipal(CompanyUserDetails userDetails) {
        return findById(userDetails.getCompanyId(), userDetails.getUserId());
    }

    public Page<Employee> findByStatus(CompanyId companyId, Pageable pageable, AccountStatus status) {
        if (status == null)
            return employeeDomainService.findAllInCompany(companyId.value(), pageable);
        var entityStatus = status.toEntityLifecycleStatus(status);
        return employeeDomainService.findAllInCompany(companyId.value(), pageable, entityStatus);
    }

    public Long createRoot(CompanyId companyId, EmployeeCredentials credentials, EmployeeMetadata metadata) {
        var encodedCredentials = credentials.withPassword(passwordEncoder.encode(credentials.password()));
        var entity = employeeDomainService.createRootAccount(companyId.value(), encodedCredentials, metadata);
        return entity.getId();
    }

    public Long createNormal(CompanyId companyId, EmployeeCredentials credentials, EmployeeMetadata metadata) {
        var encodedCredentials = credentials.withPassword(passwordEncoder.encode(credentials.password()));
        var entity = employeeDomainService.createNormalAccount(companyId.value(), encodedCredentials, metadata);
        return entity.getId();
    }

    public void update(CompanyId companyId, Long employeeId, UpdateEmployeeCommand command, @Nullable AccountStatus status) {
        var employee = employeeDomainService.findById(companyId.value(), employeeId);

        employeeDomainService.update(employee, command);
        if (status != null) {
            switch (status) {
                case ENABLE -> employee.enable();
                case DISABLE -> employee.disable();
            }
        }
    }

    public void delete(CompanyId companyId, Long employeeId) {
        var employee = employeeDomainService.findById(companyId.value(), employeeId);
        employeeDomainService.delete(employee);
    }
}
