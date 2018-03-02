package br.com.codefleck.tradebot.exchanges.trading.api.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.tradingapi.BalanceInfo;

/**
 * A Balance Info implementation that can be used by Exchange Adapters.
 */
public final class BalanceInfoImpl implements BalanceInfo {

  private Map<String, BigDecimal> balancesAvailable;
  private Map<String, BigDecimal> balancesOnHold;


  public BalanceInfoImpl(Map<String, BigDecimal> balancesAvailable, Map<String, BigDecimal> balancesOnHold) {
    this.balancesAvailable = balancesAvailable;
    this.balancesOnHold = balancesOnHold;
  }

  public Map<String, BigDecimal> getBalancesAvailable() {
    return balancesAvailable;
  }

  public void setBalancesAvailable(Map<String, BigDecimal> balancesAvailable) {
    this.balancesAvailable = balancesAvailable;
  }

  public Map<String, BigDecimal> getBalancesOnHold() {
    return balancesOnHold;
  }

  public void setBalancesOnHold(Map<String, BigDecimal> balancesOnHold) {
    this.balancesOnHold = balancesOnHold;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("balancesAvailable", balancesAvailable)
        .add("balancesOnHold", balancesOnHold)
        .toString();
  }
}

