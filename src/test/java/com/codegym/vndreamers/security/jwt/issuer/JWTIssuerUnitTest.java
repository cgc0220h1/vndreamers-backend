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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private static final String DUMMY_USERNAME = "dummy_username";
    private static final String DUMMY_PASSWORD = "dummy_password";
    private static final String DUMMY_TOKEN = "dummy_token";
    private static JSONObject payload;
    private static JWTResponse jwtLoginResponse;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @BeforeAll
    static void mockData() {
        payload = new JSONObject();
        jwtLoginResponse = new JWTResponse();
        User user = new User();
        user.setUsername(DUMMY_USERNAME);
        user.setPassword(DUMMY_PASSWORD);
        jwtLoginResponse.setUser(user);
        jwtLoginResponse.setAccess_token(DUMMY_TOKEN);
    }

    @Test
    @DisplayName("Đăng nhập với body trống")
    void giveEmptyBody_whenLoginPostRequest_thenBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp valid credential")
    void givenNoTokenAndValidCredential_whenLoginPostRequest_thenOkAndReturnUserWithToken() throws Exception {
        when(authService.authenticate(any())).thenReturn(jwtLoginResponse);

        payload.put("username", DUMMY_USERNAME);
        payload.put("password", DUMMY_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.user.username", is(DUMMY_USERNAME)))
                .andExpect(jsonPath("$.user.password", is(null)));
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp invalid credential")
    void givenNoTokenAndInvalidCredential_whenLoginPostRequest_thenUnauthorized() throws Exception {
        when(authService.authenticate(any())).thenThrow(UsernameNotFoundException.class);

        payload.put("username", DUMMY_USERNAME);
        payload.put("password", DUMMY_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }
}
