package org.re.hq.tenant;

@FunctionalInterface
public interface TenantIdProvider {
    Long getCurrentTenantId();
}
