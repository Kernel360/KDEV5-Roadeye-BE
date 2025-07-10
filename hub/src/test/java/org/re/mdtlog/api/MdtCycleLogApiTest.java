package org.re.mdtlog.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.re.car.repository.CarRepository;
import org.re.mdtlog.dto.MdtCycleLogMessageFixture;
import org.re.mdtlog.messaging.MdtLogMessagingService;
import org.re.mdtlog.service.MdtCycleLogService;
import org.re.test.api.BaseWebMvcTest;
import org.re.web.resolver.MdtLogRequestTimeInfoResolver;
import org.re.web.resolver.TransactionUUIDResolver;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
    MdtCycleLogApi.class,
    MdtCycleLogService.class
})
public class MdtCycleLogApiTest extends BaseWebMvcTest {
    @MockitoBean
    MdtLogMessagingService mdtLogMessagingService;

    @MockitoBean
    CarRepository carRepository;

    @Nested
    @DisplayName("POST /api/cycle-log")
    class PostApiCycleLogTest {
        @ParameterizedTest
        @DisplayName("필수 속성이 null일 경우 예외가 발생해야 한다.")
        @ValueSource(strings = {
            "mdn", "tid", "mid", "pv", "did", "oTime", "cCnt", "cList",
            "cList[].sec", "cList[].gcd", "cList[].lat", "cList[].lon", "cList[].ang", "cList[].spd", "cList[].sum", "cList[].bat"
        })
        @SuppressWarnings("unchecked")
        void testPropertyNullCheck(String property) throws Exception {
            var carId = 1L;
            var nLogs = 1;

            var now = LocalDateTime.now();
            var dto = MdtCycleLogMessageFixture.createWithLogItems(carId, nLogs);
            var map = objectMapper.readValue(objectMapper.writeValueAsString(dto), Map.class);
            if (property.contains("[]")) {
                var listProperty = property.substring(0, property.indexOf("[]"));
                var itemProperty = property.substring(property.indexOf("[]") + 3);
                var list = (List<Map<String, Object>>) map.get(listProperty);
                for (var item : list) {
                    item.put(itemProperty, null);
                }
                map.put(listProperty, list);
            }
            else {
                map.put(property, null);
            }
            Mockito.when(carRepository.existsById(carId)).thenReturn(true);

            var body = objectMapper.writeValueAsString(map);
            var req = post("/api/cycle-log")
                .contentType(MediaType.APPLICATION_JSON)
                .header(MdtLogRequestTimeInfoResolver.TIMESTAMP_HEADER_NAME, now.format(DateTimeFormatter.ofPattern(MdtLogRequestTimeInfoResolver.TIMESTAMP_PATTERN)))
                .header(TransactionUUIDResolver.HEADER_NAME, UUID.randomUUID().toString())
                .content(body);

            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rstCd").value("301"));
        }

        @Test
        @DisplayName("존재하지 않는 차량인 경우 rstCd==304를 반환해야 한다.")
        void testNonExistentVehicle() throws Exception {
            var tuid = UUID.randomUUID().toString();
            var now = LocalDateTime.now();
            var carId = -1L;
            var body = objectMapper.writeValueAsString(MdtCycleLogMessageFixture.create(carId));
            var expectedCode = 304;
            Mockito.when(carRepository.existsById(carId)).thenReturn(false);

            var req = post("/api/cycle-log")
                .contentType("application/json")
                .header("X-TUID", tuid)
                .header(MdtLogRequestTimeInfoResolver.TIMESTAMP_HEADER_NAME, now.format(DateTimeFormatter.ofPattern(MdtLogRequestTimeInfoResolver.TIMESTAMP_PATTERN)))
                .content(body);
            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rstCd").value(expectedCode));
        }
    }
}
