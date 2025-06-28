package org.re.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.company.supports.CompanyIdContextProvider;
import org.re.company.supports.CompanyIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompanyIdConfig {
    @Bean
    public CompanyIdProvider companyIdContextProvider() {
        return new CompanyIdContextProvider();
    }
}
