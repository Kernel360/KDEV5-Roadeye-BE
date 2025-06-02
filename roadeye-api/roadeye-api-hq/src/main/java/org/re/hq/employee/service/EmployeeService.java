package org.re.hq.employee.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.re.hq.employee.api.payload.AccountStatus;
import org.re.hq.employee.domain.Employee;
import org.re.hq.employee.domain.EmployeeCredentials;
import org.re.hq.employee.domain.EmployeeMetadata;
import org.re.hq.employee.dto.UpdateEmployeeCommand;
import org.re.hq.security.userdetails.CompanyUserDetails;
import org.re.hq.tenant.TenantId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeDomainService employeeDomainService;

    public Employee getMyInfo(CompanyUserDetails userDetails) {
        return read(userDetails.getTenantId(), userDetails.getUserId());
    }

    public Long createRoot(TenantId tenantId, EmployeeCredentials credentials, EmployeeMetadata metadata) {

        employeeDomainService.validateExistsRootAccount(tenantId.value());
        var employee = Employee.createRoot(
            tenantId.value(),
            credentials,
            metadata
        );
        return employeeDomainService.create(employee);
    }

    public Long createNormal(TenantId tenantId, EmployeeCredentials credentials, EmployeeMetadata metadata) {
        var employee = Employee.createNormal(
            tenantId.value(),
            credentials,
            metadata
        );
        return employeeDomainService.create(employee);
    }

    public Employee read(TenantId tenantId, Long employeeId) {
        return employeeDomainService.read(tenantId.value(), employeeId);
    }

    public void changeStatus(TenantId tenantId, Long employeeId, @NonNull AccountStatus status) {
        var employee = employeeDomainService.read(tenantId.value(), employeeId);

        switch (status) {
            case ENABLE -> employee.enable();
            case DISABLE -> employee.disable();
        }
    }

    public void delete(TenantId tenantId, Long employeeId) {
        employeeDomainService.delete(tenantId.value(), employeeId);
    }

    public Page<Employee> readAll(TenantId tenantId, Pageable pageable) {
        return employeeDomainService.readAll(tenantId.value(), pageable);
    }

    public void update(TenantId tenantId, Long employeeId, UpdateEmployeeCommand command) {
        employeeDomainService.updateMetadata(tenantId.value(), employeeId, command);
    }
}
