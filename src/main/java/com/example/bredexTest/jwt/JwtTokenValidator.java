package com.example.bredexTest.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {
    @Autowired
    private TokenBlacklist jwtBlacklist;
    @Value("${jwt.secret}")
    private String secret;


    public boolean validateToken(String token) {
        if (jwtBlacklist.isBlacklisted(token)) {
            return false;
        }

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        String username = claims.getBody().getSubject();
        return new UsernamePasswordAuthenticationToken(username, null, null);
    }
}
