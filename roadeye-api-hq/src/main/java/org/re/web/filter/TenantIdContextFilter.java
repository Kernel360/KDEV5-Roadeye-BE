package org.re.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.re.tenant.TenantId;
import org.re.tenant.context.TenantIdContext;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantIdContextFilter extends OncePerRequestFilter {
    public final static String TENANT_ID_HEADER_NAME = "X-Company-Id";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String tenantIdString = request.getHeader(TENANT_ID_HEADER_NAME);
        if (Strings.isEmpty(tenantIdString)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            long id = Long.parseLong(tenantIdString);
            TenantIdContext.setTenantId(new TenantId(id));
            filterChain.doFilter(request, response);
        } finally {
            TenantIdContext.clear();
        }
    }
}
