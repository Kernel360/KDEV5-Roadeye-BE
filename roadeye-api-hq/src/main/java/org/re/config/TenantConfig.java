package org.re.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.tenant.TenantIdContextProvider;
import org.re.tenant.TenantIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TenantConfig {
    @Bean
    public TenantIdProvider tenantIdContextProvider() {
        return new TenantIdContextProvider();
    }
}
