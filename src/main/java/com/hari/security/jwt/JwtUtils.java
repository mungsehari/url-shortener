package com.hari.security.jwt;

import java.security.Key;
import java.util.stream.Collectors;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hari.service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key()).compact();

    }

    public String getUserNameFromJwtString(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build().parseClaimsJws(token)
                .getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException("Expired or invalid JWT token", e);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token", e);
        } catch (Exception e) {
            throw new RuntimeException("Expired or invalid JWT token", e);
        }
    }
}
