package br.com.codefleck.tradebot.exchangeapi;

import br.com.codefleck.tradebot.tradingapi.TradingApi;

/**
 * <p>
 * All Exchange Adapters must implement this interface. It's main purpose is for the Trading Engine to pass the
 * adapter its configuration on startup.
 * </p>
 * <p>
 * The Trading Engine will send only 1 thread through the Exchange Adapter code at a time - you do not have to code for concurrency.
 * </p>
 *
 */
public interface ExchangeAdapter extends TradingApi {

  /**
   * Called once by the Trading Engine when it starts up.
   *
   * @param config configuration for the Exchange Adapter.
   */
  void init(ExchangeConfig config);
}