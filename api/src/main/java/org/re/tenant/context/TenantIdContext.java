package org.re.tenant.context;

import org.re.tenant.TenantId;

public class TenantIdContext {
    private static final ThreadLocal<TenantId> tenantIdHolder = new ThreadLocal<>();

    public static TenantId getTenantId() {
        return tenantIdHolder.get();
    }

    public static void setTenantId(TenantId tenantId) {
        tenantIdHolder.set(tenantId);
    }

    public static void clear() {
        tenantIdHolder.remove();
    }

    private TenantIdContext() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
