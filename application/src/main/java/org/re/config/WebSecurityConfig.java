package org.re.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.re.security.AuthMemberType;
import org.re.security.userdetails.CompanyUserDetailsService;
import org.re.security.userdetails.PlatformAdminUserDetailsService;
import org.re.security.web.authentication.JsonUsernamePasswordAuthenticationFilter;
import org.re.security.web.authentication.RoadeyeAuthenticationFailureHandler;
import org.re.security.web.authentication.RoadeyeAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setMaxAge(120L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Configuration
    @RequiredArgsConstructor
    public class AdminSecurityConfig {
        private final PlatformAdminUserDetailsService adminUserDetailsService;

        private final ObjectMapper objectMapper;

        @Bean
        @Priority(1)
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                .securityMatcher("/api/admin/**")
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .requestMatchers("/api/admin/auth/sign-in").permitAll()
                    .anyRequest().hasAuthority(AuthMemberType.ADMIN.getValue())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors((c) -> c.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
        }

        public AbstractAuthenticationProcessingFilter jsonUsernamePasswordAuthenticationFilter() {
            var filter = new JsonUsernamePasswordAuthenticationFilter("/api/admin/auth/sign-in", adminAuthenticationManager(), objectMapper);
            filter.setAuthenticationSuccessHandler(new RoadeyeAuthenticationSuccessHandler(objectMapper));
            filter.setAuthenticationFailureHandler(new RoadeyeAuthenticationFailureHandler(objectMapper));
            filter.setSecurityContextRepository(securityContextRepository());
            return filter;
        }

        // TODO: global authentication manager로 리팩토링
        public AuthenticationManager adminAuthenticationManager() {
            var provider = adminAuthenticationProvider();
            return new ProviderManager(provider);
        }

        @Bean
        public AuthenticationProvider adminAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(adminUserDetailsService);
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }
    }

    @Configuration
    @RequiredArgsConstructor
    public class CompanySecurityConfig {
        private final CompanyUserDetailsService companyUserDetailsService;

        private final ObjectMapper objectMapper;

        @Bean
        @Priority(2)
        public SecurityFilterChain companyFilterChain(HttpSecurity http) throws Exception {
            return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .requestMatchers(CorsUtils::isCorsRequest).permitAll()
                    .requestMatchers("/api/auth/sign-in", "/api/session").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/company/quotes").permitAll()
                    .anyRequest().hasAuthority(AuthMemberType.USER.getValue())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors((c) -> c.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
        }

        public AbstractAuthenticationProcessingFilter jsonUsernamePasswordAuthenticationFilter() {
            var filter = new JsonUsernamePasswordAuthenticationFilter("/api/auth/sign-in", companyAuthenticationManager(), objectMapper);
            filter.setAuthenticationSuccessHandler(new RoadeyeAuthenticationSuccessHandler(objectMapper));
            filter.setAuthenticationFailureHandler(new RoadeyeAuthenticationFailureHandler(objectMapper));
            filter.setSecurityContextRepository(securityContextRepository());
            return filter;
        }

        // TODO: global authentication manager로 리팩토링
        public AuthenticationManager companyAuthenticationManager() {
            var provider = companyAuthenticationProvider();
            return new ProviderManager(provider);
        }

        @Bean
        public AuthenticationProvider companyAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(companyUserDetailsService);
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }
    }
}
