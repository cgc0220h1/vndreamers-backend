package com.codegym.vndreamers.services.auth.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTIssuer {
    String generateToken(UserDetails userDetails);
    String getUsernameFromJWT(String token);
    boolean validateToken(String authToken);
}
