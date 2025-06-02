package org.re.hq.test.controller;

import org.jspecify.annotations.Nullable;
import org.re.hq.tenant.TenantId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TenantIdTestController {
    @GetMapping("/test/tenant-id")
    public Object testTenantId(
        @Nullable TenantId tenantId
    ) {
        return Map.of(
            "tenantId", tenantId.value()
        );
    }
}
