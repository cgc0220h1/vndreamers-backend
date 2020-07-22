package com.codegym.vndreamers.services.auth;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.GenericService;
import com.codegym.vndreamers.services.auth.jwt.JWTIssuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    private JWTIssuer jwtIssuer;

    private GenericService<User> userService;

    @Autowired
    public void setJwtIssuer(JWTIssuer jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    @Autowired
    public void setUserService(GenericService<User> userService) {
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
        User userRegistered = saveUserToDB(user);

        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setUser(userRegistered);
        return jwtResponse;
    }

    private User saveUserToDB(User user) {
        return userService.save(user);
    }
}
