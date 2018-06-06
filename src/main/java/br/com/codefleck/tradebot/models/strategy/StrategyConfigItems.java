package br.com.codefleck.tradebot.models.strategy;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;

/**
 * Domain object representing (optional) Strategy Config Items.
 */
final class StrategyConfigItems {

  private Map<String, String> items = new HashMap<>();

  public Map<String, String> getItems() {
    return items;
  }

  public void setItems(Map<String, String> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("items", items)
        .toString();
  }
}
