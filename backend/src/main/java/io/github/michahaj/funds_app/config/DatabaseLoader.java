package io.github.michahaj.funds_app.config;

import io.github.michahaj.funds_app.model.*;
import io.github.michahaj.funds_app.repository.AssetRepository;
import io.github.michahaj.funds_app.repository.PortfolioRepository;
import io.github.michahaj.funds_app.repository.TransactionRepository;
import io.github.michahaj.funds_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void run(String... args) {
        seedUserAndPortfolio();
        seedAssets();
        seedTransactions();
    }

    private void seedUserAndPortfolio() {
        String testEmail = "test@example.com";

        User user = userRepository.findByEmail(testEmail)
                .orElseGet(() -> {
                    log.info("Creating test user...");
                    return userRepository.save(User.builder()
                            .username("MichalTester")
                            .email(testEmail)
                            .passwordHash("secret")
                            .build());
                });

        if (portfolioRepository.findAll().isEmpty()) {
            log.info("Creating new portfolio for test user: {}", user.getUsername());

            Portfolio portfolio = Portfolio.builder()
                    .name("Wallet 0")
                    .mainCurrencyCode("PLN")
                    .user(user)
                    .build();

            portfolioRepository.save(portfolio);
        }
    }

    private void seedAssets() {
        if (assetRepository.count() == 0) {
            log.info("Adding test assets...");

            List<Asset> assets = List.of(
                    Asset.builder().symbol("BTC").exchange("BINANCE").fullName("Bitcoin").type(AssetType.CRYPTO).baseCurrencyCode("USD").build(),
                    Asset.builder().symbol("AAPL").exchange("NASDAQ").fullName("Apple Inc.").type(AssetType.STOCK).baseCurrencyCode("USD").build(),
                    Asset.builder().symbol("PKO").exchange("GPW").fullName("PKO BP").type(AssetType.STOCK).baseCurrencyCode("PLN").build(),
                    Asset.builder().symbol("CDR").exchange("GPW").fullName("CD Projekt").type(AssetType.STOCK).baseCurrencyCode("PLN").build()
            );

            assetRepository.saveAll(assets);
        }
    }

    private void seedTransactions() {
        if (transactionRepository.count() == 0) {

            Portfolio portfolio = portfolioRepository.findAll().get(0);

            Asset apple = assetRepository.findBySymbolAndExchange("AAPL", "NASDAQ")
                    .orElseThrow(() -> new RuntimeException("AAPL not found"));

            Transaction buyApple = Transaction.builder()
                    .portfolio(portfolio)
                    .asset(apple)
                    .type(TransactionType.BUY)
                    .quantity(new BigDecimal("1.00000000"))
                    .pricePerUnit(new BigDecimal("175.5000"))
                    .commission(new BigDecimal("2.5000"))
                    .exchangeRate(new BigDecimal("3.950000"))
                    .tradeDate(LocalDateTime.now().minusDays(2))
                    .note("My first stock:)")
                    .build();

            transactionRepository.save(buyApple);
        }
    }
}