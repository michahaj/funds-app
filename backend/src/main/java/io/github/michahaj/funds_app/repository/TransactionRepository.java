package io.github.michahaj.funds_app.repository;

import io.github.michahaj.funds_app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}