package org.re.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.re.test.config.WebMvcTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import({
    WebMvcTestConfig.class,
    ValidationAutoConfiguration.class
})
public abstract class BaseWebMvcTest {
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
