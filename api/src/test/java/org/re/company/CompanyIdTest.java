package org.re.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.re.company.domain.CompanyId;
import org.re.test.api.CompanyIdTestApi;
import org.re.web.method.support.CompanyIdArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    value = {CompanyIdTestApi.class},
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class CompanyIdTest {
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("CompanyId를 정상적으로 받아올 수 있어야 한다")
    public void companyIdShouldBeResolvedCorrectly() throws Exception {
        var companyId = new CompanyId(123L);

        var req = get("/test/companyId")
            .sessionAttr(CompanyIdArgumentResolver.COMPANY_ID_SESSION_ATTRIBUTE_NAME, companyId);
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.companyId").value(companyId.value()));
    }
}
