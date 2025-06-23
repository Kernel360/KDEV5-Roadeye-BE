package org.re.company;

import org.re.company.domain.CompanyQuoteInfo;

public class CompanyQuoteInfoFixture {
    public static CompanyQuoteInfo create() {
        String businessNumber = "1234567890";
        return createWithBisNo(businessNumber);
    }

    public static CompanyQuoteInfo createWithBisNo(String businessNumber) {
        String name = "Test Company";
        String username = "admin";
        String password = "{noop}password";
        String email = "test@test.com";
        return new CompanyQuoteInfo(
            name,
            username,
            password,
            email,
            businessNumber
        );
    }
}
