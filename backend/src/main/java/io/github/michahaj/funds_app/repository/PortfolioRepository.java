package io.github.michahaj.funds_app.repository;

import io.github.michahaj.funds_app.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    Portfolio findById(String id);
    List<Portfolio> findAllByUserEmail(String email);
}