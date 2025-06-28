package org.re.company.api.payload;

public class CompanyQuoteCreationRequestFixture {
    public static CompanyQuoteCreationRequest create() {
        var name = "Test Company";
        var username = "admin";
        var password = "password";
        var email = "test@test.org";
        var businessNumber = "1234567890";
        return new CompanyQuoteCreationRequest(
            name,
            username,
            password,
            email,
            businessNumber
        );
    }
}
