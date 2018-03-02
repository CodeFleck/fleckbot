package br.com.codefleck.tradebot.tradingapi;

/**
 * Defines the different order types for sending to the exchange.
 */
public enum OrderType {

  /**
   * Buy order.
   */
  BUY("Buy"),

  /**
   * Sell order.
   */
  SELL("Sell");

  private final String orderType;

  OrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getStringValue() {
    return orderType;
  }
}