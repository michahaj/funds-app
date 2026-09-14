package io.github.michahaj.funds_app.controller;

import io.github.michahaj.funds_app.dto.LoginRequest;
import io.github.michahaj.funds_app.dto.RegisterRequest;
import io.github.michahaj.funds_app.model.User;
import io.github.michahaj.funds_app.repository.UserRepository;
import io.github.michahaj.funds_app.service.JwtService;
import io.github.michahaj.funds_app.service.RefreshTokenService;
import io.github.michahaj.funds_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        var userOptional = userRepository.findByEmail(request.email());

        if (userOptional.isEmpty() || !passwordEncoder.matches(request.password(), userOptional.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong email or password!");
        }

        User user = userOptional.get();

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        refreshTokenService.store(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/users")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body("Logged in successfully!");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is empty!");
        }

        try {
            if (!jwtService.isRefreshTokenValid(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid!");
            }

            String userEmail = jwtService.extractEmail(refreshToken);

            if (!refreshTokenService.consume(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token was revoked or already used!");
            }

            String newAccessToken = jwtService.generateAccessToken(userEmail);
            String newRefreshToken = jwtService.generateRefreshToken(userEmail);
            refreshTokenService.store(newRefreshToken);

            ResponseCookie newAccessCookie = ResponseCookie.from("access_token", newAccessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(15 * 60)
                    .sameSite("Lax")
                    .build();

            ResponseCookie newRefreshCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/api/users")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                    .body("Token refreshed successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error when refreshing token!");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    ) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revoke(refreshToken);
        }

        ResponseCookie accessCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/users")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body("Logged out successfully!");
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        return ResponseEntity.ok("Authenticated");
    }
}
