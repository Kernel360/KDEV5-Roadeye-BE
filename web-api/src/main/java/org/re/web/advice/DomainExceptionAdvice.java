package org.re.web.advice;

import org.re.common.api.payload.ErrorResponse;
import org.re.common.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainExceptionAdvice {
    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainException(DomainException e) {
        return new ErrorResponse(ErrorResponse.ErrorData.of(e));
    }
}
