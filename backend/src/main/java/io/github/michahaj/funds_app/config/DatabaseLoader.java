package io.github.michahaj.funds_app.config;

import io.github.michahaj.funds_app.model.Portfolio;
import io.github.michahaj.funds_app.model.User;
import io.github.michahaj.funds_app.repository.PortfolioRepository;
import io.github.michahaj.funds_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public void run(String... args) {
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
}