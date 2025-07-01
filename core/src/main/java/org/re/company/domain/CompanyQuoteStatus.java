package org.re.company.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.re.common.domain.PersistenceEnum;

@Getter
@RequiredArgsConstructor
public enum CompanyQuoteStatus implements PersistenceEnum<Integer> {
    // @formatter:off
    PENDING  (1),
    APPROVED (2),
    REJECTED (3),
    ;
    // @formatter:on

    private final Integer code;
}
