package com.codegym.vndreamers.security.jwt.issuer;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class JWTIssuerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String API_AUTH_LOGIN = "/auth/login";
    private static final String DUMMY_USERNAME = "dummy_username";
    private static final String DUMMY_PASSWORD = "dummy_password";

    @Test
    void giveEmptyBody_whenLoginPostRequest_thenBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_AUTH_LOGIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNoToken_whenLoginPostRequest_thenOkAndReturnUserWithToken() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("username", DUMMY_USERNAME);
        payload.put("password", DUMMY_PASSWORD);
        System.out.println(payload);

        mockMvc.perform(MockMvcRequestBuilders.get(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.user", is(notNullValue())));
    }

    @Test
    void givenToken_whenLoginPostRequest_thenOkAndReturnUser() {

    }
}
