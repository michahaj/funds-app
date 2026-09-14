package io.github.michahaj.funds_app.integration;

import io.github.michahaj.funds_app.model.Asset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class YahooFinanceProvider implements MarketDataProvider {

    private final RestClient restClient;

    public YahooFinanceProvider() {
        this.restClient = RestClient.builder()
                .baseUrl("https://query1.finance.yahoo.com")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Override
    public boolean supports(Asset asset) {
        return "GPW".equals(asset.getExchange()) || "US".equals(asset.getExchange());
    }

    @Override
    public BigDecimal fetchPrice(Asset asset) {
        String ticker = formatSymbol(asset);

        try {
            log.info("Download price for {} (Yahoo Ticker: {})", asset.getSymbol(), ticker);

            YahooResponse response = restClient.get()
                    .uri("/v8/finance/chart/{ticker}", ticker)
                    .retrieve()
                    .body(YahooResponse.class);

            if (response != null && response.chart() != null
                    && response.chart().result() != null
                    && !response.chart().result().isEmpty()) {

                return response.chart().result().get(0).meta().regularMarketPrice();
            }

        } catch (Exception e) {
            log.error("Cannot get price of {}: {}", ticker, e.getMessage());
        }

        return null;
    }

    private String formatSymbol(Asset asset) {
        if ("GPW".equals(asset.getExchange())) {
            return asset.getSymbol() + ".WA";
        }
        return asset.getSymbol();
    }
}

record YahooResponse(Chart chart) {}
record Chart(List<Result> result) {}
record Result(Meta meta) {}
record Meta(BigDecimal regularMarketPrice) {}