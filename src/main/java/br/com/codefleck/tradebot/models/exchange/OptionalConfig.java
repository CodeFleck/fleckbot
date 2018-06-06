package br.com.codefleck.tradebot.models.exchange;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;

/**
 * Domain object representing optional Exchange config.
 */
public class OptionalConfig {

  private Map<String, String> items;

  public OptionalConfig() {
    items = new HashMap<>();
  }

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
