package org.re.company.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.re.admin.service.PlatformAdminService;
import org.re.company.api.payload.CompanyQuoteCreationRequestFixture;
import org.re.company.domain.CompanyQuote;
import org.re.company.dto.CompanyQuoteRequestCommand;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class CompanyQuoteServiceTest {
    CompanyQuoteService companyQuoteService;

    @Mock
    CompanyQuoteDomainService companyQuoteDomainService;

    @Mock
    PlatformAdminService platformAdminService;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        companyQuoteService = new CompanyQuoteService(
            companyQuoteDomainService,
            platformAdminService,
            passwordEncoder
        );
    }

    @AfterEach
    void tearDown() {
        companyQuoteService = null;
        companyQuoteDomainService = null;
        platformAdminService = null;
        passwordEncoder = null;
    }

    @Test
    @DisplayName("견적 생성 시 비밀번호는 암호화 해서 저장해야 한다.")
    void createQuote_ShouldEncryptPassword() {
        // Given
        var request = CompanyQuoteCreationRequestFixture.create();
        var rawPassword = request.rootAccountPassword();
        Mockito.when(companyQuoteDomainService.requestNewQuote(Mockito.any()))
            .thenAnswer((invocation) -> {
                var command = invocation.getArgument(0, CompanyQuoteRequestCommand.class);
                var info = command.toQuoteInfo();
                var quote = Mockito.mock(CompanyQuote.class);
                Mockito.when(quote.getQuoteInfo()).thenReturn(info);
                return quote;
            });

        // when
        var quote = companyQuoteService.createQuote(request);
        var savedPassword = quote.getQuoteInfo().getRootAccountPassword();

        // Then
        assertThat(savedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, savedPassword)).isTrue();
    }
}
