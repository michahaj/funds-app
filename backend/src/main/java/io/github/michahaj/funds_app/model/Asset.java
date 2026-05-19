package io.github.michahaj.funds_app.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_assets_symbol_exchange",
                columnNames = {"symbol", "exchange"})
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private AssetType type;

    @Column(name = "base_currency_code", length = 3, nullable = false)
    private String baseCurrencyCode;

    @Column(nullable = false)
    private String exchange;

    @Column(length = 12)
    private String isin;

    @Column(name = "current_price", precision = 19, scale = 8)
    private BigDecimal currentPrice;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "is_active")
    private boolean isActive = true;
}