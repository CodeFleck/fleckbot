package br.com.codefleck.tradebot.core.config.exchange;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.exchangeapi.AuthenticationConfig;

/**
 * Exchange API Authentication config.
 */
public class AuthenticationConfigImpl implements AuthenticationConfig {

  private Map<String, String> items;

  public AuthenticationConfigImpl() {
    items = new HashMap<>();
  }

  @Override
  public String getItem(String name) {
    return items.get(name);
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
        // WARNING - careful showing this!
        // .add("items", items)
        .toString();
  }
}
