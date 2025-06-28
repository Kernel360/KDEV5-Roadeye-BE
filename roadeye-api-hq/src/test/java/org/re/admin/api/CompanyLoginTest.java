package org.re.admin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.re.employee.domain.EmployeeCredentials;
import org.re.employee.service.EmployeeDomainService;
import org.re.web.filter.TenantIdContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("[통합 테스트] 플랫폼 사용자 로그인 테스트")
public class CompanyLoginTest {
    static final String VALID_USERNAME = "validUsername";
    static final String VALID_PASSWORD = "validPassword";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EmployeeDomainService employeeDomainService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("루트 계정 테스트")
    class RootAccountTest {
        @Test
        @DisplayName("루트 계정으로 로그인할 수 있어야 한다.")
        void rootAccountLoginTest() throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createRootAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", VALID_USERNAME,
                "password", VALID_PASSWORD
            ));
            var req = post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .session(new MockHttpSession())
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isOk());
        }

        @ParameterizedTest
        @DisplayName("username이 올바르지 않은 경우 로그인할 수 없다.")
        @ValueSource(strings = {"invalidUsername", " ", ""})
        void invalidUsernameLoginTest(String username) throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createRootAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", username,
                "password", VALID_PASSWORD
            ));
            var req = post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @DisplayName("password가 올바르지 않은 경우 로그인할 수 없다.")
        @ValueSource(strings = {"invalidPassword", " ", ""})
        void invalidPasswordLoginTest(String password) throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createRootAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", VALID_USERNAME,
                "password", password
            ));
            var req = post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("루트 계정으로 가입한 게정이 플랫폼 관리자 계정으로 로그인할 수 있으면 안된다.")
        void rootAccountLoginAsAdminTest() throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createRootAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", VALID_USERNAME,
                "password", VALID_PASSWORD
            ));
            var req = post("/api/admin/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("일반 계정 테스트")
    class NormalAccountTest {
        @Test
        @DisplayName("일반 계정으로 로그인할 수 있어야 한다.")
        void normalAccountLoginTest() throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createNormalAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", VALID_USERNAME,
                "password", VALID_PASSWORD
            ));
            var req = post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isOk());
        }

        @ParameterizedTest
        @DisplayName("username이 올바르지 않은 경우 로그인할 수 없다.")
        @ValueSource(strings = {"invalidUsername", " ", ""})
        void invalidUsernameLoginTest(String username) throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createNormalAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", username,
                "password", VALID_PASSWORD
            ));
            var req = post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @DisplayName("password가 올바르지 않은 경우 로그인할 수 없다.")
        @ValueSource(strings = {"invalidPassword", " ", ""})
        void invalidPasswordLoginTest(String password) throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createNormalAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", VALID_USERNAME,
                "password", password
            ));
            var req = post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("일반 계정으로 가입한 게정이 플랫폼 관리자 계정으로 로그인할 수 있으면 안된다.")
        void normalAccountLoginAsAdminTest() throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createNormalAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", VALID_USERNAME,
                "password", VALID_PASSWORD
            ));
            var req = post("/api/admin/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                .content(body);

            // then
            mvc.perform(req)
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("일반 계정으로 로그인한 후, 세션ID를 사용해 내 정보를 조회할 수 있어야 한다.")
        void normalAccountGetMyInfoTest() throws Exception {
            // given
            var tenantId = 123L;
            var credential = new EmployeeCredentials(VALID_USERNAME, passwordEncoder.encode(VALID_PASSWORD));
            var name = "name";
            var position = "position";

            employeeDomainService.createNormalAccount(tenantId, credential, name, position);

            // when
            var body = objectMapper.writeValueAsString(Map.of(
                "username", VALID_USERNAME,
                "password", VALID_PASSWORD
            ));
            var mockSession = new MockHttpSession();

            // Perform login
            mvc.perform(
                    post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                        .session(mockSession)
                        .content(body)
                )
                .andExpect(status().isOk());

            // then
            mvc.perform(
                    get("/api/employees/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TenantIdContextFilter.TENANT_ID_HEADER_NAME, tenantId)
                        .session(mockSession)
                )
                .andExpect(status().isOk());
        }
    }
}
