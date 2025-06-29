package org.re.test.security;

import org.mockito.Mockito;
import org.re.admin.PlatformAdminFixture;
import org.re.admin.domain.PlatformAdmin;
import org.re.security.userdetails.PlatformAdminUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MockPlatformAdminUserDetailsSecurityContextFactory implements WithSecurityContextFactory<MockPlatformAdminUserDetails> {

    @Override
    public SecurityContext createSecurityContext(MockPlatformAdminUserDetails annotation) {
        var user = createMockUser(annotation);
        var userDetails = PlatformAdminUserDetails.from(user);
        var context = SecurityContextHolder.createEmptyContext();
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }

    private PlatformAdmin createMockUser(MockPlatformAdminUserDetails annotation) {
        var user = PlatformAdminFixture.create(annotation.username(), annotation.password());
        var spy = Mockito.spy(user);
        Mockito.when(spy.getId()).thenReturn(annotation.id());
        return spy;
    }
}
