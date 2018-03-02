package br.com.codefleck.tradebot.exchanges.trading.api.impl;

import java.util.List;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.tradingapi.MarketOrder;
import br.com.codefleck.tradebot.tradingapi.MarketOrderBook;

/**
 * A Market Order Book implementation that can be used by Exchange Adapters.
 */
public final class MarketOrderBookImpl implements MarketOrderBook {

  private String marketId;
  private List<MarketOrder> sellOrders;
  private List<MarketOrder> buyOrders;


  public MarketOrderBookImpl(String marketId, List<MarketOrder> sellOrders, List<MarketOrder> buyOrders) {
    this.marketId = marketId;
    this.sellOrders = sellOrders;
    this.buyOrders = buyOrders;
  }

  public String getMarketId() {
    return marketId;
  }

  public void setMarketId(String marketId) {
    this.marketId = marketId;
  }

  public List<MarketOrder> getSellOrders() {
    return sellOrders;
  }

  public void setSellOrders(List<MarketOrder> sellOrders) {
    this.sellOrders = sellOrders;
  }

  public List<MarketOrder> getBuyOrders() {
    return buyOrders;
  }

  public void setBuyOrders(List<MarketOrder> buyOrders) {
    this.buyOrders = buyOrders;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("marketId", marketId)
        .add("sellOrders", sellOrders)
        .add("buyOrders", buyOrders)
        .toString();
  }
}