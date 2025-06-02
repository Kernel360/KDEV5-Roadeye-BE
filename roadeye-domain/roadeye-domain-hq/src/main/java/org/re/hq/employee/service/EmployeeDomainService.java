package org.re.hq.employee.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.re.hq.employee.domain.*;
import org.re.hq.employee.dto.UpdateEmployeeCommand;
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

    public void validateExistsRootAccount(Long tenantId) {
        if (employeeRepository.existsByTenantId(tenantId)) {
            // TODO 도메인 객체 예외 처리로 바꿀 것
            throw new IllegalStateException("Root account already exists");
        }
    }

    public Employee findCompanyRootAccount(Long companyId) {
        return employeeRepository.findByTenantIdAndRole(companyId, EmployeeRole.ROOT)
            .orElseThrow(() -> new IllegalArgumentException("Root account not found for tenant id: " + companyId));
    }

    /**
     * 업체 계정의 루트 계정을 생성하는 기능
     * <p>
     * 업체의 루트 계정은 하나만 존재해야 함
     */
    public Employee createRootAccount(Long tenantId, EmployeeCredentials credentials, String name, String position) {
        if (employeeRepository.existsByTenantId(tenantId)) {
            // TODO 도메인 객체 예외 처리로 바꿀 것
            throw new IllegalStateException("Root account already exists");
        }

        var employee = Employee.createRoot(tenantId, credentials, EmployeeMetadata.create(name, position));

        return employeeRepository.save(employee);
    }

    /**
     * 일반 계정을 생성하는 도메인 서비스
     */
    public Employee createNormalAccount(Long tenantId, EmployeeCredentials credentials, String name, String position) {
        var employee = Employee.createNormal(tenantId, credentials, EmployeeMetadata.create(name, position));

        return employeeRepository.save(employee);
    }

    /**
     * 회원 정보를 수정한다.
     * <p>
     * 루트 계정만 접근 가능하도록 컨트롤러 부분에서 처리할 것 같지만, 여기서도 권한 검증 기능을 넣어야 할까?
     */
    // TODO teanantId 이름 변경
    public void updateMetadata(Long tenantId, Long employeeId, UpdateEmployeeCommand updateEmployeeCommand) {
        // TODO 예외 객체 생성
        var employee = this.read(tenantId, employeeId);

        employee.update(updateEmployeeCommand);
    }

    // TODO 도메인 서비스 용도로 read라는 메서드를 사용했지만 내부 메서드에서 사용하기에는 정보가 너무 부족하다 별도의 클래스로 분리하는게 나을듯?
    public Employee read(Long tenantId, Long employeeId) {
        return employeeRepository.findByIdAndTenantId(employeeId, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Employee with id " + employeeId + " does not exist"));
    }

    /**
     * TODO 활성화 / 비활성화의 경우 추가 상태에 따른 예외처리를 하도록 할지 고민 중
     */
    public void disable(Long tenantId, Long employeeId) {
        var employee = this.read(tenantId, employeeId);

        employee.disable();
    }

    public void enable(Long tenantId, Long employeeId) {
        var employee = this.read(tenantId, employeeId);

        employee.enable();
    }

    public void delete(Long tenantId, Long employeeId) {
        var employee = this.read(tenantId, employeeId);

        employee.delete();
    }

    public Page<Employee> readAll(Long tenantId, Pageable pageable) {
        return employeeRepository.findByTenantId(tenantId, pageable);
    }
}
