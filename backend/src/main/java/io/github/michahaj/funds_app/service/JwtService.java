package io.github.michahaj.funds_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME_ACCESS_TOKEN = 1000 * 60 * 15;
    private static final long EXPIRATION_TIME_REFRESH_TOKEN = 1000 * 60 * 60 * 24 * 7;

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN))
                .signWith(getSignInKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH_TOKEN))
                .signWith(getSignInKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public boolean isAccessTokenValid(String token) {
        return isTokenValid(token, ACCESS_TOKEN_TYPE);
    }

    public boolean isRefreshTokenValid(String token) {
        return isTokenValid(token, REFRESH_TOKEN_TYPE);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenValid(String token, String expectedType) {
        try {
            Claims claims = extractClaims(token);
            return expectedType.equals(claims.get(TOKEN_TYPE_CLAIM, String.class));
        } catch (Exception e) {
            return false;
        }
    }
}
