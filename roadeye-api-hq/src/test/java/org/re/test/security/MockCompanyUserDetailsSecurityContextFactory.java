package org.re.test.security;

import org.re.employee.domain.Employee;
import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.domain.EmployeeMetadata;
import org.re.security.userdetails.CompanyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MockCompanyUserDetailsSecurityContextFactory implements WithSecurityContextFactory<MockCompanyUserDetails> {

    @Override
    public SecurityContext createSecurityContext(MockCompanyUserDetails annotation) {
        var user = createMockUser(annotation);
        var userDetails = CompanyUserDetails.from(user);
        var context = SecurityContextHolder.createEmptyContext();
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }

    private Employee createMockUser(MockCompanyUserDetails annotation) {
        var auth = new EmployeeCredentials(
            annotation.username(),
            annotation.password()
        );
        var meta = EmployeeMetadata.create("name", "pos");
        var tenantId = 111L;
        return switch (annotation.role()) {
            case ROOT -> Employee.createRoot(tenantId, auth, meta);
            case NORMAL -> Employee.createNormal(tenantId, auth, meta);
        };
    }
}
