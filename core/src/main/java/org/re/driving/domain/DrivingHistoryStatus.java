package org.re.driving.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.re.common.domain.PersistenceEnum;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DrivingHistoryStatus implements PersistenceEnum<String> {
    // @formatter:off
    DRIVING ("InProgress"),
    ENDED   ("Ended"),
    ;
    // @formatter:on
    private final String code;

}
