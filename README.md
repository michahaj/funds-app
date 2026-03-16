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

![database-schema](https://i.imgur.com/wXlFTdB.png)
