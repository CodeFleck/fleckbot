<p align="center">
  <img src="src/main/webapp/assets/imagens/logo.png" alt="FleckBot" width="140"/>
</p>

<h1 align="center">FleckBot</h1>

<p align="center"><b>An autonomous Bitcoin trading bot with deep-learning price prediction — built in 2018.</b></p>

FleckBot watched the Bitfinex BTC/USD market and put a neural network behind the trading decision: LSTM networks (DeepLearning4j) trained on OHLCV history to predict the next period's price, a prediction-driven strategy backtested against a dual-SMA baseline on four years of Coinbase minute data, and a Spring MVC dashboard (in Portuguese) to drive the whole thing.

> **2026 — FleckBot is being reborn.** Same bot, new brain: the LSTM has been replaced by **Claude** as the market analyst, with a human approving every trade and an honest scoreboard measuring whether the bot actually beats buy & hold. The original interface you see here is being faithfully recreated in React as part of the rebuild.

## What it does

| Page | Feature |
|---|---|
| **Análise** | Live BTC/USD technical-analysis chart (TradingView) |
| **Carteiras** | Exchange wallet balances across 10 assets, with allocation pie chart |
| **Playground** | Backtesting sandbox: dual-SMA or LSTM-prediction strategies over 2014–2018 Coinbase minute data, any timeframe from 1 minute to 1 month — orders, fees, P&L vs HODL |
| **Redes Neurais** | Train LSTM networks on any date range and price field (close/open/high/low/volume), then chart predicted vs. actual price with error metrics |
| **Logs** | Paginated engine activity log |

## How it works

- **Trading engine** — a single-threaded control loop (inspired by BX-bot) that loads exchange, market, and strategy configuration from an XML datastore, monitors an emergency-stop balance, and emails a critical alert if anything goes wrong.
- **Exchange adapters** — Bitfinex API v1 (HMAC-SHA384 signed) for live data and orders, plus a dry-run test adapter backed by Bitstamp public data.
- **Neural nets** — 2× GravesLSTM (256 units) → dense → regression head (DL4J), min-max-normalized 5-feature OHLCV input, 178-bar window, one-step-ahead prediction, MSE loss.
- **Backtesting** — ta4j strategies over Coinbase 1-minute bars downsampled to the chosen timeframe; per-trade fee accounting (0.1%/side) and buy-&-hold comparison.

## Stack

Java 8 · Spring MVC 4.3 · Spring Security · DeepLearning4j 0.9 · ta4j · JSP/Bootstrap 4 · PostgreSQL · JAXB XML config store

## Setup

1. **Database** — edit `src/main/resources/application.properties` with your PostgreSQL url/login/password.
2. **Exchange** — add your own API credentials to `src/main/resources/xml/exchange.xml` (never commit real keys).
3. **Historical data** — the Playground and neural-net pages expect the Kaggle Coinbase dataset (`coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv`) in `~/csv/`.
4. **Login** — development credentials are defined in `conf/SecurityConfig.java`.

---

*This repo is kept as-is as the origin of the FleckBot story. Active development happens in the V2 rebuild.*
