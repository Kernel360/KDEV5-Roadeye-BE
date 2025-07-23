package org.re.employee.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.re.employee.service.EmployeeService;
import org.re.test.base.BaseWebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeApi.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeApiUnitTest extends BaseWebMvcTest {
    @MockitoBean
    EmployeeService employeeService;

    @Nested
    @DisplayName("GET /api/employees/search")
    class Search {
        @Test
        @DisplayName("검색 조건을 지정하지 않은 경우 400 응답을 반환해야 한다.")
        void shouldReturnEmployeeInfo() throws Exception {
            var req = get("/api/employees/search");

            mvc.perform(req)
                .andExpect(status().isBadRequest());
        }
    }
}