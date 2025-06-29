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

    public Long create(Employee employee) {
        return employeeRepository.save(employee).getId();
    }

    public void validateExistsRootAccount(Long companyId) {
        if (employeeRepository.existsByCompanyId(companyId)) {
            throw new DomainException(EmployeeDomainException.ROOT_ACCOUNT_ALREADY_EXISTS);
        }
    }

    public Employee findCompanyRootAccount(Long companyId) {
        return employeeRepository.findByCompanyIdAndRole(companyId, EmployeeRole.ROOT)
            .orElseThrow(() -> new DomainException(EmployeeDomainException.ROOT_ACCOUNT_NOT_FOUND));
    }

    /**
     * 업체 계정의 루트 계정을 생성하는 기능
     * <p>
     * 업체의 루트 계정은 하나만 존재해야 함
     */
    public Employee createRootAccount(Long companyId, EmployeeCredentials credentials, String name, String position) {
        if (employeeRepository.existsByCompanyId(companyId)) {
            throw new DomainException(EmployeeDomainException.ROOT_ACCOUNT_ALREADY_EXISTS);
        }

        var employee = Employee.createRoot(companyId, credentials, EmployeeMetadata.create(name, position));

        return employeeRepository.save(employee);
    }

    /**
     * 일반 계정을 생성하는 도메인 서비스
     */
    public Employee createNormalAccount(Long companyId, EmployeeCredentials credentials, String name, String position) {
        if (employeeRepository.existsByCompanyIdAndCredentialsLoginId(companyId, credentials.loginId())) {
            throw new DomainException(EmployeeDomainException.USERNAME_DUPLICATED);
        }

        var employee = Employee.createNormal(companyId, credentials, EmployeeMetadata.create(name, position));

        return employeeRepository.save(employee);
    }

    /**
     * 회원 정보를 수정한다.
     * <p>
     * 루트 계정만 접근 가능하도록 컨트롤러 부분에서 처리할 것 같지만, 여기서도 권한 검증 기능을 넣어야 할까?
     */
    // TODO teanantId 이름 변경
    public void updateMetadata(Long companyId, Long employeeId, UpdateEmployeeCommand updateEmployeeCommand) {
        // TODO 예외 객체 생성
        var employee = this.read(companyId, employeeId);

        employee.update(updateEmployeeCommand);
    }

    // TODO 도메인 서비스 용도로 read라는 메서드를 사용했지만 내부 메서드에서 사용하기에는 정보가 너무 부족하다 별도의 클래스로 분리하는게 나을듯?
    public Employee read(Long companyId, Long employeeId) {
        return employeeRepository.findByIdAndCompanyId(employeeId, companyId)
            .orElseThrow(() -> new DomainException(EmployeeDomainException.ACCOUNT_NOT_FOUND));
    }

    /**
     * TODO 활성화 / 비활성화의 경우 추가 상태에 따른 예외처리를 하도록 할지 고민 중
     */
    public void disable(Long companyId, Long employeeId) {
        var employee = this.read(companyId, employeeId);

        employee.disable();
    }

    public void enable(Long companyId, Long employeeId) {
        var employee = this.read(companyId, employeeId);

        employee.enable();
    }

    public void delete(Long companyId, Long employeeId) {
        var employee = this.read(companyId, employeeId);

        employee.delete();
    }

    public Page<Employee> readAll(Long companyId, Pageable pageable) {
        return employeeRepository.findByCompanyId(companyId, pageable);
    }

    public Page<Employee> readByStatus(Long companyId, Pageable pageable, EntityLifecycleStatus status) {
        return employeeRepository.findByCompanyIdAndStatus(companyId, status, pageable);
    }
}
