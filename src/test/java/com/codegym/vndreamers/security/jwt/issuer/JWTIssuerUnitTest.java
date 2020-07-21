package com.codegym.vndreamers.security.jwt.issuer;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.AuthService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.ValidationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private static final String API_AUTH_REGISTER = "/auth/register";
    private static final String VALID_USERNAME = "some_valid_username";
    private static final String VALID_PASSWORD = "some_valid_password";
    private static final String VALID_TOKEN = "some_valid_token";
    private static final String VALID_EMAIL = "some_valid_email@example.com";
    private static final Timestamp VALID_BIRTH_DATE = Timestamp.valueOf(LocalDateTime.now());
    private static final String VALID_PHONE = "0912345678";
    public static final String FAIL_PASSWORD = "some_fail_password";
    public static final String FAIL_USERNAME = "some_fail_username";
    private static final String FAIL_EMAIL = "some_fail_email";
    private static final String FAIL_BIRTH_DATE = "some_fail_date";
    public static final String VALID_AVATAR = "https://giaitri.vn/wp-content/uploads/2019/07/avatar-la-gi-01.jpg";
    public static final int STATUS_ACTIVE = 1;

    private JSONObject payload;
    private static JWTResponse jwtResponse;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @BeforeAll
    static void mockData() {
        jwtResponse = new JWTResponse();
        User user = new User();
        user.setPassword(VALID_PASSWORD);
        user.setEmail(VALID_EMAIL);
        user.setBirthDate(VALID_BIRTH_DATE);
        user.setUsername(VALID_USERNAME);
        jwtResponse.setUser(user);
        jwtResponse.setAccess_token(VALID_TOKEN);
    }

    @BeforeEach
    void emptyPayload() {
        payload = new JSONObject();
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
//        payload.put("username", VALID_USERNAME);
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
//        payload.put("username", VALID_USERNAME);
        payload.put("password", FAIL_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Đăng nhập với trường hợp sai username")
    void givenWrongUsername_whenLoginPostRequest_thenUnauthorized() throws Exception {
        payload.put("username", FAIL_USERNAME);
        payload.put("password", FAIL_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Đăng ký với thông tin hợp lệ")
    void givenValidBody_whenRegisterPostRequest_thenReturnOKAndJWTResponse() throws Exception {
        when(authService.register(any())).thenReturn(jwtResponse);

        payload.put("email", VALID_EMAIL);
        payload.put("password", VALID_PASSWORD);
        payload.put("confirm_password", VALID_PASSWORD);
        payload.put("phone", VALID_PHONE);
        payload.put("birth_date", VALID_BIRTH_DATE);
        payload.put("status", STATUS_ACTIVE);
        payload.put("avatar", VALID_AVATAR);

        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_REGISTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.access_token", is(VALID_TOKEN)))
                .andExpect(jsonPath("$.user.username", is(VALID_USERNAME)))
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andExpect(jsonPath("$.user.email", is(VALID_EMAIL)))
                .andExpect(jsonPath("$.user.birth_date").exists())
                .andExpect(jsonPath("$.user.phone").exists())
                .andExpect(jsonPath("$.user.status").exists())
                .andExpect(jsonPath("$.user.avatar").exists());
    }

    @Test
    @DisplayName("Đăng ký với body trống")
    void givenEmptyBody_whenRegisterPostRequest_thenBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_REGISTER))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Đăng ký với thông tin không hợp lệ")
    void givenInvalidEmail_whenRegisterPostRequest_thenBadRequest() throws Exception {
        when(authService.register(any())).thenThrow(ValidationException.class);
        payload.put("username", FAIL_USERNAME);
        payload.put("password", FAIL_PASSWORD);
        payload.put("email", FAIL_EMAIL);
        payload.put("birthDate", FAIL_BIRTH_DATE);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_REGISTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Đăng ký với tài khoản trùng email")
    void givenDuplicateEmail_whenRegisterPostRequest_thenConflict() throws Exception {
        when(authService.register(any())).thenThrow(DataIntegrityViolationException.class);

//        payload.put("username", VALID_USERNAME);
        payload.put("password", VALID_PASSWORD);
        payload.put("email", FAIL_EMAIL);
        payload.put("birthDate", VALID_BIRTH_DATE);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_REGISTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict());
    }


    @Test
    @DisplayName("Đăng ký với tài khoản trùng username")
    void givenDuplicateUsername_whenRegisterPostRequest_thenConflict() throws Exception {
        when(authService.register(any())).thenThrow(DataIntegrityViolationException.class);

        payload.put("username", FAIL_USERNAME);
        payload.put("password", VALID_PASSWORD);
        payload.put("email", VALID_EMAIL);
        payload.put("birthDate", VALID_BIRTH_DATE);
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_REGISTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict());
    }
}
