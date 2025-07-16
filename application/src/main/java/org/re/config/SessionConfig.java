package org.re.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@Slf4j
@Configuration
@EnableJdbcHttpSession(
    tableName = "_SPRING_SESSION"
)
@RequiredArgsConstructor
public class SessionConfig {
}
