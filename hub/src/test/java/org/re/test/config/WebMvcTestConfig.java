package org.re.test.config;

import org.re.config.WebMvcConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({WebMvcConfig.class})
@Configuration
public class WebMvcTestConfig {
}
