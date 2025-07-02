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

    @ParameterizedTest
    @DisplayName("위도 테스트")
    @CsvSource({
        "-90.000001, 400",
        "-90.0, 000",
        "0.0, 000",
        "90.0, 000",
        "90.000001, 400",
    })
    public void testLatitude(Double latitude, String code) throws Exception {
        var req = post("/test/field/latitude")
            .param("latitude", String.valueOf(latitude));
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(code));
    }

    @ParameterizedTest
    @DisplayName("경도 테스트")
    @CsvSource({
        "-180.000001, 400",
        "-180.0, 000",
        "0.0, 000",
        "180.0, 000",
        "180.000001, 400",
    })
    public void testLongitude(Double longitude, String code) throws Exception {
        var req = post("/test/field/longitude")
            .param("longitude", String.valueOf(longitude));
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(code));
    }

    @ParameterizedTest
    @DisplayName("Angle 테스트")
    @CsvSource({
        "-1, 400",
        "-0.1, 400",
        "0.0, 000",
        "365, 000",
        "365.0, 000",
        "365.000001, 400",
        "366, 400",
    })
    public void testAngle(Double angle, String code) throws Exception {
        var req = post("/test/field/angle")
            .param("angle", String.valueOf(angle));
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(code));
    }
}
