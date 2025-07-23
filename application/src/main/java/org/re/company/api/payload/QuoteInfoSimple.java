package org.re.company.api.payload;

import org.re.company.domain.CompanyQuote;
import org.re.company.domain.CompanyQuoteStatus;

public record QuoteInfoSimple(
    Long id,
    String companyName,
    CompanyQuoteStatus status,
    String rootAccountUsername,
    String companyBusinessNumber,
    String companyEmail
) {
    public static QuoteInfoSimple from(CompanyQuote quote) {
        var info = quote.getQuoteInfo();
        return new QuoteInfoSimple(
            quote.getId(),
            info.getCompanyName(),
            quote.getQuoteStatus(),
            info.getRootAccountUsername(),
            info.getCompanyBusinessNumber(),
            info.getCompanyEmail()
        );
    }
}
