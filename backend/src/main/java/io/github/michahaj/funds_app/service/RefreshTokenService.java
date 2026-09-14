package io.github.michahaj.funds_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String KEY_PREFIX = "refresh-token:";

    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;

    public void store(String token) {
        if (!jwtService.isRefreshTokenValid(token)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Duration timeToLive = Duration.between(
                Instant.now(),
                jwtService.extractExpiration(token).toInstant()
        );

        if (timeToLive.isNegative() || timeToLive.isZero()) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        redisTemplate.opsForValue().set(
                key(token),
                jwtService.extractEmail(token),
                timeToLive
        );
    }

    public boolean consume(String token) {
        String storedEmail = redisTemplate.opsForValue().getAndDelete(key(token));
        return storedEmail != null && storedEmail.equals(jwtService.extractEmail(token));
    }

    public void revoke(String token) {
        redisTemplate.delete(key(token));
    }

    private String key(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return KEY_PREFIX + HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
