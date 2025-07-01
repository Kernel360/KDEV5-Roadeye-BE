package org.re.security.api.payload;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record AuthenticationSuccessResponse(
    String message,
    Collection<? extends GrantedAuthority> authorities
) {

    public static AuthenticationSuccessResponse from(Authentication auth) {
        return new AuthenticationSuccessResponse("Login successful", auth.getAuthorities());
    }
}
