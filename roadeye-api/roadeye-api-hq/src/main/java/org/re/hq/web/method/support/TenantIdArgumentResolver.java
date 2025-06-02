package org.re.hq.web.method.support;

import jakarta.servlet.http.HttpServletRequest;
import org.re.hq.tenant.TenantId;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TenantIdArgumentResolver implements HandlerMethodArgumentResolver {
    public final static String TENANT_ID_SESSION_ATTRIBUTE_NAME = "tenantId";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(TenantId.class);
    }

    @Override
    public TenantId resolveArgument(
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
        var tenantId = (Long) session.getAttribute(TENANT_ID_SESSION_ATTRIBUTE_NAME);
        return new TenantId(tenantId);
    }
}
