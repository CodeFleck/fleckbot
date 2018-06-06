package br.com.codefleck.tradebot.exchanges.exchangeInterfaces;

/**
 * Encapsulates any optional configuration for an Exchange Adapter.
 *
 */
public interface OptionalConfig {

  /**
   * Fetches a given config item by name.
   *
   * @param name the name of the item to fetch.
   * @return the item value if found, null otherwise.
   */
  String getItem(String name);
}
