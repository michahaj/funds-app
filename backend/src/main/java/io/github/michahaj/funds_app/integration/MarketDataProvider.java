package io.github.michahaj.funds_app.integration;

import io.github.michahaj.funds_app.model.Asset;

import java.math.BigDecimal;

public interface MarketDataProvider {
    boolean supports(Asset asset);

    BigDecimal fetchPrice(Asset asset);
}
