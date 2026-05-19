package io.github.michahaj.funds_app.service;

import io.github.michahaj.funds_app.dto.CreateTransaction;
import io.github.michahaj.funds_app.model.Asset;
import io.github.michahaj.funds_app.model.Portfolio;
import io.github.michahaj.funds_app.model.Transaction;
import io.github.michahaj.funds_app.model.User;
import io.github.michahaj.funds_app.repository.AssetRepository;
import io.github.michahaj.funds_app.repository.PortfolioRepository;
import io.github.michahaj.funds_app.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction addTransaction(CreateTransaction request, String userEmail) {
        final UUID portfolioId = request.portfolioId();
        final Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("portfolio not found"));

        final Asset asset = assetRepository.findBySymbolAndExchange(
                request.assetSymbol(),
                request.exchange()
        ).orElseThrow(() -> new RuntimeException("asset not found"));

        final User portfolioUser = portfolio.getUser();

        if (!portfolioUser.getEmail().equals(userEmail)) {
            throw new RuntimeException("User " + userEmail + " does not belong to this portfolio");
        }

        Transaction newTransaction = Transaction.builder()
                .portfolio(portfolio)
                .asset(asset)
                .type(request.type())
                .quantity(request.quantity())
                .pricePerUnit(request.price())
                .tradeDate(request.tradeDate())
                .build();

        return transactionRepository.save(newTransaction);
    }
}
