package org.re.hq.company.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyQuoteStatus {
    // @formatter:off
    PENDING  (1),
    APPROVED (2),
    REJECTED (3),
    ;
    // @formatter:on

    private final int code;

    public static CompanyQuoteStatus of(int code) {
        for (CompanyQuoteStatus status : values()) {
            if (status.ordinal() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown CompanyQuoteStatus code: " + code);
    }
}
