package io.github.michahaj.funds_app.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "assets")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 10)
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

    @Column(name = "is_active")
    private boolean isActive = true;
}