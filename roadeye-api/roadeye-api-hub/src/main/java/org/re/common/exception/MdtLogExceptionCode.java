package org.re.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MdtLogExceptionCode implements AppExceptionCode {
    // @formatter:off
    Success("000", "Success"),
    INVALID_ACCESS_PATH("100", "Invalid access path"),
    TUID_ERROR("108", "TUID error"),
    IGNITION_ALREADY_ON("400", "Ignition is already on"),
    DATA_PROCESSING_ERROR("400", "An error occurred while processing the data"),
    INTERNAL_SERVER_ERROR("500", "Internal server error"),
    ;
    // @formatter:on

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    MdtLogExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }
}
