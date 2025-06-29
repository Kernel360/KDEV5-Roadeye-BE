package org.re.company.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.re.company.CompanyQuoteFixture;
import org.re.company.api.payload.CompanyQuoteCreationRequestFixture;
import org.re.company.service.CompanyQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CompanyQuoteApi.class})
@AutoConfigureMockMvc(addFilters = false)
class CompanyQuoteApiTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CompanyQuoteService quoteService;

    @Test
    @DisplayName("견적 생성 API 테스트")
    void createQuote() throws Exception {
        // Given
        var request = CompanyQuoteCreationRequestFixture.create();
        var quote = Mockito.spy(CompanyQuoteFixture.create());
        Mockito.when(quoteService.createQuote(request)).thenReturn(quote);

        // when
        var body = objectMapper.writeValueAsString(request);
        var req = post("/api/company/quotes")
            .contentType("application/json")
            .content(body)
            .with(csrf());

        // Then
        mvc.perform(req)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.companyName").isString())
            .andExpect(jsonPath("$.data.rootAccountUsername").isString())
            .andExpect(jsonPath("$.data.companyEmail").isString())
            .andExpect(jsonPath("$.data.companyBusinessNumber").isString());
        Mockito.verify(quoteService, Mockito.times(1))
            .createQuote(request);
    }
}
