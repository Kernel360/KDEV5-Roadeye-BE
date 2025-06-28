package org.re.company.api;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.re.common.api.payload.PageResponse;
import org.re.company.api.payload.QuoteInfoSimple;
import org.re.company.service.CompanyQuoteService;
import org.re.security.userdetails.PlatformAdminUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/company/quotes")
@RequiredArgsConstructor
public class CompanyQuoteAdminApi {
    private final CompanyQuoteService quoteService;

    @GetMapping
    public PageResponse<QuoteInfoSimple> findAll(Pageable pageable) {
        var page = quoteService.findAll(pageable);
        return PageResponse.of(page, QuoteInfoSimple::from);
    }

    @GetMapping("/{quoteId}")
    public QuoteInfoSimple findById(@PathVariable Long quoteId) {
        var quote = quoteService.findById(quoteId);
        return QuoteInfoSimple.from(quote);
    }

    @PostMapping("/{quoteId}/approve")
    public QuoteInfoSimple approveQuote(
        @NonNull PlatformAdminUserDetails userDetails,
        @PathVariable Long quoteId
    ) {
        var quote = quoteService.approveQuote(userDetails, quoteId);
        return QuoteInfoSimple.from(quote);
    }

    @PostMapping("/{quoteId}/reject")
    public QuoteInfoSimple rejectQuote(
        @NonNull PlatformAdminUserDetails userDetails,
        @PathVariable Long quoteId
    ) {
        var quote = quoteService.rejectQuote(userDetails, quoteId);
        return QuoteInfoSimple.from(quote);
    }
}
