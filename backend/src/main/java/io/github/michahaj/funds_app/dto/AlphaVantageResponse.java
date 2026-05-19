package io.github.michahaj.funds_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AlphaVantageResponse(
        @JsonProperty("Global Quote")
        GlobalQuote quote
) {
    public record GlobalQuote(
            @JsonProperty("01. symbol") String symbol,
            @JsonProperty("05. price") BigDecimal price
    ) {}
}
