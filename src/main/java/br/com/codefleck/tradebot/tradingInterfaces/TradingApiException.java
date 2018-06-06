package br.com.codefleck.tradebot.tradingInterfaces;

/**
 * <p>
 * This exception is thrown from Exchange Adapter implementations when there is a problem making an API call to the exchange.
 * </p>
 * <p>
 * If your Trading Strategy receives this exception, this means something bad as happened; you would probably want to
 * wrap this exception in a StrategyException and let the Trading Engine shutdown the bot immediately to prevent
 * unexpected losses.
 * </p>
 * <p>
 * If the Trading Engine receives one of these exceptions from directly calling an Exchange Adapter method,
 * it shuts down the bot immediately.
 * </p>
 *
 */
public class TradingApiException extends Exception {

  private static final long serialVersionUID = -8279304672615688060L;

  /**
   * Constructor builds exception with error message.
   *
   * @param msg the error message.
   */
  public TradingApiException(String msg) {
    super(msg);
  }

  /**
   * Constructor builds exception with error message and original throwable.
   *
   * @param msg the error message.
   * @param e   the original exception.
   */
  public TradingApiException(String msg, Throwable e) {
    super(msg, e);
  }
}
