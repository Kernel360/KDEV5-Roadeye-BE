package org.re.hq.test.security;

import org.re.hq.employee.domain.Employee;
import org.re.hq.employee.domain.EmployeeCredentials;
import org.re.hq.employee.domain.EmployeeMetadata;
import org.re.hq.security.userdetails.CompanyUserDetails;
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
        var meta = switch (annotation.role()) {
            case ROOT -> EmployeeMetadata.createRoot("name", "pos");
            case NORMAL -> EmployeeMetadata.createNormal("name", "pos");
        };
        return Employee.of(
            111L,
            auth,
            meta
        );
    }
}
