package org.re.employee.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.re.common.domain.EntityLifecycleStatus;
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

    public Employee getMyInfo(CompanyUserDetails userDetails) {
        return read(userDetails.getCompanyId(), userDetails.getUserId());
    }

    public Long createRoot(CompanyId companyId, EmployeeCredentials credentials, EmployeeMetadata metadata) {
        employeeDomainService.validateExistsRootAccount(companyId.value());

        var employee = Employee.createRoot(
            companyId.value(),
            credentials.withPassword(passwordEncoder.encode(credentials.password())),
            metadata
        );
        return employeeDomainService.create(employee);
    }

    public Long createNormal(CompanyId companyId, EmployeeCredentials credentials, EmployeeMetadata metadata) {
        var employee = Employee.createNormal(
            companyId.value(),
            credentials.withPassword(passwordEncoder.encode(credentials.password())),
            metadata
        );
        return employeeDomainService.create(employee);
    }

    public Employee read(CompanyId companyId, Long employeeId) {
        return employeeDomainService.read(companyId.value(), employeeId);
    }

    public void changeStatus(CompanyId companyId, Long employeeId, @NonNull AccountStatus status) {
        var employee = employeeDomainService.read(companyId.value(), employeeId);

        switch (status) {
            case ENABLE -> employee.enable();
            case DISABLE -> employee.disable();
        }
    }

    public void delete(CompanyId companyId, Long employeeId) {
        employeeDomainService.delete(companyId.value(), employeeId);
    }

    public Page<Employee> readAll(CompanyId companyId, Pageable pageable) {
        return employeeDomainService.readAll(companyId.value(), pageable);
    }

    public Page<Employee> readByStatus(CompanyId companyId, Pageable pageable, String status) {
        if (status == null)
            return employeeDomainService.readAll(companyId.value(), pageable);
        return employeeDomainService.readByStatus(companyId.value(), pageable, EntityLifecycleStatus.valueOf(status));
    }

    public void update(CompanyId companyId, Long employeeId, UpdateEmployeeCommand command, AccountStatus status) {
        employeeDomainService.updateMetadata(companyId.value(), employeeId, command);
        switch (status) {
            case ENABLE -> employeeDomainService.enable(companyId.value(), employeeId);
            case DISABLE -> employeeDomainService.disable(companyId.value(), employeeId);
        }
    }
}
