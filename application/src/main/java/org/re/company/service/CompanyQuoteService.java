package org.re.company.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.re.admin.service.PlatformAdminService;
import org.re.company.api.payload.CompanyQuoteCreationRequest;
import org.re.company.domain.CompanyQuote;
import org.re.security.userdetails.PlatformAdminUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyQuoteService {
    private final CompanyQuoteDomainService quoteDomainService;
    private final PlatformAdminService platformAdminService;

    private final PasswordEncoder passwordEncoder;

    public Page<CompanyQuote> findAll(Pageable pageable) {
        return quoteDomainService.findAll(pageable);
    }

    public CompanyQuote createQuote(CompanyQuoteCreationRequest request) {
        var encodedPassword = passwordEncoder.encode(request.rootAccountPassword());
        var command = request.toCommand().withPassword(encodedPassword);
        return quoteDomainService.requestNewQuote(command);
    }

    public CompanyQuote findById(Long quoteId) {
        return quoteDomainService.findById(quoteId);
    }

    public CompanyQuote approveQuote(PlatformAdminUserDetails userDetails, Long quoteId) {
        var quote = quoteDomainService.findById(quoteId);
        var approver = platformAdminService.findById(userDetails.getId());
        quoteDomainService.approve(approver, quote);
        return quote;
    }

    public CompanyQuote rejectQuote(PlatformAdminUserDetails userDetails, Long quoteId) {
        var quote = quoteDomainService.findById(quoteId);
        var approver = platformAdminService.findById(userDetails.getId());
        quoteDomainService.reject(approver, quote);
        return quote;
    }
}
