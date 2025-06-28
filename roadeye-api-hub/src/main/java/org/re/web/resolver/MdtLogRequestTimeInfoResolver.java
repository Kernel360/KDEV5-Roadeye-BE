package org.re.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MdtLogRequestTimeInfoResolver implements HandlerMethodArgumentResolver {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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
        var timestamp = servletRequest.getHeader("X-Timestamp");
        var receivedAt = LocalDateTime.now();
        if (timestamp == null) {
            return new MdtLogRequestTimeInfo(receivedAt, receivedAt);
        }
        try {
            var occurredAt = LocalDateTime.parse(timestamp, formatter);
            return new MdtLogRequestTimeInfo(occurredAt, receivedAt);
        } catch (Exception e) {
            // TODO: 무슨 에러를 던져줘야 하지?
            throw new IllegalArgumentException("Invalid timestamp format", e);
        }
    }
}
