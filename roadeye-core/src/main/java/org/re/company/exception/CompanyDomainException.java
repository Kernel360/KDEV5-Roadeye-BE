package org.re.company.exception;

import lombok.Getter;
import org.re.common.exception.DomainExceptionCode;

@Getter
public enum CompanyDomainException implements DomainExceptionCode {
    // @formatter:off
    COMPANY_NOT_FOUND("100", "Company not found"),
    BUSINESS_NUMBER_EXISTS("101", "Business number already exists"),
    ;
    // @formatter:on

    private static final String PREFIX = "COM_";

    private final String code;
    private final String message;

    CompanyDomainException(String code, String message) {
        this.code = PREFIX + code;
        this.message = message;
    }
}
