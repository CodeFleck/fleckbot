package br.com.codefleck.tradebot.strategyapi;

import br.com.codefleck.tradebot.tradingInterfaces.Market;
import br.com.codefleck.tradebot.tradingInterfaces.TradingApi;

/**
 * <p>
 * All user defined Trading Strategies must implement this interface.
 * </p>
 * <p>
 * The Trading Engine will send only 1 thread through your strategy code at a time - you do not have to code for concurrency.
 * </p>
 *
 */
public interface TradingStrategy {

  /**
   * Called once by the Trading Engine when it starts up.
   *
   * @param tradingApi the Trading API.
   * @param market     the market for this strategy.
   * @param config     optional configuration for the strategy.
   */
  void init(TradingApi tradingApi, Market market, StrategyConfig config);

  /**
   * <p>
   * Called by the Trading Engine during each trade cycle.
   * </p>
   * <p>
   * Here, you can create some orders, cancel some, buy some beer... do whatever you want.
   * </p>
   *
   * @throws StrategyException if something goes bad. Trading Strategy implementations should throw this exception
   *                           if they want the Trading Engine to shutdown the bot immediately.
   */
  void execute() throws StrategyException;
}
