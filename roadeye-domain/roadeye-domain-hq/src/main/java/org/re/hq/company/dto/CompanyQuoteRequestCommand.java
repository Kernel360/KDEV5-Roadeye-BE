package org.re.hq.company.dto;

import org.re.hq.company.domain.CompanyQuoteInfo;

public record CompanyQuoteRequestCommand(
    String name,
    String username,
    String password,
    String email,
    String businessNumber
) {
    public CompanyQuoteInfo toQuoteInfo() {
        return new CompanyQuoteInfo(
            name,
            username,
            password,
            email,
            businessNumber
        );
    }
}
