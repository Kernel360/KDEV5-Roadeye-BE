package org.re.security;

import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.re.config.QueryDslConfig;
import org.re.employee.domain.EmployeeRole;
import org.re.test.api.HelloApi;
import org.re.test.security.MockCompanyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ QueryDslConfig.class,AopAutoConfiguration.class})
@ComponentScan(
    includeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            classes = {Aspect.class}
        )
    },
    useDefaultFilters = false
)
@WebMvcTest(HelloApi.class)
public class ManagerOnlyTest {
    @Autowired
    WebApplicationContext wac;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .apply(springSecurity())
            .build();
    }

    @Test
    @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
    void testManagerOnly() throws Exception {
        mvc.perform(get("/api/foods/mgr-only"))
            .andExpect(status().isForbidden());
    }
}
