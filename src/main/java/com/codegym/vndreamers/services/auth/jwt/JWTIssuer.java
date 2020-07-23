package com.codegym.vndreamers.services.auth.jwt;

import com.codegym.vndreamers.exceptions.UserExistException;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTIssuer {
    String generateToken(UserDetails userDetails) throws UserExistException;
    String getUsernameFromJWT(String token);
    boolean validateToken(String authToken);
}
