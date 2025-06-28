package org.re.tenant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.re.test.api.TenantIdTestApi;
import org.re.web.method.support.TenantIdArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    value = {TenantIdTestApi.class},
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class TenantIdTest {
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("TenantId를 정상적으로 받아올 수 있어야 한다")
    public void tenantIdShouldBeResolvedCorrectly() throws Exception {
        var tenantId = new TenantId(123L);

        var req = get("/test/tenant-id")
            .sessionAttr(TenantIdArgumentResolver.TENANT_ID_SESSION_ATTRIBUTE_NAME, tenantId);
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tenantId").value(tenantId.value()));
    }
}
