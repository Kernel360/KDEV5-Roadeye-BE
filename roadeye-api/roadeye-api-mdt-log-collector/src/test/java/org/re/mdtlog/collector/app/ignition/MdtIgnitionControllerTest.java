package org.re.mdtlog.collector.app.ignition;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.re.mdtlog.collector.app.common.dto.MdtLogRequestTimeInfo;
import org.re.mdtlog.collector.app.ignition.dto.MdtIgnitionOffRequest;
import org.re.mdtlog.collector.app.ignition.dto.MdtIgnitionOnRequest;
import org.re.mdtlog.collector.exception.MdtLogExceptionCode;
import org.re.mdtlog.domain.MdtTransactionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MdtIgnitionController.class)
@DisplayName("[단위 테스트] MDT 시동 Controller")
class MdtIgnitionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    MdtIgnitionService mdtIgnitionService;

    @Test
    @DisplayName("시동 ON 로그 적재 성공")
    void post_ignitionOn() throws Exception {
        // given
        var params = createIgnitionOnRequest();

        var requestBody = objectMapper.writeValueAsString(params);

        Mockito.doAnswer((invocation) -> null)
            .when(mdtIgnitionService)
            .ignitionOn(
                Mockito.any(MdtTransactionId.class),
                Mockito.any(MdtIgnitionOnRequest.class),
                Mockito.any(MdtLogRequestTimeInfo.class)
            );

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-Timestamp", "2025-05-16 12:00:00.123")
            .header("X-TUID", UUID.randomUUID().toString())
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(MdtLogExceptionCode.Success.getCode()))
            .andExpect(jsonPath("$.rstMsg").value(MdtLogExceptionCode.Success.getMessage()))
            .andExpect(jsonPath("$.mdn").value(params.get("mdn")));

        Mockito.verify(mdtIgnitionService, Mockito.times(1))
            .ignitionOn(
                Mockito.any(MdtTransactionId.class),
                Mockito.any(MdtIgnitionOnRequest.class),
                Mockito.any(MdtLogRequestTimeInfo.class)
            );
    }

    @ParameterizedTest
    @DisplayName("GPS 상태가 올바르지 않은 경우 400 에러가 발생해야 한다.")
    @ValueSource(strings = {"Z", "F", "3", "A1"})
    void post_ignitionOn_invalidGpsCondition(String gpsCondition) throws Exception {
        // given
        var params = createIgnitionOnRequest();
        params.put("gcd", gpsCondition);

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isBadRequest());
    }


    @ParameterizedTest
    @DisplayName("패킷 버전이 올바르지 않은 경우 400 에러가 발생해야 한다.")
    @ValueSource(ints = {-1, 65536})
    void post_ignitionOn_invalidPacketVersion(int packetVersion) throws Exception {
        // given
        var params = createIgnitionOnRequest();
        params.put("pv", packetVersion);

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("위도 값이 올바르지 않은 경우 400 에러가 발생해야 한다.")
    @ValueSource(doubles = {-90.1, 90.1})
    void post_ignitionOn_invalidLatitude(double latitude) throws Exception {
        // given
        var params = createIgnitionOnRequest();
        params.put("lat", latitude);

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("경도 값이 올바르지 않은 경우 400 에러가 발생해야 한다.")
    @ValueSource(doubles = {-180.1, 180.1})
    void post_ignitionOn_invalidLongitude(double longitude) throws Exception {
        // given
        var params = createIgnitionOnRequest();
        params.put("lon", longitude);

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("MDT 각도가 올바르지 않은 경우 400 에러가 발생해야 한다.")
    @ValueSource(ints = {-1, 366})
    void post_ignitionOn_invalidMdtAngle(int mdtAngle) throws Exception {
        // given
        var params = createIgnitionOnRequest();
        params.put("ang", mdtAngle);

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("MDT 속도가 올바르지 않은 경우 400 에러가 발생해야 한다.")
    @ValueSource(ints = {-1, 256})
    void post_ignitionOn_invalidMdtSpeed(int mdtSpeed) throws Exception {
        // given
        var params = createIgnitionOnRequest();
        params.put("spd", mdtSpeed);

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("MDT 주행 거리 합계가 올바르지 않은 경우 400 에러가 발생해야 한다.")
    @ValueSource(ints = {-1, 10000000})
    void post_ignitionOn_invalidMdtMileageSum(int mdtMileageSum) throws Exception {
        // given
        var params = createIgnitionOnRequest();
        params.put("sum", mdtMileageSum);

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/ignition/on")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("시동 Off 로그 적재 성공")
    void post_ignitionOff() throws Exception {
        // given
        var params = createIgnitionOffRequest();

        var requestBody = objectMapper.writeValueAsString(params);

        Mockito.doAnswer((invocation) -> null)
            .when(mdtIgnitionService)
            .ignitionOff(
                Mockito.any(MdtTransactionId.class),
                Mockito.any(MdtIgnitionOffRequest.class),
                Mockito.any(MdtLogRequestTimeInfo.class)
            );

        // when
        var req = post("/api/ignition/off")
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-Timestamp", "2025-05-16 12:00:00.123")
            .header("X-TUID", UUID.randomUUID().toString())
            .content(requestBody);

        // then
        mockMvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(MdtLogExceptionCode.Success.getCode()))
            .andExpect(jsonPath("$.rstMsg").value(MdtLogExceptionCode.Success.getMessage()))
            .andExpect(jsonPath("$.mdn").value(params.get("mdn")));

        Mockito.verify(mdtIgnitionService, Mockito.times(1))
            .ignitionOff(
                Mockito.any(MdtTransactionId.class),
                Mockito.any(MdtIgnitionOffRequest.class),
                Mockito.any(MdtLogRequestTimeInfo.class)
            );
    }

    private Map<String, Object> createIgnitionOnRequest() {
        var params = new HashMap<String, Object>();
        params.put("mdn", "car-001");
        params.put("tid", "term-002");
        params.put("mid", "manu-003");
        params.put("pv", 1);
        params.put("did", "dev-004");
        params.put("onTime", "20250516120000");
        params.put("gcd", "A");
        params.put("lat", 37.123456);
        params.put("lon", 127.123456);
        params.put("ang", 90);
        params.put("spd", 60);
        params.put("sum", 1000);
        return params;
    }

    private Map<String, Object> createIgnitionOffRequest() {
        var params = new HashMap<String, Object>();
        params.put("mdn", "car-001");
        params.put("tid", "term-002");
        params.put("mid", "manu-003");
        params.put("pv", 1);
        params.put("did", "dev-004");
        params.put("onTime", "20250516120000");
        params.put("offTime", "20250516130000");
        params.put("gcd", "A");
        params.put("lat", 37.123456);
        params.put("lon", 127.123456);
        params.put("ang", 90);
        params.put("spd", 60);
        params.put("sum", 1000);
        return params;
    }
}
