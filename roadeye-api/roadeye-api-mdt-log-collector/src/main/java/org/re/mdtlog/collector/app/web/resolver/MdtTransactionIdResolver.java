package org.re.mdtlog.collector.app.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.re.mdtlog.domain.MdtTransactionId;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MdtTransactionIdResolver implements HandlerMethodArgumentResolver {
    private static final String HEADER_NAME = "X-TUID";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return MdtTransactionId.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public MdtTransactionId resolveArgument(
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
        return new MdtTransactionId(tuid);
    }
}
