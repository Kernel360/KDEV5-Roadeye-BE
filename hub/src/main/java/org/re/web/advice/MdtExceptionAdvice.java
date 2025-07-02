package org.re.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.re.common.api.payload.BaseMdtResponse;
import org.re.common.exception.AppException;
import org.re.common.exception.MdtLogExceptionCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Order(100)
@RestControllerAdvice
public class MdtExceptionAdvice {
    @ExceptionHandler(AppException.class)
    public Object handleMdtException(AppException e) {
        var code = e.getCode();
        if (code instanceof MdtLogExceptionCode mdtCode) {
            return ResponseEntity
                .status(code.getHttpStatus())
                .body(new BaseMdtResponse(mdtCode));
        }

        log.error("Unknown exception: {}", e.getMessage(), e);
        return createErrorResponse(MdtLogExceptionCode.DATA_PROCESSING_ERROR);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public Object handleValidationException(HandlerMethodValidationException e) {
        return createErrorResponse(MdtLogExceptionCode.FIELD_VALIDATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error("Unhandled exception: {}", e.getMessage(), e);
        return createErrorResponse(MdtLogExceptionCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException() {
        return createErrorResponse(MdtLogExceptionCode.INVALID_ACCESS_PATH);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleMethodNotAllowedException() {
        return createErrorResponse(MdtLogExceptionCode.WRONG_APPROACH);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object handleMediaTypeNotSupportedException() {
        return createErrorResponse(MdtLogExceptionCode.CONTENT_TYPE_NOT_SUPPORTED);
    }

    private ResponseEntity<?> createErrorResponse(MdtLogExceptionCode code) {
        return ResponseEntity
            .status(code.getHttpStatus())
            .body(new BaseMdtResponse(code));
    }
}
