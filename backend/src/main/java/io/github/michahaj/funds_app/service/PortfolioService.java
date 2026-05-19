package io.github.michahaj.funds_app.service;

import io.github.michahaj.funds_app.dto.CreatePortfolio;
import io.github.michahaj.funds_app.model.Portfolio;
import io.github.michahaj.funds_app.model.User;
import io.github.michahaj.funds_app.repository.PortfolioRepository;
import io.github.michahaj.funds_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public Portfolio createPortfolio(CreatePortfolio request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Portfolio portfolio = Portfolio.builder()
                .name(request.name())
                .mainCurrencyCode(request.mainCurrencyCode())
                .user(user)
                .build();

        return portfolioRepository.save(portfolio);
    }

}
