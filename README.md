# Fund Tracker

...

## Database schema

### assets

Everything that can be bought (stocks, bonds, crypto)

| Column | Type | Details |
| -------- | -------- | -------- |
| id     | UUID     | Primarly key     |
| symbol | VARCHAR(10) | Ticker (for example `AAPL`) |
| name | VARCHAR(100) | Fullname (for example `Apple Inc.`) |
| type | ENUM | `STOCK`, `CRYPTO`, `BOND`, `ETF` ... |
| currency_code | VARCHAR(3) | for example `USD`, `PLN` |
| exchange | VARCHAR(20) | exchange (for uniqueness) |

### quotes

Prices fetched from APIs.
| Column | Type | Details |
| -------- | -------- | -------- |
| asset_id     | FK (assets)     | FK     |
| price | DECIMAL(19,4) | Current price |
| timestamp | TIMESTAMP |  |

### portfolios

User wallets
| Column | Type | Details |
| -------- | -------- | -------- |
| id     | UUID     | Primarly key     |
| user_id | FK (users) | FK |
| name | VARCHAR(50) | Wallet name |
| base_currency | VARCHAR(3) | for example `USD` |

### transactions

Main table - all transactions.
| Column | Type | Details |
| -------- | -------- | -------- |
| id     | UUID     | Primarly key     |
| portfolio_id | FK (portfolios) | wallet linked to transaction |
| asset_id | FK (assets) | what was bought/sold |
| type | ENUM | `BUY`, `SELL`, `DIVIDEND` |
| quantity | DECIMAL(19,8) | |
| price_per_unit | DECIMAL(19,4) | |
| commision | DECIMAL(19,4) | |
| fx_rate | DECIMAL(19,6) | exchange rate at the time of purchase |
| trade_date | TIMESTAMP | |
 

![database-schema](https://i.imgur.com/wXlFTdB.png)
