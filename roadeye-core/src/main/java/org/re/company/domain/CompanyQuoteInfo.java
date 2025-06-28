package org.re.company.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyQuoteInfo {
    @Column(nullable = false, length = 50)
    private String companyName;

    @Column(nullable = false)
    private String rootAccountUsername;

    @Column(nullable = false)
    private String rootAccountPassword;

    @Column(nullable = false)
    private String companyEmail;

    @Column(nullable = false)
    private String companyBusinessNumber;

    public CompanyQuoteInfo(
        String companyName,
        String rootAccountUsername,
        String rootAccountPassword,
        String companyEmail,
        String companyBusinessNumber
    ) {
        this.companyName = companyName;
        this.rootAccountUsername = rootAccountUsername;
        this.rootAccountPassword = rootAccountPassword;
        this.companyEmail = companyEmail;
        this.companyBusinessNumber = companyBusinessNumber;
    }

    public CompanyQuoteInfo withPassword(String newPassword) {
        return new CompanyQuoteInfo(
            this.companyName,
            this.rootAccountUsername,
            newPassword,
            this.companyEmail,
            this.companyBusinessNumber
        );
    }
}
