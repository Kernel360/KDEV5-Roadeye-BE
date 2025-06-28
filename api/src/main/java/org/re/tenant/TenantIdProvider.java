package org.re.tenant;

@FunctionalInterface
public interface TenantIdProvider {
    Long getCurrentTenantId();
}
