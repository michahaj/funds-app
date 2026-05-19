package io.github.michahaj.funds_app.service;

import io.github.michahaj.funds_app.integration.MarketDataProvider;
import io.github.michahaj.funds_app.model.Asset;
import io.github.michahaj.funds_app.repository.AssetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceUpdateService {

    private final AssetRepository assetRepository;
    private final List<MarketDataProvider> providers;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 10000)
    @Transactional
    public void updateAllPrices() {
        log.info("Price update request received");

        List<Asset> assets = assetRepository.findAll();

        for  (Asset asset : assets) {
            updateAssetPrice(asset);
        }

        log.info("Done Price update request");
    }

    private void updateAssetPrice(Asset asset) {
        providers.stream()
                .filter(p -> p.supports(asset))
                .findFirst()
                .ifPresentOrElse(
                        provider -> {
                            try {
                                BigDecimal newPrice = provider.fetchPrice(asset);
                                if (newPrice != null) {
                                    asset.setCurrentPrice(newPrice);
                                    asset.setLastUpdate(LocalDateTime.now());
                                    assetRepository.save(asset);
                                }
                            } catch (Exception e) {
                                log.error("Price fetching error (updating {} - {}): {}", asset.getSymbol(), asset.getExchange(), e.getMessage());
                            }
                        },
                        () -> log.warn("Can find provider for {} - {}", asset.getSymbol(), asset.getExchange())
                );
    }
}
