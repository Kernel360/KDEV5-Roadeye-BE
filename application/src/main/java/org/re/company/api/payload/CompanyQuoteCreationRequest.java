package org.re.company.api.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.re.company.dto.CompanyQuoteRequestCommand;

public record CompanyQuoteCreationRequest(
    @NotBlank
    String companyName,
    @NotBlank
    String rootAccountUsername,
    @NotBlank
    String rootAccountPassword,
    @Email
    String companyEmail,
    @NotBlank
    String companyBusinessNumber
) {
    public CompanyQuoteRequestCommand toCommand() {
        return new CompanyQuoteRequestCommand(
            companyName,
            rootAccountUsername,
            rootAccountPassword,
            companyEmail,
            companyBusinessNumber
        );
    }
}
