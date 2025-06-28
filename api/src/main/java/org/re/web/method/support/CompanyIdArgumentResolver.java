package org.re.web.method.support;

import jakarta.servlet.http.HttpServletRequest;
import org.re.company.domain.CompanyId;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CompanyIdArgumentResolver implements HandlerMethodArgumentResolver {
    public final static String COMPANY_ID_SESSION_ATTRIBUTE_NAME = "companyId";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CompanyId.class);
    }

    @Override
    public CompanyId resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        var servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        var session = servletRequest.getSession(false);
        if (session == null) {
            return null;
        }
        return (CompanyId) session.getAttribute(COMPANY_ID_SESSION_ATTRIBUTE_NAME);
    }
}
