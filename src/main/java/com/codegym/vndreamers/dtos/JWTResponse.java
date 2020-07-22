package com.codegym.vndreamers.dtos;

import com.codegym.vndreamers.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JWTResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;
    private User user;
}
