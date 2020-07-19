package com.codegym.vndreamers.dtos;

import com.codegym.vndreamers.models.User;
import lombok.Data;

@Data
public class JWTResponse {
    private String access_token;
    private User user;
}
