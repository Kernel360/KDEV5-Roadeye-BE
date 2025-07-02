package org.re.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.common.exception.AppException;
import org.re.common.exception.MdtLogExceptionCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MdtLogRequestTimeInfoResolver implements HandlerMethodArgumentResolver {
    private static final String TIMESTAMP_HEADER = "X-Timestamp";
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return MdtLogRequestTimeInfo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public MdtLogRequestTimeInfo resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        var servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        var timestamp = servletRequest.getHeader(TIMESTAMP_HEADER);
        if (timestamp == null) {
            throw new AppException(MdtLogExceptionCode.TIMESTAMP_MISSING);
        }
        try {
            var occurredAt = LocalDateTime.parse(timestamp, formatter);
            var receivedAt = LocalDateTime.now();
            return new MdtLogRequestTimeInfo(occurredAt, receivedAt);
        } catch (Exception e) {
            throw new AppException(MdtLogExceptionCode.TIMESTAMP_INVALID);
        }
    }
}
