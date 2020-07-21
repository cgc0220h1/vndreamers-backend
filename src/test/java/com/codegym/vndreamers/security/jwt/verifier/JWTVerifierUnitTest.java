package com.codegym.vndreamers.security.jwt.verifier;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebMvcTest
public class JWTVerifierUnitTest {
    private static final String API_AUTH_LOGIN = "/auth/login";
    private static final String VALID_USERNAME = "some_valid_username";
    private static final String VALID_PASSWORD = "some_valid_password";
    private static final String VALID_TOKEN = "some_valid_token";
    private static final String VALID_EMAIL = "some_valid_email@example.com";
    private static final Timestamp VALID_BIRTH_DATE = Timestamp.valueOf(LocalDateTime.now());
    private static JWTResponse jwtResponse;

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void mockData() {
        jwtResponse = new JWTResponse();
        User user = new User();
//        user.setUsername(VALID_USERNAME);
        user.setPassword(VALID_PASSWORD);
        user.setEmail(VALID_EMAIL);
        user.setBirthDate(VALID_BIRTH_DATE);
        jwtResponse.setUser(user);
        jwtResponse.setAccess_token(VALID_TOKEN);
    }

    @Test
    @DisplayName("Đã đăng nhập và lấy các tài nguyên")
    @WithMockUser
    void givenToken_whenGetRequestResource_thenJWTResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/**"));
    }
}
