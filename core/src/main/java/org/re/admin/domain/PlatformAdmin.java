package org.re.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlatformAdmin extends BaseEntity {
    @Embedded
    private PlatformAdminPrincipal loginInfo;

    @Column(nullable = false)
    private String name;

    PlatformAdmin(PlatformAdminPrincipal loginInfo, String name) {
        this.loginInfo = loginInfo;
        this.name = name;
    }

    public static PlatformAdmin create(String username, String encodedPassword) {
        var loginInfo = new PlatformAdminPrincipal(username, encodedPassword);
        return new PlatformAdmin(loginInfo, "admin");
    }
}
