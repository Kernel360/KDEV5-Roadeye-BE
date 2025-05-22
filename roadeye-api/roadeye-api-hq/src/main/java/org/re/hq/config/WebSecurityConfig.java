package org.re.hq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.re.hq.security.userdetails.PlatformAdminUserDetailsService;
import org.re.hq.security.web.authentication.JsonUsernamePasswordAuthenticationFilter;
import org.re.hq.security.web.authentication.RoadeyeAuthenticationFailureHandler;
import org.re.hq.security.web.authentication.RoadeyeAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Configuration
    @RequiredArgsConstructor
    public class AdminSecurityConfig {
        private final PlatformAdminUserDetailsService adminUserDetailsService;
        
        private final ObjectMapper objectMapper;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                .securityMatcher("/api/admin/**")
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/admin/auth/sign-in").permitAll()
                    .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
        }

        @Bean
        public AbstractAuthenticationProcessingFilter jsonUsernamePasswordAuthenticationFilter() {
            var filter = new JsonUsernamePasswordAuthenticationFilter("/api/admin/auth/sign-in", adminAuthenticationManager(), objectMapper);
            filter.setAuthenticationSuccessHandler(new RoadeyeAuthenticationSuccessHandler(objectMapper));
            filter.setAuthenticationFailureHandler(new RoadeyeAuthenticationFailureHandler(objectMapper));
            return filter;
        }

        @Bean
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
}
