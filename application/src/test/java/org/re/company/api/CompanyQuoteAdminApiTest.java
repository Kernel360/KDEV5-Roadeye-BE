package org.re.company.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.re.company.CompanyQuoteFixture;
import org.re.company.domain.CompanyQuote;
import org.re.company.service.CompanyQuoteService;
import org.re.employee.domain.EmployeeRole;
import org.re.security.userdetails.PlatformAdminUserDetails;
import org.re.test.base.BaseWebMvcTest;
import org.re.test.security.MockCompanyUserDetails;
import org.re.test.security.MockPlatformAdminUserDetails;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyQuoteAdminApi.class)
class CompanyQuoteAdminApiTest extends BaseWebMvcTest {
    @MockitoBean
    CompanyQuoteService companyQuoteService;

    List<CompanyQuote> quotes;

    @BeforeEach
    void setUp() {
        var id = new long[]{1,};
        var nQuotes = 10;
        quotes = CompanyQuoteFixture.createList(nQuotes)
            .stream()
            .map(Mockito::spy)
            .peek(quote -> Mockito.lenient().when(quote.getId()).thenReturn(id[0]++))
            .toList();
        Mockito.lenient().when(companyQuoteService.findAll(Mockito.any()))
            .thenReturn(new PageImpl<>(quotes));
        Mockito.lenient().when(companyQuoteService.findById(Mockito.anyLong()))
            .thenAnswer(invocation -> {
                var quoteId = invocation.getArgument(0, Long.class);
                return quotes.stream()
                    .filter(quote -> Objects.equals(quote.getId(), quoteId))
                    .findFirst()
                    .orElseThrow();
            });
    }

    @Nested
    @DisplayName("조회 테스트")
    class FindTest {
        @Test
        @DisplayName("업체의 일반 계정은 견적 목록 조회 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
        void service_normal_member_cannot_access_quote_list() throws Exception {
            // when
            var request = get("/api/admin/company/quotes");

            // then
            mvc.perform(request)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("업체의 루트 계정은 견적 목록 조회 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.ROOT)
        void service_root_member_cannot_access_quote_list() throws Exception {
            // when
            var request = get("/api/admin/company/quotes");

            // then
            mvc.perform(request)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("관리자는 견적 목록 조회 API를 호출할 수 있다.")
        @MockPlatformAdminUserDetails
        void service_admin_member_can_access_quote_list() throws Exception {
            // when
            var request = get("/api/admin/company/quotes");

            // then
            mvc.perform(request)
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("업체의 일반 계정은 견적 상세 조회 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
        void service_normal_member_cannot_access_quote_detail() throws Exception {
            // given
            var quoteId = 1L;
            var request = get("/api/admin/company/quotes/{quoteId}", quoteId);

            // when
            mvc.perform(request)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("업체의 루트 계정은 견적 상세 조회 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.ROOT)
        void service_root_member_cannot_access_quote_detail() throws Exception {
            // given
            var quoteId = 1L;
            var request = get("/api/admin/company/quotes/{quoteId}", quoteId);

            // when
            mvc.perform(request)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("관리자는 견적 상세 조회 API를 호출할 수 있다.")
        @MockPlatformAdminUserDetails
        void service_admin_member_can_access_quote_detail() throws Exception {
            // given
            var quoteId = 1L;
            var request = get("/api/admin/company/quotes/{quoteId}", quoteId);

            // when
            mvc.perform(request)
                .andExpect(status().isOk());

            // then
            Mockito.verify(companyQuoteService, Mockito.times(1))
                .findById(quoteId);
        }
    }

    @Nested
    @DisplayName("승인 테스트")
    class ApprovalTest {
        @Test
        @DisplayName("업체의 일반 계정은 견적 승인 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
        void service_normal_member_cannot_approve_quote() throws Exception {
            // given
            var quoteId = 1L;

            // when
            var req = post("/api/admin/company/quotes/{quoteId}/approve", quoteId);

            // then
            mvc.perform(req)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("업체의 루트 계정은 견적 승인 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.ROOT)
        void service_root_member_cannot_approve_quote() throws Exception {
            // given
            var quoteId = 1L;

            // when
            var req = post("/api/admin/company/quotes/{quoteId}/approve", quoteId);

            // then
            mvc.perform(req)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("관리자는 견적 승인 API를 호출할 수 있다.")
        @MockPlatformAdminUserDetails
        void service_admin_member_can_approve_quote() throws Exception {
            // given
            var quoteId = 1L;
            Mockito.lenient().when(companyQuoteService.approveQuote(Mockito.any(PlatformAdminUserDetails.class), Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    var id = invocation.getArgument(1, Long.class);
                    return quotes.stream()
                        .filter(quote -> Objects.equals(quote.getId(), id))
                        .findFirst()
                        .orElseThrow();
                });

            // when
            var req = post("/api/admin/company/quotes/{quoteId}/approve", quoteId);
            mvc.perform(req)
                .andExpect(status().isOk());

            // then
            Mockito.verify(companyQuoteService, Mockito.times(1))
                .approveQuote(Mockito.any(), Mockito.eq(quoteId));
        }
    }

    @Nested
    @DisplayName("거절 테스트")
    class RejectionTest {
        @Test
        @DisplayName("업체의 일반 계정은 견적 거절 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.NORMAL)
        void service_normal_member_cannot_reject_quote() throws Exception {
            // given
            var quoteId = 1L;

            // when
            var req = post("/api/admin/company/quotes/{quoteId}/reject", quoteId);

            // then
            mvc.perform(req)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("업체의 루트 계정은 견적 거절 API를 호출할 수 없다.")
        @MockCompanyUserDetails(role = EmployeeRole.ROOT)
        void service_root_member_cannot_reject_quote() throws Exception {
            // given
            var quoteId = 1L;

            // when
            var req = post("/api/admin/company/quotes/{quoteId}/reject", quoteId);

            // then
            mvc.perform(req)
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("관리자는 견적 거절 API를 호출할 수 있다.")
        @MockPlatformAdminUserDetails
        void service_admin_member_can_reject_quote() throws Exception {
            // given
            var quoteId = 1L;
            Mockito.lenient().when(companyQuoteService.rejectQuote(Mockito.any(PlatformAdminUserDetails.class), Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    var id = invocation.getArgument(1, Long.class);
                    return quotes.stream()
                        .filter(quote -> Objects.equals(quote.getId(), id))
                        .findFirst()
                        .orElseThrow();
                });

            // when
            var req = post("/api/admin/company/quotes/{quoteId}/reject", quoteId);
            mvc.perform(req)
                .andExpect(status().isOk());

            // then
            Mockito.verify(companyQuoteService, Mockito.times(1))
                .rejectQuote(Mockito.any(), Mockito.eq(quoteId));
        }
    }
}
