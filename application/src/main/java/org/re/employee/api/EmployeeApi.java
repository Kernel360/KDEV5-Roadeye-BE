package org.re.employee.api;

import lombok.RequiredArgsConstructor;
import org.re.company.domain.CompanyId;
import org.re.employee.api.payload.EmployeeCreateRequest;
import org.re.employee.api.payload.EmployeeInfo;
import org.re.employee.api.payload.EmployeeStatusChangeRequest;
import org.re.employee.api.payload.EmployeeUpdateRequest;
import org.re.employee.service.EmployeeService;
import org.re.security.access.ManagerOnly;
import org.re.security.userdetails.CompanyUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeApi {
    private final EmployeeService employeeService;

    @GetMapping
    public PagedModel<EmployeeInfo> getAll(CompanyId companyId, Pageable pageable, @RequestParam(required = false) String status) {
        return new PagedModel<>(employeeService.readByStatus(companyId, pageable, status)
            .map(EmployeeInfo::from)
        );
    }

    @GetMapping("/{employeeId}")
    public EmployeeInfo getById(CompanyId companyId, @PathVariable Long employeeId) {
        var employee = employeeService.getById(companyId, employeeId);
        return EmployeeInfo.from(employee);
    }

    @GetMapping("/my")
    public EmployeeInfo getMyInfo(CompanyUserDetails userDetails) {
        return EmployeeInfo.from(employeeService.getMyInfo(userDetails));
    }

    @ManagerOnly
    @PostMapping
    public void create(CompanyId companyId, @RequestBody EmployeeCreateRequest employeeCreateRequest) {
        employeeService.createNormal(
            companyId,
            employeeCreateRequest.toCredentials(),
            employeeCreateRequest.toMetadata()
        );
    }

    @ManagerOnly
    @PutMapping("/{employeeId}")
    public void update(CompanyId companyId, @PathVariable Long employeeId, @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {
        employeeService.update(companyId, employeeId, employeeUpdateRequest.toCommand(), employeeUpdateRequest.status());
    }

    @ManagerOnly
    @PatchMapping("/{employeeId}/status")
    public void changeStatus(CompanyId companyId, @PathVariable Long employeeId, @RequestBody EmployeeStatusChangeRequest request) {
        employeeService.changeStatus(companyId, employeeId, request.status());
    }

    @ManagerOnly
    @DeleteMapping("/{employeeId}")
    public void delete(CompanyId companyId, @PathVariable Long employeeId) {
        employeeService.delete(companyId, employeeId);
    }
}
