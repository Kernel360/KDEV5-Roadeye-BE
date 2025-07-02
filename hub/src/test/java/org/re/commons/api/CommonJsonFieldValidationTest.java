package org.re.commons.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.re.test.api.BaseWebMvcTest;
import org.re.test.api.TestApi;
import org.re.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TestApi.class})
public class CommonJsonFieldValidationTest extends BaseWebMvcTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    TestService testService;

    @ParameterizedTest
    @DisplayName("패킷 버전 테스트")
    @CsvSource({
        "-11111, 400",
        "-1, 400",
        "65535, 000",
        "65536, 400",
    })
    public void testPacketVersion(Integer pv, String code) throws Exception {
        var req = post("/test/field/packet-version")
            .param("packetVersion", String.valueOf(pv));
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(code));
    }
}
