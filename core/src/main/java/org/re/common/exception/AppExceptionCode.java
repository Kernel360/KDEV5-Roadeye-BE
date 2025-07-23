package org.re.common.exception;

import org.springframework.http.HttpStatus;

public interface AppExceptionCode {
    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
