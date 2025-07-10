package org.re.employee.api;

import lombok.RequiredArgsConstructor;
import org.re.company.domain.CompanyId;
import org.re.employee.api.payload.AccountStatus;
import org.re.employee.api.payload.EmployeeCreateRequest;
import org.re.employee.api.payload.EmployeeInfo;
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
    public PagedModel<EmployeeInfo> getAll(
        @RequestParam(required = false) AccountStatus status,
        Pageable pageable,
        CompanyId companyId
    ) {
        return new PagedModel<>(employeeService.findByStatus(companyId, pageable, status)
            .map(EmployeeInfo::from)
        );
    }

    @GetMapping("/{employeeId}")
    public EmployeeInfo getById(CompanyId companyId, @PathVariable Long employeeId) {
        var employee = employeeService.findById(companyId, employeeId);
        return EmployeeInfo.from(employee);
    }

    @GetMapping("/my")
    public EmployeeInfo getMyInfo(CompanyUserDetails userDetails) {
        return EmployeeInfo.from(employeeService.findByCurrentPrincipal(userDetails));
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
    public void update(
        @PathVariable Long employeeId,
        @RequestBody EmployeeUpdateRequest request,
        CompanyId companyId
    ) {
        employeeService.update(companyId, employeeId, request.toCommand(), request.status());
    }

    @ManagerOnly
    @DeleteMapping("/{employeeId}")
    public void delete(CompanyId companyId, @PathVariable Long employeeId) {
        employeeService.delete(companyId, employeeId);
    }
}
