package io.github.michahaj.funds_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RefreshTokenServiceTest {

    private final StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
    private final RefreshTokenService refreshTokenService = new RefreshTokenService(redisTemplate, jwtService);

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void storesOnlyAHashedTokenKeyWithAnExpiry() {
        when(jwtService.isRefreshTokenValid("raw-token")).thenReturn(true);
        when(jwtService.extractEmail("raw-token")).thenReturn("user@example.com");
        when(jwtService.extractExpiration("raw-token"))
                .thenReturn(Date.from(Instant.now().plus(Duration.ofDays(7))));

        refreshTokenService.store("raw-token");

        verify(valueOperations).set(
                eq("refresh-token:34d328009b123fbbb0dc93f18b3e6de1ecf7b1a5783c33dff7ffe1926f09e943"),
                eq("user@example.com"),
                any(Duration.class)
        );
    }

    @Test
    void consumesAStoredTokenAtomically() {
        when(valueOperations.getAndDelete(anyString())).thenReturn("user@example.com");
        when(jwtService.extractEmail("raw-token")).thenReturn("user@example.com");

        assertThat(refreshTokenService.consume("raw-token")).isTrue();
        verify(valueOperations).getAndDelete(anyString());
    }
}
