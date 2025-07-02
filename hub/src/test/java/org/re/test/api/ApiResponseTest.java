package org.re.test.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.re.common.exception.MdtLogExceptionCode;
import org.re.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestApi.class)
public class ApiResponseTest {
    @MockitoBean
    TestService testService;

    @Autowired
    MockMvc mvc;

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
}
