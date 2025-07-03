package org.re.web.advice;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.re.common.api.payload.BaseMdtResponse;
import org.re.common.exception.AppException;
import org.re.common.exception.MdtLogExceptionCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof JsonParseException) {
            return createErrorResponse(MdtLogExceptionCode.PROTOCOL_FORMAT_ERROR);
        }
        return createErrorResponse(MdtLogExceptionCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var isRequiredFieldMissing = e.getBindingResult().getFieldErrors().stream()
            .anyMatch((fe) -> Objects.isNull(fe.getRejectedValue()));
        if (isRequiredFieldMissing) {
            return createErrorResponse(MdtLogExceptionCode.REQUIRED_FIELD_MISSING);
        }
        return createErrorResponse(MdtLogExceptionCode.FIELD_VALIDATION_ERROR);
    }

    private ResponseEntity<?> createErrorResponse(MdtLogExceptionCode code) {
        return ResponseEntity
            .status(code.getHttpStatus())
            .body(new BaseMdtResponse(code));
    }
}
