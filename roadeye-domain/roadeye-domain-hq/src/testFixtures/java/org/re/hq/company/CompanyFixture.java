package org.re.hq.company;

import org.re.hq.company.domain.Company;

public class CompanyFixture {
    public static Company create() {
        var name = "Test Company";
        var businessNumber = "1234567890";
        var email = "test@test.com";
        return new Company(name, businessNumber, email);
    }
}
