package br.com.codefleck.tradebot.exchanges.exchangeInterfaces;

/**
 * Encapsulates configuration for an Exchange Adapter.
 *
 */
public interface ExchangeConfig {

  /**
   * Returns the name of the exchange.
   *
   * @return the exchange name.
   */
  String getExchangeName();

  /**
   * Returns the fully qualified class name of the Exchange Adapter.
   *
   * @return the full class name (includes packages) of the Exchange Adapter.
   */
  String getExchangeAdapter();

  /**
   * Returns the authentication config.
   *
   * @return authentication config if present, null otherwise.
   */
  AuthenticationConfig getAuthenticationConfig();

  /**
   * Returns the network config.
   *
   * @return network config if present, null otherwise.
   */
  NetworkConfig getNetworkConfig();

  /**
   * Returns the optional config.
   *
   * @return other config if present, null otherwise.
   */
  OptionalConfig getOptionalConfig();
}
