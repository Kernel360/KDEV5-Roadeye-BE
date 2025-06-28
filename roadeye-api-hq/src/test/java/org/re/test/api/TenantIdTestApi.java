package org.re.test.api;

import org.jspecify.annotations.Nullable;
import org.re.tenant.TenantId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TenantIdTestApi {
    @GetMapping("/test/tenant-id")
    public Object testTenantId(
        @Nullable TenantId tenantId
    ) {
        return Map.of(
            "tenantId", tenantId.value()
        );
    }
}
