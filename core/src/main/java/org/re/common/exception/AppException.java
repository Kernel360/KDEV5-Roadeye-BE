package org.re.common.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final AppExceptionCode code;

    public AppException(AppExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public AppException(AppExceptionCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }
}
