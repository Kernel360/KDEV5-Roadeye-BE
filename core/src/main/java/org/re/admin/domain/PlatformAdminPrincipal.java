package org.re.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record PlatformAdminPrincipal(
    @Column(name = "login_username", nullable = false, unique = true, length = 30)
    String username,

    @Column(name = "login_password", nullable = false)
    String password
) {
}
