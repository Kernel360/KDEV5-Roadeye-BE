package org.re.company.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.re.admin.domain.PlatformAdmin;
import org.re.company.CompanyQuoteFixture;
import org.re.company.domain.CompanyQuoteStatus;
import org.re.company.dto.CompanyQuoteRequestCommandFixture;
import org.re.config.QueryDslConfig;
import org.re.employee.service.EmployeeDomainService;
import org.re.test.supports.WithPlatformAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import({ QueryDslConfig.class, CompanyQuoteDomainService.class, CompanyDomainService.class, EmployeeDomainService.class})
@DataJpaTest
@WithPlatformAdmin
class CompanyQuoteDomainServiceTest {

    @Autowired
    CompanyQuoteDomainService companyQuoteDomainService;

    @Nested
    @DisplayName("조회 테스트")
    class SelectTest {
        @Test
        @DisplayName("견적 목록 조회가 가능해야 한다.")
        void findAll_Success() {
            // Given
            var numQuotes = 5;
            for (int i = 0; i < numQuotes; i++) {
                var command = CompanyQuoteRequestCommandFixture.create();
                companyQuoteDomainService.requestNewQuote(command);
            }
            var pageable = PageRequest.of(0, 10);

            // When
            var quotes = companyQuoteDomainService.findAll(pageable);

            // Then
            assertNotNull(quotes);
            assertEquals(numQuotes, quotes.getTotalElements());
        }

        @Test
        @DisplayName("견적 단건 조회가 가능해야 한다.")
        void findById_Success() {
            // Given
            var command = CompanyQuoteRequestCommandFixture.create();
            var quote = companyQuoteDomainService.requestNewQuote(command);

            // When
            var foundQuote = companyQuoteDomainService.findById(quote.getId());

            // Then
            assertNotNull(foundQuote);
            assertEquals(quote.getId(), foundQuote.getId());
        }

        @Test
        @DisplayName("견적 상태별 목록 조회가 가능해야 한다.")
        void findAllByQuoteStatus_Success() {
            // Given
            var numQuotes = 5;
            for (int i = 0; i < numQuotes; i++) {
                var command = CompanyQuoteRequestCommandFixture.create();
                companyQuoteDomainService.requestNewQuote(command);
            }
            var quoteStatus = CompanyQuoteStatus.PENDING;
            var pageable = PageRequest.of(0, 10);

            // When
            var quotes = companyQuoteDomainService.findAllByQuoteStatus(quoteStatus, pageable);

            // Then
            assertNotNull(quotes);
            assertEquals(numQuotes, quotes.getTotalElements());
        }

        @Test
        @DisplayName("견적 상태별 목록 조회 시 다른 상태가 포함되지 않아야 한다.")
        void findAllByQuoteStatus_OnlyIncludesSpecifiedStatus(PlatformAdmin approver) {
            // Given
            var numQuotes = 10;
            var numApproved = 3;
            var numRejected = 2;
            for (int i = 0; i < numQuotes; i++) {
                var businessNumber = "1234567890" + i; // 고유한 사업자 번호 생성
                var command = CompanyQuoteRequestCommandFixture.createWithBusinessNumber(businessNumber);
                var quote = companyQuoteDomainService.requestNewQuote(command);

                // 일부는 승인, 일부는 거절 상태로 설정
                if (i < numApproved) {
                    companyQuoteDomainService.approve(approver, quote);
                } else if (i < numApproved + numRejected) {
                    companyQuoteDomainService.reject(approver, quote);
                }
            }
            var numExpected = numQuotes - numApproved - numRejected;

            var pageable = PageRequest.of(0, 10);

            // When
            var quotes = companyQuoteDomainService.findAllByQuoteStatus(CompanyQuoteStatus.PENDING, pageable);

            // Then
            assertNotNull(quotes);
            assertEquals(numExpected, quotes.getTotalElements());
        }
    }

    @Nested
    @DisplayName("승인 테스트")
    class ApprovalTest {
        @Test
        @DisplayName("견적 요청을 승인할 수 있어야 한다.")
        void approve_Success(PlatformAdmin approver) {
            // Given
            var quote = Mockito.spy(CompanyQuoteFixture.create());

            // When
            var company = companyQuoteDomainService.approve(approver, quote);

            // Then
            assertNotNull(company);
            Mockito.verify(quote).approve(approver);
            assertThat(quote.getApprover()).isEqualTo(approver);
        }

        @Test
        @DisplayName("이미 승인된 견적 요청을 다시 승인할 수 없어야 한다.")
        void approve_AlreadyApproved_ThrowsException(PlatformAdmin approver) {
            // Given
            var command = CompanyQuoteRequestCommandFixture.create();
            var quote = companyQuoteDomainService.requestNewQuote(command);
            quote.approve(approver); // 이미 승인된 상태

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                companyQuoteDomainService.approve(approver, quote);
            });
        }

        @Test
        @DisplayName("이미 거절된 견적 요청을 승인할 수 없어야 한다.")
        void approve_AlreadyRejected_ThrowsException(PlatformAdmin approver) {
            // Given
            var command = CompanyQuoteRequestCommandFixture.create();
            var quote = companyQuoteDomainService.requestNewQuote(command);
            quote.reject(approver); // 이미 거절된 상태

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                companyQuoteDomainService.approve(approver, quote);
            });
        }

        @Test
        @DisplayName("견적 요청이 승인되면 회사가 생성되어야 한다.")
        void approve_CreatesCompany(PlatformAdmin approver) {
            // Given
            var command = CompanyQuoteRequestCommandFixture.create();
            var quote = companyQuoteDomainService.requestNewQuote(command);

            // When
            var company = companyQuoteDomainService.approve(approver, quote);

            // Then
            assertNotNull(company);
            assertNotNull(company.getId());
            assertEquals(command.name(), company.getName());
            assertEquals(command.businessNumber(), company.getBusinessNumber());
            assertEquals(command.email(), company.getEmail());
        }
    }

    @Nested
    @DisplayName("거절 테스트")
    class RejectionTest {
        @Test
        @DisplayName("견적 요청을 거절할 수 있어야 한다.")
        void reject_Success(PlatformAdmin approver) {
            // Given
            var command = CompanyQuoteRequestCommandFixture.create();
            var quote = Mockito.spy(companyQuoteDomainService.requestNewQuote(command));

            // When
            var quotes = companyQuoteDomainService.reject(approver, quote);

            // Then
            assertNotNull(quotes);
            Mockito.verify(quote).reject(approver);
            assertThat(quotes.getApprover()).isEqualTo(approver);
        }

        @Test
        @DisplayName("이미 거절된 견적 요청을 다시 거절할 수 없어야 한다.")
        void reject_AlreadyRejected_ThrowsException(PlatformAdmin approver) {
            // Given
            var command = CompanyQuoteRequestCommandFixture.create();
            var quote = companyQuoteDomainService.requestNewQuote(command);
            quote.reject(approver); // 이미 거절된 상태

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                companyQuoteDomainService.reject(approver, quote);
            });
        }

        @Test
        @DisplayName("이미 승인된 견적 요청을 거절할 수 없어야 한다.")
        void reject_AlreadyApproved_ThrowsException(PlatformAdmin approver) {
            // Given
            var command = CompanyQuoteRequestCommandFixture.create();
            var quote = companyQuoteDomainService.requestNewQuote(command);
            quote.approve(approver); // 이미 승인된 상태

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                companyQuoteDomainService.reject(approver, quote);
            });
        }
    }

    @Test
    @DisplayName("회사 견적 요청 생성에 성공해야 한다.")
    void requestNewQuote_Success() {
        // Given
        var command = CompanyQuoteRequestCommandFixture.create();

        // When
        var quote = companyQuoteDomainService.requestNewQuote(command);

        // Then
        assertNotNull(quote);
        assertEquals(command.name(), quote.getQuoteInfo().getCompanyName());
        assertEquals(command.username(), quote.getQuoteInfo().getRootAccountUsername());
        assertEquals(command.email(), quote.getQuoteInfo().getCompanyEmail());
        assertEquals(command.businessNumber(), quote.getQuoteInfo().getCompanyBusinessNumber());
        assertNotNull(quote.getRequestedAt());
    }
}
