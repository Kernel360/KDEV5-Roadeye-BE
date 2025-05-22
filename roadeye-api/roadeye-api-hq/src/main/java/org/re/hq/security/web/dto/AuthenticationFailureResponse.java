package org.re.hq.security.web.dto;

import org.springframework.security.core.AuthenticationException;

public record AuthenticationFailureResponse(
    String message,
    String error
) {

    public static AuthenticationFailureResponse from(AuthenticationException ex) {
        return new AuthenticationFailureResponse("Login Failed", ex.getMessage());
    }
}
