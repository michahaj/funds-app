-- Migration fix problem with assets uniqueness - same symbol could exist in different exchanges
-- (for example ABC on GPW can be something different from ABC on NASDAQ)

ALTER TABLE assets DROP CONSTRAINT assets_symbol_key;

ALTER TABLE assets ADD COLUMN exchange VARCHAR(20) DEFAULT 'UNKNOWN';

ALTER TABLE assets ADD CONSTRAINT asset_symbol_exchange_unique UNIQUE (symbol, exchange);