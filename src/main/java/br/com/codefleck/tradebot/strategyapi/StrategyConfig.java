package br.com.codefleck.tradebot.strategyapi;

import java.util.Set;

/**
 * <p>
 * Encapsulates any (optional) configuration for a Trading Strategy.
 * Basically just a map of key-value pairs.
 * </p>
 *
 * <p>
 * Configuration comes from the strategies.xml file.
 * </p>
 *
 */
public interface StrategyConfig {

  /**
   * Fetches a config item for a given key.
   *
   * @param key the key of the item to fetch.
   * @return value of the item if found, null otherwise.
   */
  String getConfigItem(String key);

  /**
   * Returns the number of config items.
   * @return the number of config items.
   */
  int getNumberOfConfigItems();

  /**
   * Returns all of the config item keys.
   * @return all of the config item keys.
   */
  Set<String> getConfigItemKeys();
}

