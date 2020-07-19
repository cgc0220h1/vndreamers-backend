package com.codegym.vndreamers.security.jwt.issuer;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.AuthService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class JWTIssuerUnitTest {
    private static final String API_AUTH_LOGIN = "/auth/login";
    private static final String VALID_USERNAME = "dummy_username";
    private static final String VALID_PASSWORD = "dummy_password";
    private static final String VALID_TOKEN = "dummy_token";
    public static final String FAIL_PASSWORD = "some_fail_password";
    public static final String FAIL_USERNAME = "some_fail_username";
    private static final String VALID_EMAIL = "dummy@example.com";
    private static JSONObject payload;
    private static JWTResponse jwtResponse;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @BeforeAll
    static void mockData() {
        payload = new JSONObject();
        jwtResponse = new JWTResponse();
        User user = new User();
        user.setUsername(VALID_USERNAME);
        user.setPassword(VALID_PASSWORD);
        jwtResponse.setUser(user);
        jwtResponse.setAccess_token(VALID_TOKEN);
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp valid credential")
    void givenValidCredential_whenLoginPostRequest_thenOkAndReturnJWTResponse() throws Exception {
        when(authService.authenticate(any())).thenReturn(jwtResponse);

        payload.put("username", VALID_USERNAME);
        payload.put("password", VALID_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.access_token", is(VALID_TOKEN)))
                .andExpect(jsonPath("$.user.username", is(VALID_USERNAME)))
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andExpect(jsonPath("$.user.email", is(VALID_EMAIL)))
                .andExpect(jsonPath("$.user.birthDay").exists());
    }

    @Test
    @DisplayName("Đăng nhập với body trống")
    void givenEmptyBody_whenLoginPostRequest_thenBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp để trống credential")
    void givenEmptyCredential_whenLoginPostRequest_thenBadRequest() throws Exception {
        payload.put("username", "");
        payload.put("password", "");
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp để trống username")
    void givenEmptyUsername_whenLoginPostRequest_thenBadRequest() throws Exception {
        payload.put("username", "");
        payload.put("password", VALID_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp để trống password")
    void givenEmptyPassword_whenLoginPostRequest_thenBadRequest() throws Exception {
        payload.put("username", VALID_USERNAME);
        payload.put("password", "");
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp sai password")
    void givenWrongPassword_whenLoginPostRequest_thenUnauthorized() throws Exception {
        payload.put("username", VALID_USERNAME);
        payload.put("password", FAIL_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp sai username")
    void givenWrongUsername_whenLoginPostRequest_thenNotFound() throws Exception {
        payload.put("username", FAIL_USERNAME);
        payload.put("password", FAIL_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}
