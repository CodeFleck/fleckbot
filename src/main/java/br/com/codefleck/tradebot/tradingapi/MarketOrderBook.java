package br.com.codefleck.tradebot.tradingapi;


import java.util.List;

/**
 * <p>
 * Represents a Market Order Book.
 * </p>
 * <p>
 * The Market Order Book SELL orders are ordered price ascending - <em>lowest</em> ASK price is first in list.
 * </p>
 * <p>
 * The Market Order Book BUY orders are ordered price descending - <em>highest</em> BID price is first in list.
 * </p>
 */
public interface MarketOrderBook {

  /**
   * Returns the market id for this Market Order Book.
   *
   * @return The market id.
   */
  String getMarketId();

  /**
   * Returns current SELL orders for the market.
   * Ordered price ascending - <em>lowest</em> ASK price is first in list.
   *
   * @return current SELL orders for the market.
   */
  List<MarketOrder> getSellOrders();

  /**
   * Return the current BUY orders for the market.
   * Ordered price descending - <em>highest</em> BID price is first in list.
   *
   * @return current BUY orders for the market.
   */
  List<MarketOrder> getBuyOrders();
}
