package org.re.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.re.company.domain.CompanyId;
import org.re.company.supports.CompanyIdContext;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CompanyIdContextFilter extends OncePerRequestFilter {
    public final static String COMPANY_ID_HEADER_NAME = "X-Company-Id";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String companyIdString = request.getHeader(COMPANY_ID_HEADER_NAME);
        if (Strings.isEmpty(companyIdString)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            long id = Long.parseLong(companyIdString);
            CompanyIdContext.setCompanyId(new CompanyId(id));
            filterChain.doFilter(request, response);
        } finally {
            CompanyIdContext.clear();
        }
    }
}
