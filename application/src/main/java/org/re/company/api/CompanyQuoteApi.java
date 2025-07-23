package org.re.company.api;

import lombok.RequiredArgsConstructor;
import org.re.common.api.payload.SingleItemResponse;
import org.re.company.api.payload.CompanyQuoteCreationRequest;
import org.re.company.api.payload.QuoteInfoSimple;
import org.re.company.service.CompanyQuoteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company/quotes")
@RequiredArgsConstructor
public class CompanyQuoteApi {
    private final CompanyQuoteService quoteService;

    @PostMapping
    public SingleItemResponse<QuoteInfoSimple> createQuote(
        @RequestBody CompanyQuoteCreationRequest request
    ) {
        var quote = quoteService.createQuote(request);
        return SingleItemResponse.of(quote, QuoteInfoSimple::from);
    }
}
