package io.github.michahaj.funds_app.controller;

import io.github.michahaj.funds_app.dto.CreatePortfolio;
import io.github.michahaj.funds_app.dto.PortfolioResponse;
import io.github.michahaj.funds_app.model.Portfolio;
import io.github.michahaj.funds_app.repository.PortfolioRepository;
import io.github.michahaj.funds_app.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioRepository portfolioRepository;

    @PostMapping
    public ResponseEntity<String> createPortfolio(
            @Valid @RequestBody CreatePortfolio request,
            Principal principal
            ) {
        String userEmail =  principal.getName();

        try {
            Portfolio savedPortfolio = portfolioService.createPortfolio(request, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPortfolio.getId().toString());
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> getUserPortfolios(Principal principal) {
        String userEmail = principal.getName();

        List<PortfolioResponse> portfolios = portfolioRepository.findAllByUserEmail(userEmail).stream()
                .map(p -> new PortfolioResponse(
                        p.getId(),
                        p.getName(),
                        p.getMainCurrencyCode(),
                        p.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(portfolios);
    }
}
