package br.com.codefleck.tradebot.core.config.strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.strategyapi.StrategyConfig;

/**
 * Encapsulates (optional) Strategy Config Items.
 */
public final class StrategyConfigItems implements StrategyConfig {

  private Map<String, String> items = new HashMap<>();

  @Override
  public String getConfigItem(String key) {
    return items.get(key);
  }

  @Override
  public int getNumberOfConfigItems() {
    return items.size();
  }

  @Override
  public Set<String> getConfigItemKeys() {
    return Collections.unmodifiableSet(items.keySet());
  }

  public void setItems(Map<String, String> items) {
    this.items = items;
  }

  public Map<String, String> getItems() {
    return items;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("items", items)
        .toString();
  }
}
