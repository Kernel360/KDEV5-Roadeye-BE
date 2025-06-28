package org.re.tenant;

import org.jspecify.annotations.Nullable;
import org.re.tenant.context.TenantIdContext;

public class TenantIdContextProvider implements TenantIdProvider {
    @Override
    @Nullable
    public Long getCurrentTenantId() {
        var t = TenantIdContext.getTenantId();
        if (t == null) {
            return null;
        }
        return t.value();
    }
}
