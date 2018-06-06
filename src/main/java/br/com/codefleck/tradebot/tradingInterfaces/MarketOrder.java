package br.com.codefleck.tradebot.tradingInterfaces;


import java.math.BigDecimal;

/**
 * <p>
 * Represents a Market Order on the exchange.
 * </p>
 * <p>
 * The type of order (buy/sell) is determined by the {@link OrderType}.
 * </p>
 */
public interface MarketOrder {

  /**
   * Returns the type of order. Value will be {@link OrderType#BUY} or {@link OrderType#SELL}.
   *
   * @return the type of order.
   */
  OrderType getType();

  /**
   * Returns the price of the order. This is usually in BTC or USD.
   *
   * @return Price of the order.
   */
  BigDecimal getPrice();

  /**
   * Returns the quantity of the order. This is usually the amount of the other currency you want to trade for BTC/USD.
   *
   * @return Quantity of the order.
   */
  BigDecimal getQuantity();

  /**
   * Returns the total value of order (price * quantity). This is usually in BTC or USD.
   *
   * @return Total value of order (price * quantity).
   */
  BigDecimal getTotal();
}
