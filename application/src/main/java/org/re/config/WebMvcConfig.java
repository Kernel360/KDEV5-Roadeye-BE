package org.re.config;

import lombok.RequiredArgsConstructor;
import org.re.web.filter.CompanyIdContextFilter;
import org.re.web.method.support.CompanyIdArgumentResolver;
import org.re.web.method.support.CompanyUserDetailsArgumentResolver;
import org.re.web.method.support.PlatformAdminUserDetailsArgumentResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CompanyIdArgumentResolver());
        resolvers.add(new CompanyUserDetailsArgumentResolver());
        resolvers.add(new PlatformAdminUserDetailsArgumentResolver());
    }

    @Bean
    public FilterRegistrationBean<CompanyIdContextFilter> companyIdContextFilterRegistration() {
        var registration = new FilterRegistrationBean<>(new CompanyIdContextFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
