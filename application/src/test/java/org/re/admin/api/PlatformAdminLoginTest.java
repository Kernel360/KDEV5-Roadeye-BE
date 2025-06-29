package org.re.admin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.re.admin.service.PlatformAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@DisplayName("[통합 테스트] 플랫폼 관리자 로그인 테스트")
public class PlatformAdminLoginTest {
    static final String VALID_USERNAME = "validUsername";
    static final String VALID_PASSWORD = "validPassword";

    @Autowired
    WebApplicationContext wac;

    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PlatformAdminService platformAdminService;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders
            .webAppContextSetup(this.wac)
            .apply(springSecurity())
            .build();
    }

    @Test
    @DisplayName("플랫폼 관리자로 로그인할 수 있어야 한다.")
    void platformAdminLoginTest() throws Exception {
        // given
        platformAdminService.createAdmin(VALID_USERNAME, VALID_PASSWORD);

        // when
        var body = objectMapper.writeValueAsString(Map.of(
            "username", VALID_USERNAME,
            "password", VALID_PASSWORD
        ));
        var req = post("/api/admin/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body);

        // then
        mvc.perform(req)
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @DisplayName("올바르지 않은 username인 경우 로그인할 수 없다.")
    @ValueSource(strings = {"invalidUsername", ""})
    void invalidUsernameLoginTest(String invalidUsername) throws Exception {
        // given
        platformAdminService.createAdmin(VALID_USERNAME, VALID_PASSWORD);

        // when
        var body = objectMapper.writeValueAsString(Map.of(
            "username", invalidUsername,
            "password", VALID_PASSWORD
        ));
        var req = post("/api/admin/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body);

        // then
        mvc.perform(req)
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @DisplayName("올바르지 않은 password인 경우 로그인할 수 없다.")
    @ValueSource(strings = {"invalidPassword", ""})
    void invalidPasswordLoginTest(String invalidPassword) throws Exception {
        // given
        platformAdminService.createAdmin(VALID_USERNAME, VALID_PASSWORD);

        // when
        var body = objectMapper.writeValueAsString(Map.of(
            "username", VALID_USERNAME,
            "password", invalidPassword
        ));
        var req = post("/api/admin/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body);

        // then
        mvc.perform(req)
            .andExpect(status().isUnauthorized());
    }
}
