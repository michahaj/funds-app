package io.github.michahaj.funds_app.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePortfolio(
        @NotBlank
        String name,
        @Size(min = 3, max = 3)
        String mainCurrencyCode
) {
}
