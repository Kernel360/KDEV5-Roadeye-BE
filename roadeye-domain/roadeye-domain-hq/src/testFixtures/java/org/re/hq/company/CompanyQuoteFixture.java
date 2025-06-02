package org.re.hq.company;

import org.re.hq.company.domain.CompanyQuote;

import java.time.LocalDateTime;

public class CompanyQuoteFixture {
    public static CompanyQuote create() {
        var quoteInfo = CompanyQuoteInfoFixture.create();
        var requestedAt = LocalDateTime.now();
        return new CompanyQuote(quoteInfo, requestedAt);
    }
}
