package org.re.car.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.re.common.domain.PersistenceEnum;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CarIgnitionStatus implements PersistenceEnum<Integer> {
    // @formatter:off
    OFF (0),
    ON  (1),
    ;
    // @formatter:on

    private final Integer code;
}
