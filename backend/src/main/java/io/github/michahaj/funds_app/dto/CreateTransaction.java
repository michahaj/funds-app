package io.github.michahaj.funds_app.dto;

import io.github.michahaj.funds_app.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTransaction(
        @NotNull
        UUID portfolioId,
        String assetSymbol,
        String exchange,
        @NotNull(message = "Type is required")
        TransactionType type,
        @NotNull(message = "Quantity is required")
        @Positive
        BigDecimal quantity,
        @NotNull
        @Positive
        BigDecimal price,
        @NotNull
        @PastOrPresent
        LocalDateTime tradeDate
) {

}
