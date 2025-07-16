package org.re.employee.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.re.common.api.payload.PageResponse;
import org.re.common.api.payload.SingleItemResponse;
import org.re.company.domain.CompanyId;
import org.re.employee.api.payload.*;
import org.re.employee.service.EmployeeService;
import org.re.security.access.ManagerOnly;
import org.re.security.userdetails.CompanyUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
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

    @GetMapping("/search")
    public PageResponse<EmployeeInfo> search(
        @Valid EmployeeSearchRequest request,
        Pageable pageable,
        CompanyId companyId
    ) {
        var page = employeeService.search(companyId, request, pageable);
        return PageResponse.of(page, EmployeeInfo::from);
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

    @GetMapping("/status/count")
    public SingleItemResponse<EmployeeStatusCount> count(CompanyId companyId) {
        var data = employeeService.getEmployeeStatusCount(companyId);
        return SingleItemResponse.of(data);
    }
}
