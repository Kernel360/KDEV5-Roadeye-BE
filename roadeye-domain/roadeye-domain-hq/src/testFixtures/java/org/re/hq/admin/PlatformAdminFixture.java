package org.re.hq.admin;

import org.re.hq.admin.domain.PlatformAdmin;

public class PlatformAdminFixture {
    public static PlatformAdmin create() {
        var username = "admin";
        var encodedPassword = "{noop}admin123";
        return PlatformAdmin.create(username, encodedPassword);
    }
}
