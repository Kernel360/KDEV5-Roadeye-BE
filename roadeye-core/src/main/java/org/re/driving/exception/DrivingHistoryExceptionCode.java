package org.re.driving.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.re.common.exception.DomainExceptionCode;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DrivingHistoryExceptionCode implements DomainExceptionCode {
    ALREADY_ENDED("001", "Driving history has already ended."),
    ;

    private static final String PREFIX = "DHS_";

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return PREFIX + code;
    }
}
