ALTER TABLE assets DROP CONSTRAINT IF EXISTS assets_symbol_key;

ALTER TABLE assets ALTER COLUMN symbol TYPE VARCHAR(20);

ALTER TABLE assets ALTER COLUMN current_price TYPE NUMERIC(19, 8);

ALTER TABLE assets ADD COLUMN isin VARCHAR(12);

ALTER TABLE assets ADD CONSTRAINT uk_assets_symbol_exchange UNIQUE (symbol, exchange);