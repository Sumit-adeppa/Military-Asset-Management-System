package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey signingKey;

    private SecretKey getSigningKey() {
        if (signingKey == null) {
            signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return signingKey;
    }

    public String generateToken(Long userId, String username, String role, Long baseId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        var builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate);

        if (baseId != null) {
            builder.claim("baseId", baseId);
        }

        return builder.signWith(getSigningKey()).compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(validateToken(token).getSubject());
    }

    public String getUsernameFromToken(String token) {
        return validateToken(token).get("username", String.class);
    }

    public String getRoleFromToken(String token) {
        return validateToken(token).get("role", String.class);
    }

    public Long getBaseIdFromToken(String token) {
        return validateToken(token).get("baseId", Long.class);
    }
}
