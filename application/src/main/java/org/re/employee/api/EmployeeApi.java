package org.re.employee.api;

import lombok.RequiredArgsConstructor;
import org.re.company.domain.CompanyId;
import org.re.employee.api.payload.EmployeeCreateRequest;
import org.re.employee.api.payload.EmployeeSearchResponse;
import org.re.employee.api.payload.EmployeeStatusChangeRequest;
import org.re.employee.api.payload.EmployeeUpdateRequest;
import org.re.employee.service.EmployeeService;
import org.re.security.userdetails.CompanyUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeApi {

    private final EmployeeService employeeService;

    @GetMapping("/my")
    public EmployeeSearchResponse getMyInfo(CompanyUserDetails userDetails) {
        return EmployeeSearchResponse.from(employeeService.getMyInfo(userDetails));
    }

    @PostMapping
    public void create(CompanyId companyId, @RequestBody EmployeeCreateRequest employeeCreateRequest) {
        employeeService.createNormal(
            companyId,
            employeeCreateRequest.toCredentials(),
            employeeCreateRequest.toMetadata()
        );
    }

    @PutMapping("/{employeeId}")
    public void update(CompanyId companyId, @PathVariable Long employeeId, @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {
        employeeService.update(companyId, employeeId, employeeUpdateRequest.toCommand(), employeeUpdateRequest.status());
    }

    @PatchMapping("/{employeeId}/status")
    public void changeStatus(CompanyId companyId, @PathVariable Long employeeId, @RequestBody EmployeeStatusChangeRequest request) {
        employeeService.changeStatus(companyId, employeeId, request.status());
    }

    @DeleteMapping("/{employeeId}")
    public void delete(CompanyId companyId, @PathVariable Long employeeId) {
        employeeService.delete(companyId, employeeId);
    }

    @GetMapping
    public PagedModel<EmployeeSearchResponse> getAll(CompanyId companyId, Pageable pageable, @RequestParam(required = false) String status) {
        return new PagedModel<>(employeeService.readByStatus(companyId, pageable, status)
            .map(EmployeeSearchResponse::from)
        );
    }

}
