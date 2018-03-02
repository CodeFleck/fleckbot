package br.com.codefleck.tradebot.tradingapi;

/**
 * Holds information for an Exchange market.
 */
public interface Market {

  /**
   * Returns the market name, e.g. LTC_BTC, USD_BTC.
   *
   * @return the market name.
   */
  String getName();

  /**
   * Sets the market id, e.g. 3, btc_usd
   *
   * @param id the ID of the Market.
   */
  void setId(String id);

  /**
   * Returns the market id, e.g. 3, btc_usd
   *
   * @return the market id.
   */
  String getId();

  /**
   * <p>
   * Returns the base currency for the market currency pair.
   * </p>
   * <p>
   * When you buy or sell a currency pair, you are performing that action on the base currency.
   * E.g. in a LTC/BTC market, the first currency (LTC) is the base currency and the second currency (BTC) is the
   * counter currency.
   * </p>
   *
   * @return the base currency short code, e.g. LTC
   */
  String getBaseCurrency();

  /**
   * Returns the counter currency for the market currency pair. Also known as the quote currency.
   * E.g. in a LTC/BTC market, the first currency (LTC) is the base currency and the second currency (BTC) is the
   * counter currency.
   *
   * @return the counter currency short code, e.g. LTC
   */
  String getCounterCurrency();
}