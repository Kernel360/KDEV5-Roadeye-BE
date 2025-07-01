package org.re.company.exception;

import lombok.Getter;
import org.re.common.exception.DomainExceptionCode;

@Getter
public enum CompanyQuoteDomainException implements DomainExceptionCode {
    // @formatter:off
    QUOTE_NOT_FOUND("100", "Company quote not found"),
    BUSINESS_NUMBER_EXISTS("101", "Business number already exists"),
    QUOTE_STATE_IS_NOT_PENDING("102", "Quote state is not pending"),
    ;
    // @formatter:on

    private static final String PREFIX = "CMQ_";

    private final String code;
    private final String message;

    CompanyQuoteDomainException(String code, String message) {
        this.code = PREFIX + code;
        this.message = message;
    }
}
