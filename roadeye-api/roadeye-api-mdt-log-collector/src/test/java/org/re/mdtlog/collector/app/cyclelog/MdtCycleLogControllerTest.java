package org.re.mdtlog.collector.app.cyclelog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.re.mdtlog.collector.app.common.dto.MdtLogRequestTimeInfo;
import org.re.mdtlog.collector.app.cyclelog.dto.MdtAddCycleLogRequest;
import org.re.mdtlog.domain.MdtTransactionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MdtCycleLogController.class)
@DisplayName("[단위 테스트] MDT 주기 정보 수집 Controller")
class MdtCycleLogControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    MdtCycleLogService cycleLogService;

    @Test
    @DisplayName("주기 정보 적재 성공")
    void post_cycleLog() throws Exception {
        // given
        var params = createCycleLogRequest();

        var requestBody = objectMapper.writeValueAsString(params);

        // when
        var req = post("/api/cycle-log")
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-Timestamp", "2025-05-26 12:00:00.123")
            .header("X-TUID", UUID.randomUUID().toString())
            .content(requestBody);

        // then
        mvc.perform(req)
            .andExpect(status().isOk());

        Mockito.verify(cycleLogService, Mockito.times(1))
            .addCycleLogs(
                Mockito.any(MdtTransactionId.class),
                Mockito.any(MdtAddCycleLogRequest.class),
                Mockito.any(MdtLogRequestTimeInfo.class)
            );
    }

    private Map<String, Object> createCycleLogRequest() {
        var params = new HashMap<String, Object>();
        params.put("mdn", "car-001");
        params.put("tid", "term-002");
        params.put("mid", "manu-003");
        params.put("pv", 1);
        params.put("did", "dev-004");
        params.put("oTime", "202505161200");

        var cList = List.of(
            createCycleLogItem(0, "A", 37.123456, 127.123456),
            createCycleLogItem(1, "V", 37.654321, 127.654321)
        );
        params.put("cCnt", cList.size());
        params.put("cList", cList);
        return params;
    }

    private Map<String, Object> createCycleLogItem(int sec, String gpsCond, double gpsLat, double gpsLon) {
        var item = new HashMap<String, Object>();
        item.put("sec", sec);
        item.put("gcd", gpsCond);
        item.put("lat", gpsLat);
        item.put("lon", gpsLon);
        return item;
    }
}
