package com.codegym.vndreamers.services.auth;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;

public interface AuthService {
    JWTResponse authenticate(User user);
}
