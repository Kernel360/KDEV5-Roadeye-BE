package org.re.car.exception;

import lombok.Getter;
import org.re.common.exception.DomainExceptionCode;

@Getter
public enum CarDomainException implements DomainExceptionCode {
    // @formatter:off
    CAR_NOT_FOUND       ("100", "Car not found"),

    IGNITION_IS_NOT_ON   ("202", "Ignition is not ON"),
    IGNITION_IS_NOT_OFF  ("203", "Ignition is not OFF"),

    TRANSACTION_ID_MISMATCH("300", "Transaction ID does not match the active transaction"),
    ;
    // @formatter:on

    private static final String PREFIX = "CAR_";

    private final String code;
    private final String message;

    CarDomainException(String code, String message) {
        this.code = PREFIX + code;
        this.message = message;
    }
}
