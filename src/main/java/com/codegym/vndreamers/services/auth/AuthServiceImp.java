package com.codegym.vndreamers.services.auth;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    @Override
    public JWTResponse authenticate(User user) {
        return null;
    }

    @Override
    public JWTResponse register(User user) {
        return null;
    }
}
