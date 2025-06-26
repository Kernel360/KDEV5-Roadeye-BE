package org.re.web.advice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.re.common.exception.AppException;
import org.re.common.exception.MdtLogExceptionCode;
import org.re.test.api.TestController;
import org.re.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
public class MdtExceptionAdviceTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    TestService testService;

    @ParameterizedTest
    @DisplayName("요청 실패시 응답 포맷 검증")
    @EnumSource(MdtLogExceptionCode.class)
    void testSuccess(MdtLogExceptionCode code) throws Exception {
        Mockito.doAnswer((__) -> {
            throw new AppException(code);
        }).when(testService).invoke();

        mvc.perform(get("/api/test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(code.getCode()))
            .andExpect(jsonPath("$.rstMsg").isString());
    }

    @Test
    @DisplayName("알 수 없는 예외 발생시 응답 포맷 검증")
    void testUnknownException() throws Exception {
        Mockito.doAnswer((__) -> {
            throw new RuntimeException("Unknown error");
        }).when(testService).invoke();

        mvc.perform(get("/api/test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rstCd").value(MdtLogExceptionCode.INTERNAL_SERVER_ERROR.getCode()))
            .andExpect(jsonPath("$.rstMsg").isString());
    }
}
