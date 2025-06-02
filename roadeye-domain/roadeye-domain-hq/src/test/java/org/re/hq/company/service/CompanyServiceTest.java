package org.re.hq.company.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.re.hq.company.CompanyQuoteFixture;
import org.re.hq.employee.service.EmployeeDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({CompanyService.class, EmployeeDomainService.class})
@DataJpaTest
class CompanyServiceTest {
    @Autowired
    CompanyService companyService;

    @Autowired
    EmployeeDomainService employeeDomainService;

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {
        @Test
        @DisplayName("회사를 생성할 수 있어야 한다.")
        void createCompany_Success() {
            // Given
            var quote = CompanyQuoteFixture.create();

            // When
            var company = companyService.createCompany(quote);

            // Then
            assertNotNull(company);
            assertNotNull(company.getId());
        }

        @Test
        @DisplayName("회사를 생성할 때, 중복된 사업자 번호가 있으면 예외가 발생해야 한다.")
        void createCompany_DuplicateBusinessNumber_Failure() {
            // Given
            var quote = CompanyQuoteFixture.create();
            companyService.createCompany(quote);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                companyService.createCompany(quote);
            });
        }

        @Test
        @DisplayName("회사를 생성할 때, 루트 계정이 생성되어야 한다.")
        void createCompany_RootAccountCreated_Success() {
            // Given
            var quote = CompanyQuoteFixture.create();

            // When
            var company = companyService.createCompany(quote);
            var rootAccount = employeeDomainService.findCompanyRootAccount(company.getId());

            // Then
            assertNotNull(company);
            assertNotNull(company.getId());
            assertNotNull(rootAccount);
            assertNotNull(rootAccount.getId());
        }
    }
}
