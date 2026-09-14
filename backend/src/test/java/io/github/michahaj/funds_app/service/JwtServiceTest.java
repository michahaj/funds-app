package io.github.michahaj.funds_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="
        );
    }

    @Test
    void acceptsOnlyAccessTokensForAuthentication() {
        String accessToken = jwtService.generateAccessToken("user@example.com");
        String refreshToken = jwtService.generateRefreshToken("user@example.com");

        assertThat(jwtService.isAccessTokenValid(accessToken)).isTrue();
        assertThat(jwtService.isAccessTokenValid(refreshToken)).isFalse();
    }

    @Test
    void acceptsOnlyRefreshTokensForRotation() {
        String accessToken = jwtService.generateAccessToken("user@example.com");
        String refreshToken = jwtService.generateRefreshToken("user@example.com");

        assertThat(jwtService.isRefreshTokenValid(refreshToken)).isTrue();
        assertThat(jwtService.isRefreshTokenValid(accessToken)).isFalse();
    }
}
