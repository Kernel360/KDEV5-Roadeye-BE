package org.re.test.api;

import org.re.config.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import({
    WebMvcConfig.class
})
public abstract class BaseWebMvcTest {
    @Autowired
    protected MockMvc mvc;
}
