package org.re.test.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.re.common.exception.MdtLogExceptionCode;
import org.re.test.service.TestService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestApi.class)
public class ApiResponseTest extends BaseWebMvcTest {
    @MockitoBean
    TestService testService;

    @ParameterizedTest
    @DisplayName("존재하지 않는 경로로 요청시 rstCd==100 을 반환한다.")
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE"})
    public void testInvalidPathRequest(String method) throws Exception {
        var invalidPath = "/invalid/path";
        var expected = MdtLogExceptionCode.INVALID_ACCESS_PATH;

        var httpMethod = HttpMethod.valueOf(method);

        mvc.perform(request(httpMethod, invalidPath))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expected.getCode()));
    }

    @ParameterizedTest
    @DisplayName("존재하는 경로이나 지원하지 않는 메소드로 요청 시 rstCd==101 을 반환한다.")
    @ValueSource(strings = {"POST", "PUT", "DELETE"})
    public void testUnsupportedMethodRequest(String method) throws Exception {
        var validPath = "/method-test/get";
        var expected = MdtLogExceptionCode.WRONG_APPROACH;

        var httpMethod = HttpMethod.valueOf(method);

        mvc.perform(request(httpMethod, validPath))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expected.getCode()));
    }

    @ParameterizedTest
    @DisplayName("지원하지 않는 Content-Type 으로 요청 시 rstCd==102 를 반환한다.")
    @ValueSource(strings = {"application/xml", "text/plain"})
    public void testUnsupportedContentTypeRequest(String contentType) throws Exception {
        var validPath = "/content-type-test/json";
        var expected = MdtLogExceptionCode.CONTENT_TYPE_NOT_SUPPORTED;

        mvc.perform(request(HttpMethod.POST, validPath)
                .contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expected.getCode()));
    }

    @Test
    @DisplayName("타임스탬프 헤더가 누락된 경우 rstCd==107 을 반환한다.")
    public void testMissingTimestampHeader() throws Exception {
        var validPath = "/test/timestamp";
        var expectedCode = 107;

        var req = request(HttpMethod.POST, validPath);
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expectedCode));
    }

    @ParameterizedTest
    @DisplayName("타임스탬프 헤더가 올바르지 않은 경우 rstCd==107 을 반환한다.")
    @ValueSource(strings = {"invalid-timestamp", "1234567890"})
    public void testInvalidTimestampHeader(String invalidTimestamp) throws Exception {
        var validPath = "/test/timestamp";
        var expectedCode = 107;

        mvc.perform(request(HttpMethod.POST, validPath)
                .header("X-Timestamp", invalidTimestamp))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expectedCode));
    }

    @Test
    @DisplayName("트랜잭션 ID 헤더가 누락된 경우 rstCd==108 을 반환한다.")
    public void testMissingTransactionIdHeader() throws Exception {
        var validPath = "/test/tuid";

        mvc.perform(request(HttpMethod.POST, validPath))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(MdtLogExceptionCode.TUID_MISSING.getCode()));
    }

    @Test
    @DisplayName("트랜잭션 ID 헤더가 UUID로 해석 가능한 경우 rstCd==000 을 반환한다.")
    public void testValidTransactionIdHeader() throws Exception {
        var validPath = "/test/tuid";
        var validTuid = UUID.randomUUID().toString();

        mvc.perform(request(HttpMethod.POST, validPath)
                .header("X-TUID", validTuid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(MdtLogExceptionCode.Success.getCode()));
    }

    @Test
    @DisplayName("트랜잭션 ID 헤더가 UUID로 해석할 수 없는 경우 rstCd==108 을 반환한다.")
    public void testInvalidTransactionIdHeader() throws Exception {
        var validPath = "/test/tuid";
        var invalidTuid = "invalid-uuid";

        mvc.perform(request(HttpMethod.POST, validPath)
                .header("X-TUID", invalidTuid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(MdtLogExceptionCode.TUID_ERROR.getCode()));
    }

    @Test
    @DisplayName("올바르지 않은 형태의 JSON 요청인 경우 rstCd==300을 반환한다.")
    public void testInvalidJsonRequest() throws Exception {
        var validPath = "/test/json";
        var invalidJson = "{invalidJson}";
        var expectedCode = 300;

        mvc.perform(request(HttpMethod.POST, validPath)
                .contentType("application/json")
                .content(invalidJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expectedCode));
    }

    @Test
    @DisplayName("필수 값이 누락된 JSON 요청인 경우 rstCd==301을 반환한다.")
    public void testUnmappableJsonRequest() throws Exception {
        var validPath = "/test/json/todo";
        var unmappableJson = objectMapper.writeValueAsString(Map.of(
            "nonExistentField", "value"
        ));
        var expectedCode = 301;

        var req = request(HttpMethod.POST, validPath)
            .contentType("application/json")
            .content(unmappableJson);
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expectedCode));
    }

    @Test
    @DisplayName("Jakarta Validation 예외가 발생한 경우 rstCd==400 을 반환한다.")
    public void testJakartaValidationException() throws Exception {
        var validPath = "/test/validation";
        var invalidValue = 0;
        var expectedCode = 400;

        var req = request(HttpMethod.POST, validPath)
            .param("value", String.valueOf(invalidValue));
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expectedCode));
    }

    @Test
    @DisplayName("알 수 없는 예외가 발생한 경우 rstCd==500 을 반환한다.")
    public void testUnknownException() throws Exception {
        var validPath = "/api/test";
        var expectedCode = 500;
        Mockito.doThrow(new RuntimeException("Unknown error"))
            .when(testService).invoke();

        mvc.perform(request(HttpMethod.GET, validPath))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(expectedCode));
    }
}
