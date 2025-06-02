package org.re.hq.web.method.support;

import jakarta.servlet.http.HttpServletRequest;
import org.re.hq.security.userdetails.PlatformAdminUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
        var servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        var session = servletRequest.getSession(false);
        if (session == null) {
            return null;
        }
        return (PlatformAdminUserDetails) session.getAttribute(PLATFORM_ADMIN_USER_DETAILS_SESSION_ATTR_NAME);
    }
}
