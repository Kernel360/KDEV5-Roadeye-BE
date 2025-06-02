package org.re.hq.employee.api;

import lombok.RequiredArgsConstructor;
import org.re.hq.employee.api.payload.EmployeeCreateRequest;
import org.re.hq.employee.api.payload.EmployeeSearchResponse;
import org.re.hq.employee.api.payload.EmployeeStatusChangeRequest;
import org.re.hq.employee.api.payload.EmployeeUpdateRequest;
import org.re.hq.employee.service.EmployeeService;
import org.re.hq.security.userdetails.CompanyUserDetails;
import org.re.hq.tenant.TenantId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeApi {

    private final EmployeeService employeeService;

    @GetMapping("/my")
    public EmployeeSearchResponse getMyInfo(CompanyUserDetails userDetails) {
        return EmployeeSearchResponse.from(employeeService.getMyInfo(userDetails));
    }

    @PostMapping
    public void create(TenantId tenantId, @RequestBody EmployeeCreateRequest employeeCreateRequest) {
        employeeService.createNormal(
            tenantId,
            employeeCreateRequest.toCredentials(),
            employeeCreateRequest.toMetadata()
        );
    }

    @PutMapping("/{employeeId}")
    public void update(TenantId tenantId, @PathVariable Long employeeId, @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {
        employeeService.update(tenantId, employeeId, employeeUpdateRequest.toCommand());
    }

    @PatchMapping("/{employeeId}/status")
    public void changeStatus(TenantId tenantId, @PathVariable Long employeeId, @RequestBody EmployeeStatusChangeRequest request) {
        employeeService.changeStatus(tenantId, employeeId, request.status());
    }

    @DeleteMapping("/{employeeId}")
    public void delete(TenantId tenantId, @PathVariable Long employeeId) {
        employeeService.delete(tenantId, employeeId);
    }

    @GetMapping
    public PagedModel<EmployeeSearchResponse> getAll(TenantId tenantId, Pageable pageable) {
        return new PagedModel<>(employeeService.readAll(tenantId, pageable)
            .map(EmployeeSearchResponse::from)
        );
    }

}
