package org.re.web.advice;

import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.re.common.api.payload.BaseMdtResponse;
import org.re.common.exception.AppException;
import org.re.common.exception.MdtLogExceptionCode;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class MdtExceptionAdvice {
    @Priority(Ordered.HIGHEST_PRECEDENCE + 100)
    @ExceptionHandler(AppException.class)
    public Object handleMdtException(AppException e) {
        var code = e.getCode();
        if (code instanceof MdtLogExceptionCode mdtCode) {
            return ResponseEntity
                .status(code.getHttpStatus())
                .body(new BaseMdtResponse(mdtCode));
        }

        log.error("Unknown exception: {}", e.getMessage(), e);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new BaseMdtResponse(MdtLogExceptionCode.DATA_PROCESSING_ERROR));
    }

    @Priority(Ordered.HIGHEST_PRECEDENCE + 100)
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error("Unhandled exception: {}", e.getMessage(), e);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new BaseMdtResponse(MdtLogExceptionCode.INTERNAL_SERVER_ERROR));
    }

    @Priority(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new BaseMdtResponse(MdtLogExceptionCode.INVALID_ACCESS_PATH));
    }
}
