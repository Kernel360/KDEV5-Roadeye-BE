package org.re.hq.company.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.re.hq.company.domain.Company;
import org.re.hq.company.domain.CompanyQuote;
import org.re.hq.company.exception.CompanyDomainException;
import org.re.hq.company.repository.CompanyRepository;
import org.re.hq.domain.exception.DomainException;
import org.re.hq.employee.domain.EmployeeCredentials;
import org.re.hq.employee.service.EmployeeDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final EmployeeDomainService employeeService;
    private final CompanyRepository companyRepository;

    public Page<Company> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

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
        employeeService.createRootAccount(company.getId(), credential, "Root", "Administrator");
        return company;
    }
}
