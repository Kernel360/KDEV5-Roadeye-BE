package org.re.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MdtLogExceptionCode implements AppExceptionCode {
    // @formatter:off
    Success("000", "Success"),
    INVALID_ACCESS_PATH("100", "Invalid access path"),
    WRONG_APPROACH("101", "Wrong approach"),
    CONTENT_TYPE_NOT_SUPPORTED("102", "Content type not supported"),
    TIMESTAMP_MISSING("107", "Timestamp is missing"),
    TIMESTAMP_INVALID("107", "Invalid timestamp format"),
    TUID_MISSING("108", "TUID is missing"),
    TUID_ERROR("108", "TUID error"),
    PROTOCOL_FORMAT_ERROR("300", "Protocol format error"),
    MDN_NOT_FOUND("304", "MDN not found"),
    MDN_MISMATCH("304", "Mismatched MDN"),
    IGNITION_ALREADY_ON("400", "Ignition is already on"),
    FIELD_VALIDATION_ERROR("400", "Field validation error"),
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
