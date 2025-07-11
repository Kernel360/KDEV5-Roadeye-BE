package org.re.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record PlatformAdminPrincipal(
    @Column(nullable = false, unique = true, length = 30)
    String username,

    @Column(nullable = false)
    String password
) {
}
