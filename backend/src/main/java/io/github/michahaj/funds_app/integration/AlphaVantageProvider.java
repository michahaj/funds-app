package io.github.michahaj.funds_app.integration;

import io.github.michahaj.funds_app.dto.AlphaVantageResponse;
import io.github.michahaj.funds_app.model.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlphaVantageProvider implements MarketDataProvider{

    private final RestClient restClient;

    @Value("${app.api.alpha-vantage-key}")
    private String apiKey;


    @Override
    public boolean supports(Asset asset) {
        String exchange = asset.getExchange().toUpperCase();
        return exchange.equals("NASDAQ") || exchange.equals("NYSE");
    }

    @Override
    public BigDecimal fetchPrice(Asset asset) {
        log.info("Fetching pirce from Alpha Vantage from: {}", asset.getSymbol());

        try {
            AlphaVantageResponse response = restClient.get()
                    .uri("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={key}",
                            asset.getSymbol(), apiKey)
                    .retrieve()
                    .body(AlphaVantageResponse.class);

            if (response != null && response.quote() != null) {
                return response.quote().price();
            }
        } catch (Exception e) {
            log.error("Fetching price error {}: {}", asset.getSymbol(), e.getMessage());
        }

        return null;
    }
}
