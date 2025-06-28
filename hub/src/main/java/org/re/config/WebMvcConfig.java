package org.re.config;

import org.re.web.resolver.MdtLogRequestTimeInfoResolver;
import org.re.web.resolver.TransactionUUIDResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MdtLogRequestTimeInfoResolver());
        resolvers.add(new TransactionUUIDResolver());
    }
}
