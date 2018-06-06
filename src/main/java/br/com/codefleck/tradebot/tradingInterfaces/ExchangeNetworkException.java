package br.com.codefleck.tradebot.tradingInterfaces;

/**
 * <p>
 * This exception is thrown by the Exchange Adapter when there is a network error when attempting to connect to the
 * exchange to make an API call.
 * </p>
 * <p>
 * The non-fatal error response codes and messages specified in the exchange.xml config file determine whether this
 * exception is thrown by the Exchange Adapter.
 * </p>
 * <p>
 * If your Trading Strategy catches this exception, you could retry the API call, or exit from your Trading Strategy
 * and let the Trading Engine execute your Trading Strategy at the next trade cycle. This allows the you to recover from
 * temporary network issues.
 * </p>
 * <p>
 * If the Trading Engine receives these exceptions from directly calling an Exchange Adapter method, it will log the
 * event and sleep until the next trade cycle.
 * </p>
 *
 */
public final class ExchangeNetworkException extends Exception {

  private static final long serialVersionUID = 1090595894948829893L;

  /**
   * Constructor builds exception with error message.
   *
   * @param msg the error message.
   */
  public ExchangeNetworkException(String msg) {
    super(msg);
  }

  /**
   * Constructor builds exception with error message and original throwable.
   *
   * @param msg the error message.
   * @param e   the original exception.
   */
  public ExchangeNetworkException(String msg, Throwable e) {
    super(msg, e);
  }
}