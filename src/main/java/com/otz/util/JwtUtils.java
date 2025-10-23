package com.otz.util;

import com.otz.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Secret key (should be moved to application.properties for production)
    private final String jwtSecret = "MySuperSecretKeyForJWTGenerationInAIJobPortalProject123!";
    
    // Token validity: 1 day (in milliseconds)
    private final long jwtExpirationMs = 86400000;

    // Generate signing key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generate JWT token with payload
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())                   // Subject: user email
                .claim("role", user.getRole().name())         // Role claim
                .claim("userId", user.getId())                // User ID claim
                .setIssuedAt(new Date())                      // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with HS256
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    // Extract claims from JWT token
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract user email from token
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Extract role from token
    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Extract userId from token
    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }
}
