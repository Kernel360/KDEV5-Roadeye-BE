package org.re.web.method.support;

import jakarta.servlet.http.HttpServletRequest;
import org.re.security.userdetails.PlatformAdminUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class PlatformAdminUserDetailsArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String PLATFORM_ADMIN_USER_DETAILS_SESSION_ATTR_NAME = "userDetails:platformAdmin";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PlatformAdminUserDetails.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public PlatformAdminUserDetails resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        var request = (HttpServletRequest) webRequest.getNativeRequest();
        return Optional.ofNullable(getFromSecurityContext())
            .orElseGet(() -> getFromSession(request));
    }

    private PlatformAdminUserDetails getFromSecurityContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(PlatformAdminUserDetails.class::cast)
            .orElse(null);
    }

    private PlatformAdminUserDetails getFromSession(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false))
            .map(s -> s.getAttribute(PLATFORM_ADMIN_USER_DETAILS_SESSION_ATTR_NAME))
            .map(PlatformAdminUserDetails.class::cast)
            .orElse(null);
    }
}
