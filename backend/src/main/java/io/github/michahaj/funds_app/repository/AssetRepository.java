package io.github.michahaj.funds_app.repository;

import io.github.michahaj.funds_app.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    boolean existsBySymbol(String symbol);
    Optional<Asset> findBySymbolAndExchange(String symbol, String exchange);
}