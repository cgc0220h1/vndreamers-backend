package com.codegym.vndreamers.services.auth;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.jwt.JWTIssuer;
import com.codegym.vndreamers.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    private JWTIssuer jwtIssuer;

    private UserService userService;

    @Autowired
    public void setJwtIssuer(JWTIssuer jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public JWTResponse authenticate(User user) {
        return null;
    }

    @Override
    public JWTResponse register(User user) {
        JWTResponse jwtResponse = new JWTResponse();
        String accessToken = jwtIssuer.generateToken(user);
        User userRegistered = userService.save(user);
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setUser(userRegistered);
        return jwtResponse;
    }
}
