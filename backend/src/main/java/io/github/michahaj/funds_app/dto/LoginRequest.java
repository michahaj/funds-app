package io.github.michahaj.funds_app.dto;

public record LoginRequest(
        String email,
        String password
) {}