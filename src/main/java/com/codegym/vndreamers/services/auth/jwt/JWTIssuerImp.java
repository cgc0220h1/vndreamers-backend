package com.codegym.vndreamers.services.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTIssuerImp implements JWTIssuer {

    @Override
    public String generateToken(UserDetails userLogin) {
        Date loginTime = new Date();
        Date expiryTime = new Date();
        expiryTime.setTime(loginTime.getTime() + 24 * 60 * 60);
        return Jwts.builder()
                .setSubject(userLogin.getUsername())
                .setIssuedAt(loginTime)
                .setExpiration(expiryTime)
                .signWith(SignatureAlgorithm.HS512, "vndreamers")
                .compact();
    }


}
