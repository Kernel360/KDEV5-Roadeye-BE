package org.re.company.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.re.common.exception.DomainException;
import org.re.common.stereotype.DomainService;
import org.re.company.domain.Company;
import org.re.company.domain.CompanyQuote;
import org.re.company.exception.CompanyDomainException;
import org.re.company.repository.CompanyRepository;
import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.domain.EmployeeMetadata;
import org.re.employee.service.EmployeeDomainService;

@DomainService
@Transactional
@RequiredArgsConstructor
public class CompanyDomainService {
    private final EmployeeDomainService employeeService;
    private final CompanyRepository companyRepository;

    public Company findById(Long id) {
        return companyRepository.findById(id)
            .orElseThrow(() -> new DomainException(CompanyDomainException.COMPANY_NOT_FOUND));
    }

    public boolean isBusinessNumberExists(String businessNumber) {
        return companyRepository.existsByBusinessNumber(businessNumber);
    }

    public Company createCompany(CompanyQuote quote) {
        var bisNo = quote.getQuoteInfo().getCompanyBusinessNumber();
        if (isBusinessNumberExists(bisNo)) {
            throw new DomainException(CompanyDomainException.BUSINESS_NUMBER_EXISTS);
        }

        var company = quote.toCompany();
        companyRepository.save(company);
        var credential = EmployeeCredentials.from(quote);
        var meta = EmployeeMetadata.create("Root", "Administrator");
        employeeService.createRootAccount(company.getId(), credential, meta);
        return company;
    }
}
