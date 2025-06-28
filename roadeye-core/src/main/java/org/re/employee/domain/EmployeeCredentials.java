package org.re.employee.domain;

import jakarta.persistence.Embeddable;
import org.re.company.domain.CompanyQuote;

@Embeddable
public record EmployeeCredentials(
    String loginId,
    String password
) {
    public static EmployeeCredentials from(CompanyQuote quote) {
        return new EmployeeCredentials(
            quote.getQuoteInfo().getRootAccountUsername(),
            quote.getQuoteInfo().getRootAccountPassword()
        );
    }

    public static EmployeeCredentials create(String loginId, String password) {
        return new EmployeeCredentials(loginId, password);
    }

    public EmployeeCredentials withPassword(String password) {
        return new EmployeeCredentials(loginId, password);
    }
}
