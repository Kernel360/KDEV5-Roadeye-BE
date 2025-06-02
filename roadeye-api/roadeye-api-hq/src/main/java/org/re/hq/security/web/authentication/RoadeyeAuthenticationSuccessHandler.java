package org.re.hq.security.web.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.re.hq.security.userdetails.CompanyUserDetails;
import org.re.hq.security.userdetails.PlatformAdminUserDetails;
import org.re.hq.security.web.dto.AuthenticationSuccessResponse;
import org.re.hq.web.method.support.CompanyUserDetailsArgumentResolver;
import org.re.hq.web.method.support.PlatformAdminUserDetailsArgumentResolver;
import org.re.hq.web.method.support.TenantIdArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class RoadeyeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        onAuthenticationSuccess(request, response, authentication);
        chain.doFilter(request, response);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try (var writer = response.getWriter()) {
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                var session = request.getSession(true);

                if (userDetails instanceof CompanyUserDetails companyUserDetails) {
                    session.setAttribute(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, companyUserDetails.getTenantId());
                    session.setAttribute(CompanyUserDetailsArgumentResolver.COMPANY_USER_DETAILS_SESSION_ATTR_NAME, companyUserDetails);
                } else if (userDetails instanceof PlatformAdminUserDetails) {
                    session.setAttribute(PlatformAdminUserDetailsArgumentResolver.PLATFORM_ADMIN_USER_DETAILS_SESSION_ATTR_NAME, userDetails);
                } else {
                    throw new IllegalStateException("Unsupported user details type: " + userDetails.getClass().getName());
                }
            } else {
                throw new IllegalStateException("Authentication details is not an instance of UserDetails");
            }

            var jsonResponse = AuthenticationSuccessResponse.from(authentication);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            objectMapper.writeValue(writer, jsonResponse);
        }
    }
}
