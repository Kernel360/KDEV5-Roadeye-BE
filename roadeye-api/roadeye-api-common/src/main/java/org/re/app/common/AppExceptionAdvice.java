package org.re.app.common;

import lombok.extern.slf4j.Slf4j;
import org.re.domain.common.CommonAppExceptionCode;
import org.re.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);

        var ae = new AppException(CommonAppExceptionCode.INVALID_HTTP_MESSAGE, e);
        return handleAppException(ae);
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);

        var ae = new AppException(CommonAppExceptionCode.INTERNAL_SERVER_ERROR, e);
        return handleAppException(ae);
    }
}
