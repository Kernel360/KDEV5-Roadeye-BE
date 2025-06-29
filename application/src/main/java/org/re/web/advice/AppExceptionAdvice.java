package org.re.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.re.common.api.payload.ErrorResponse;
import org.re.common.exception.AppException;
import org.re.common.exception.CommonAppExceptionCode;
import org.re.common.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class AppExceptionAdvice {
    @ExceptionHandler(AppException.class)
    public Object handleAppException(AppException e) {
        // TODO: 예외 처리 코드 작성
        var code = e.getCode();
        var body = Map.of(
            "error", Map.of(
                "code", code.getCode(),
                "message", code.getMessage()
            )
        );
        return ResponseEntity
            .status(code.getHttpStatus())
            .body(body);
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainException(DomainException e) {
        return new ErrorResponse(ErrorResponse.ErrorData.of(e));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);

        var ae = new AppException(CommonAppExceptionCode.INVALID_HTTP_MESSAGE, e);
        return handleAppException(ae);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException() {
        throw new AppException(CommonAppExceptionCode.ACCESS_DENIED);
    }

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

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);

        var ae = new AppException(CommonAppExceptionCode.INTERNAL_SERVER_ERROR, e);
        return handleAppException(ae);
    }
}
