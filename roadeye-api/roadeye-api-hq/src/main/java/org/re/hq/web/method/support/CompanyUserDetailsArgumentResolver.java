package org.re.hq.web.method.support;

import jakarta.servlet.http.HttpServletRequest;
import org.re.hq.security.userdetails.CompanyUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CompanyUserDetailsArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String COMPANY_USER_DETAILS_SESSION_ATTR_NAME = "userDetails:company";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CompanyUserDetails.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public CompanyUserDetails resolveArgument(
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
        return (CompanyUserDetails) session.getAttribute(COMPANY_USER_DETAILS_SESSION_ATTR_NAME);
    }
}
