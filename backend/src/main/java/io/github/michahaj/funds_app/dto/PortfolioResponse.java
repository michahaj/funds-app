package io.github.michahaj.funds_app.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioResponse(
        UUID id,
        String name,
        String mainCurrencyCode,
        LocalDateTime createdAt
) {

}
