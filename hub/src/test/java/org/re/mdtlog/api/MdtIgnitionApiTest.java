package org.re.mdtlog.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.re.mdtlog.dto.MdtIgnitionOffMessageFixture;
import org.re.mdtlog.dto.MdtIgnitionOnMessageFixture;
import org.re.mdtlog.service.MdtIgnitionService;
import org.re.test.api.BaseWebMvcTest;
import org.re.web.resolver.MdtLogRequestTimeInfoResolver;
import org.re.web.resolver.TransactionUUIDResolver;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MdtIgnitionApi.class)
class MdtIgnitionApiTest extends BaseWebMvcTest {
    @MockitoBean
    MdtIgnitionService mdtIgnitionService;

    @Nested
    @DisplayName("POST /api/ignition/on")
    class IgnitionOnTest {
        @Test
        @DisplayName("필수 속성이 모두 존재할 경우 요청에 성공해야 한다.")
        void testIgnitionOnSuccess() throws Exception {
            var now = LocalDateTime.now();
            var dto = MdtIgnitionOnMessageFixture.create();
            var body = objectMapper.writeValueAsString(dto);

            var req = post("/api/ignition/on")
                .contentType(MediaType.APPLICATION_JSON)
                .header(MdtLogRequestTimeInfoResolver.TIMESTAMP_HEADER_NAME, now.format(DateTimeFormatter.ofPattern(MdtLogRequestTimeInfoResolver.TIMESTAMP_PATTERN)))
                .header(TransactionUUIDResolver.HEADER_NAME, UUID.randomUUID().toString())
                .content(body);

            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rstCd").value("000"));
        }

        @ParameterizedTest
        @DisplayName("필수 속성이 null일 경우 예외가 발생해야 한다.")
        @ValueSource(strings = {"mdn", "tid", "mid", "pv", "did", "onTime", "gcd", "lat", "lon", "ang", "spd", "sum"})
        @SuppressWarnings("unchecked")
        void testPropertyNullCheck(String property) throws Exception {
            var now = LocalDateTime.now();
            var dto = MdtIgnitionOnMessageFixture.create();
            var map = objectMapper.readValue(objectMapper.writeValueAsString(dto), Map.class);
            map.put(property, null);

            var body = objectMapper.writeValueAsString(map);
            var req = post("/api/ignition/on")
                .contentType(MediaType.APPLICATION_JSON)
                .header(MdtLogRequestTimeInfoResolver.TIMESTAMP_HEADER_NAME, now.format(DateTimeFormatter.ofPattern(MdtLogRequestTimeInfoResolver.TIMESTAMP_PATTERN)))
                .header(TransactionUUIDResolver.HEADER_NAME, UUID.randomUUID().toString())
                .content(body);

            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rstCd").value("301"));
        }
    }

    @Nested
    @DisplayName("POST /api/ignition/off")
    class IgnitionOffTest {
        @Test
        @DisplayName("필수 속성이 모두 존재할 경우 요청에 성공해야 한다.")
        void testIgnitionOffSuccess() throws Exception {
            var now = LocalDateTime.now();
            var dto = MdtIgnitionOffMessageFixture.create();
            var body = objectMapper.writeValueAsString(dto);

            var req = post("/api/ignition/off")
                .contentType(MediaType.APPLICATION_JSON)
                .header(MdtLogRequestTimeInfoResolver.TIMESTAMP_HEADER_NAME, now.format(DateTimeFormatter.ofPattern(MdtLogRequestTimeInfoResolver.TIMESTAMP_PATTERN)))
                .header(TransactionUUIDResolver.HEADER_NAME, UUID.randomUUID().toString())
                .content(body);

            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rstCd").value("000"));
        }

        @ParameterizedTest
        @DisplayName("필수 속성이 null일 경우 예외가 발생해야 한다.")
        @ValueSource(strings = {"mdn", "tid", "mid", "pv", "did", "onTime", "offTime", "gcd", "lat", "lon", "ang", "spd", "sum"})
        @SuppressWarnings("unchecked")
        void testPropertyNullCheck(String property) throws Exception {
            var now = LocalDateTime.now();
            var dto = MdtIgnitionOffMessageFixture.create();
            var map = objectMapper.readValue(objectMapper.writeValueAsString(dto), Map.class);
            map.put(property, null);

            var body = objectMapper.writeValueAsString(map);
            var req = post("/api/ignition/off")
                .contentType(MediaType.APPLICATION_JSON)
                .header(MdtLogRequestTimeInfoResolver.TIMESTAMP_HEADER_NAME, now.format(DateTimeFormatter.ofPattern(MdtLogRequestTimeInfoResolver.TIMESTAMP_PATTERN)))
                .header(TransactionUUIDResolver.HEADER_NAME, UUID.randomUUID().toString())
                .content(body);

            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rstCd").value("301"));
        }
    }
}
