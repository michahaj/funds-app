package io.github.michahaj.funds_app.controller;

import io.github.michahaj.funds_app.dto.TransactionResponse;
import io.github.michahaj.funds_app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping
    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll().stream()
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
    }
}