package org.re.employee.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.re.common.domain.EntityLifecycleStatus;
import org.re.common.exception.DomainException;
import org.re.employee.domain.*;
import org.re.employee.dto.UpdateEmployeeCommand;
import org.re.employee.exception.EmployeeDomainException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeDomainService {
    private final EmployeeRepository employeeRepository;

    public Page<Employee> findAllInCompany(Long companyId, Pageable pageable) {
        return employeeRepository.findByCompanyId(companyId, pageable);
    }

    public Page<Employee> findAllInCompany(Long companyId, Pageable pageable, EntityLifecycleStatus status) {
        return employeeRepository.findByCompanyIdAndStatus(companyId, status, pageable);
    }

    public Employee findById(Long companyId, Long employeeId) {
        return employeeRepository.findByIdAndCompanyId(employeeId, companyId)
            .orElseThrow(() -> new DomainException(EmployeeDomainException.ACCOUNT_NOT_FOUND));
    }

    public Employee findRootAccountInCompany(Long companyId) {
        return employeeRepository.findByCompanyIdAndRole(companyId, EmployeeRole.ROOT)
            .orElseThrow(() -> new DomainException(EmployeeDomainException.ROOT_ACCOUNT_NOT_FOUND));
    }

    public int countAllByStatus(Long companyId, EntityLifecycleStatus status) {
        return employeeRepository.countAllByCompanyIdAndStatus(companyId, status);
    }

    public int countAllByPosition(Long companyId, String position) {
        return employeeRepository.countAllByCompanyIdAndMetadata_Position(companyId, position);
    }

    public Employee createRootAccount(Long companyId, EmployeeCredentials credentials, EmployeeMetadata metadata) {
        if (employeeRepository.existsByCompanyId(companyId)) {
            throw new DomainException(EmployeeDomainException.ROOT_ACCOUNT_ALREADY_EXISTS);
        }

        var employee = Employee.createRoot(companyId, credentials, metadata);
        return employeeRepository.save(employee);
    }

    public Employee createNormalAccount(Long companyId, EmployeeCredentials credentials, EmployeeMetadata metadata) {
        if (employeeRepository.existsByCompanyIdAndCredentialsLoginId(companyId, credentials.loginId())) {
            throw new DomainException(EmployeeDomainException.USERNAME_DUPLICATED);
        }

        var employee = Employee.createNormal(companyId, credentials, metadata);
        return employeeRepository.save(employee);
    }

    public void update(Employee employee, UpdateEmployeeCommand command) {
        employee.update(command);
    }

    public void disable(Employee employee) {
        employee.disable();
    }

    public void enable(Employee employee) {
        employee.enable();
    }

    public void delete(Employee employee) {
        employee.delete();
    }
}
