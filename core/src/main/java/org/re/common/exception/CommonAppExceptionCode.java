package org.re.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonAppExceptionCode implements AppExceptionCode {
    // @formatter:off
    BAD_REQUEST("400", "Bad Request", HttpStatus.BAD_REQUEST),
    INVALID_HTTP_MESSAGE ("400", "Invalid HTTP message",  HttpStatus.BAD_REQUEST),
    ACCESS_DENIED("403", "Access Denied", HttpStatus.FORBIDDEN),
    NOT_FOUND("404", "Not Found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
    ;
    // @formatter:on

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
