package org.re.admin;

import org.re.admin.domain.PlatformAdmin;

public class PlatformAdminFixture {
    public static PlatformAdmin create() {
        var username = "admin";
        var encodedPassword = "{noop}admin123";
        return create(username, encodedPassword);
    }

    public static PlatformAdmin create(String username, String password) {
        return PlatformAdmin.create(username, "{noop}" + password);
    }
}
