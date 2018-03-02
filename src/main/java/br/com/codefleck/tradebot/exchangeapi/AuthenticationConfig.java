package br.com.codefleck.tradebot.exchangeapi;

/**
 * Encapsulates any (optional) Authentication configuration for an Exchange Adapter.
 *
 */
public interface AuthenticationConfig {

  /**
   * Fetches a given config item by name.
   *
   * @param name the name of the item to fetch.
   * @return the item value if found, null otherwise.
   */
  String getItem(String name);
}
