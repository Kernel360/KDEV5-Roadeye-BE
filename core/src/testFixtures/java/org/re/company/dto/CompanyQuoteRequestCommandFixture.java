package org.re.company.dto;

public class CompanyQuoteRequestCommandFixture {
    public static CompanyQuoteRequestCommand create() {
        String name = "Test Company";
        String username = "admin";
        String password = "password";
        String email = "test@test.com";
        String businessNumber = "1234567890";
        return new CompanyQuoteRequestCommand(
            name,
            username,
            password,
            email,
            businessNumber
        );
    }

    public static CompanyQuoteRequestCommand createWithBusinessNumber(String businessNumber) {
        String name = "Test Company";
        String username = "admin";
        String password = "password";
        String email = "test@test.com";
        return new CompanyQuoteRequestCommand(
            name,
            username,
            password,
            email,
            businessNumber
        );
    }
}
