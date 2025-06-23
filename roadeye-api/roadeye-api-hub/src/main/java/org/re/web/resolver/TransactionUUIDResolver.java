package org.re.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.re.mdtlog.domain.TransactionUUID;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TransactionUUIDResolver implements HandlerMethodArgumentResolver {
    private static final String HEADER_NAME = "X-TUID";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return TransactionUUID.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public TransactionUUID resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        var servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        var tuid = servletRequest.getHeader(HEADER_NAME);
        if (tuid == null) {
            return null;
        }
        return TransactionUUID.from(tuid);
    }
}
