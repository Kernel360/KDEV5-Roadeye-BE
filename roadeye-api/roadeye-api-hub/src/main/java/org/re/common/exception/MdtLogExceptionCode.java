package org.re.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MdtLogExceptionCode implements AppExceptionCode {
    // @formatter:off
    Success("000", "Success"),
    TUID_ERROR("108", "TUID error")
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
