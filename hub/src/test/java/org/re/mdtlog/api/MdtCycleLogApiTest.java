package org.re.mdtlog.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.re.car.repository.CarRepository;
import org.re.mdtlog.dto.MdtCycleLogMessageFixture;
import org.re.mdtlog.messaging.MdtLogMessagingService;
import org.re.mdtlog.service.MdtCycleLogService;
import org.re.test.api.BaseWebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
    @DisplayName("주기 정보 전송 API 테스트")
    class CycleLogApiTest {
        @Test
        @DisplayName("존재하지 않는 차량인 경우 rstCd==304를 반환해야 한다.")
        void testNonExistentVehicle() throws Exception {
            var tuid = UUID.randomUUID().toString();
            var carId = -1L;
            var body = objectMapper.writeValueAsString(MdtCycleLogMessageFixture.create(carId));
            var expectedCode = 304;
            Mockito.when(carRepository.existsById(carId)).thenReturn(false);

            var req = post("/api/cycle-log")
                .contentType("application/json")
                .header("X-TUID", tuid)
                .content(body);
            mvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rstCd").value(expectedCode));
        }
    }
}
