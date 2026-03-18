package io.github.michahaj.funds_app.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}