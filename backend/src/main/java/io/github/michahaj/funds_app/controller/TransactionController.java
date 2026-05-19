package io.github.michahaj.funds_app.controller;

import io.github.michahaj.funds_app.dto.CreateTransaction;
import io.github.michahaj.funds_app.dto.TransactionResponse;
import io.github.michahaj.funds_app.repository.TransactionRepository;
import io.github.michahaj.funds_app.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;


    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(Principal principal) {

        String userEmail = principal.getName();

        List<TransactionResponse> transactions = transactionRepository.findAllByPortfolioUserEmail(userEmail).stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getAsset().getSymbol(),
                        tx.getAsset().getExchange(),
                        tx.getType().name(),
                        tx.getQuantity(),
                        tx.getPricePerUnit(),
                        tx.getTradeDate(),
                        tx.getNote()
                ))
                .toList();

        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<String> createTransaction(
            @RequestBody @Valid CreateTransaction request,
            Principal principal
    ) {

        String userEmail = principal.getName();

        try {
            transactionService.addTransaction(request, userEmail);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created");
    }
}