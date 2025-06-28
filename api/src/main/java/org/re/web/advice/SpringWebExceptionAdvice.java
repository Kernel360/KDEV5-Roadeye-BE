package org.re.web.advice;

import org.re.common.api.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

@RestControllerAdvice
public class SpringWebExceptionAdvice {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var value = Optional.ofNullable(ex.getValue())
            .map(Object::toString)
            .orElse(null);
        var code = "invalid_argument";
        var message = String.format("Invalid argument '%s' with value '%s'", code, value);
        return new ErrorResponse(new ErrorResponse.ErrorData(code, message));
    }
}
