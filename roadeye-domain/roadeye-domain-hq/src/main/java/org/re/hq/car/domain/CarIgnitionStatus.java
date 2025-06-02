package org.re.hq.car.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CarIgnitionStatus {
    // @formatter:off
    OFF (0),
    ON  (1),
    ;
    // @formatter:on

    private final int value;

    public static CarIgnitionStatus of(int value) {
        for (CarIgnitionStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ignition status value: " + value);
    }
}
