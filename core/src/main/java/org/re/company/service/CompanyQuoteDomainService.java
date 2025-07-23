package org.re.company.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.re.admin.domain.PlatformAdmin;
import org.re.common.exception.DomainException;
import org.re.common.stereotype.DomainService;
import org.re.company.domain.Company;
import org.re.company.domain.CompanyQuote;
import org.re.company.domain.CompanyQuoteStatus;
import org.re.company.dto.CompanyQuoteRequestCommand;
import org.re.company.exception.CompanyQuoteDomainException;
import org.re.company.repository.CompanyQuoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@DomainService
@Transactional
@RequiredArgsConstructor
public class CompanyQuoteDomainService {
    private final CompanyDomainService companyDomainService;
    private final CompanyQuoteRepository companyQuoteRepository;

    public Page<CompanyQuote> findAll(Pageable pageable) {
        return companyQuoteRepository.findAll(pageable);
    }

    public Page<CompanyQuote> findAllByQuoteStatus(CompanyQuoteStatus status, Pageable pageable) {
        return companyQuoteRepository.findAllByQuoteStatus(status, pageable);
    }

    public CompanyQuote findById(Long id) {
        return companyQuoteRepository.findById(id)
            .orElseThrow(() -> new DomainException(CompanyQuoteDomainException.QUOTE_NOT_FOUND));
    }

    public CompanyQuote requestNewQuote(CompanyQuoteRequestCommand command) {
        if (companyDomainService.isBusinessNumberExists(command.businessNumber())) {
            throw new DomainException(CompanyQuoteDomainException.BUSINESS_NUMBER_EXISTS);
        }
        var quoteInfo = command.toQuoteInfo();
        var requestedAt = LocalDateTime.now();
        var quote = new CompanyQuote(quoteInfo, requestedAt);
        return companyQuoteRepository.save(quote);
    }

    public Company approve(PlatformAdmin approver, CompanyQuote quote) {
        quote.approve(approver);
        return companyDomainService.createCompany(quote);
    }

    public CompanyQuote reject(PlatformAdmin approver, CompanyQuote quote) {
        quote.reject(approver);
        return quote;
    }
}
