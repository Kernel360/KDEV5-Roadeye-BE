package org.re.hq.web.advice;

import org.re.domain.common.CommonAppExceptionCode;
import org.re.exception.AppException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityAdvice {
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException() {
        throw new AppException(CommonAppExceptionCode.ACCESS_DENIED);
    }
}
