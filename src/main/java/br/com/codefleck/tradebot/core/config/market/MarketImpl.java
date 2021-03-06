package br.com.codefleck.tradebot.core.config.market;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import br.com.codefleck.tradebot.tradingInterfaces.Market;

/**
 * Holds information for an Exchange market.
 */
public final class MarketImpl implements Market {

  private String name;
  private String id;
  private String baseCurrency;
  private String counterCurrency;


  public MarketImpl(String name, String id, String baseCurrency, String counterCurrency) {
    this.id = id;
    this.name = name;
    this.baseCurrency = baseCurrency;
    this.counterCurrency = counterCurrency;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setBaseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }

  public String getBaseCurrency() {
    return baseCurrency;
  }

  public void setCounterCurrency(String counterCurrency) {
    this.counterCurrency = counterCurrency;
  }

  public String getCounterCurrency() {
    return counterCurrency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MarketImpl market = (MarketImpl) o;
    return Objects.equal(id, market.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("id", id)
        .add("baseCurrency", baseCurrency)
        .add("counterCurrency", counterCurrency)
        .toString();
  }
}
