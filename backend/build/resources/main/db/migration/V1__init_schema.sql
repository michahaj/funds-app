CREATE TYPE asset_type AS ENUM ('STOCK', 'CRYPTO', 'ETF', 'COMMODITY', 'CASH');
CREATE TYPE transaction_type AS ENUM ('BUY', 'SELL', 'DIVIDEND', 'TAX', 'FEE');

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(255) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE assets (
                        id SERIAL PRIMARY KEY,
                        symbol VARCHAR(10) UNIQUE NOT NULL,
                        full_name VARCHAR(100),
                        type asset_type NOT NULL,
                        base_currency_code VARCHAR(3) NOT NULL,
                        is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE portfolios (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            name VARCHAR(50) NOT NULL,
                            main_currency_code VARCHAR(3) DEFAULT 'PLN',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              portfolio_id UUID NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
                              asset_id INTEGER NOT NULL REFERENCES assets(id),
                              type transaction_type NOT NULL,
                              quantity DECIMAL(19, 8) NOT NULL,
                              price_per_unit DECIMAL(19, 4) NOT NULL,
                              commission DECIMAL(19, 4) DEFAULT 0,
                              exchange_rate DECIMAL(19, 6) DEFAULT 1,
                              trade_date TIMESTAMP NOT NULL,
                              note TEXT
);

CREATE TABLE quotes (
                        id BIGSERIAL PRIMARY KEY,
                        asset_id INTEGER NOT NULL REFERENCES assets(id) ON DELETE CASCADE,
                        price DECIMAL(19, 4) NOT NULL,
                        timestamp TIMESTAMP NOT NULL
);