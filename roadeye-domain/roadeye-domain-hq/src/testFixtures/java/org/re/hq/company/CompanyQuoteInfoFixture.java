package org.re.hq.company;

import org.re.hq.company.domain.CompanyQuoteInfo;

public class CompanyQuoteInfoFixture {
    public static CompanyQuoteInfo create() {
        String name = "Test Company";
        String username = "admin";
        String password = "password";
        String email = "test@test.com";
        String businessNumber = "1234567890";
        return new CompanyQuoteInfo(
            name,
            username,
            password,
            email,
            businessNumber
        );
    }
}
