package org.re.web.method.support;

import jakarta.servlet.http.HttpServletRequest;
import org.re.security.userdetails.CompanyUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

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
        var request = (HttpServletRequest) webRequest.getNativeRequest();
        return Optional.ofNullable(getFromSecurityContext())
            .orElseGet(() -> getFromSession(request));
    }

    private CompanyUserDetails getFromSecurityContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(CompanyUserDetails.class::cast)
            .orElse(null);
    }

    private CompanyUserDetails getFromSession(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false))
            .map(s -> s.getAttribute(COMPANY_USER_DETAILS_SESSION_ATTR_NAME))
            .map(CompanyUserDetails.class::cast)
            .orElse(null);
    }
}
