package br.com.codefleck.tradebot.tradingInterfaces;

import java.math.BigDecimal;

/**
 * Holds Exchange Ticker information.
 * <p>
 * Not all exchanges provide the information returned in the Ticker methods - you'll need to check the relevant
 * Exchange Adapter code/Javadoc and online Exchange API documentation.
 * <p>
 * If the exchange does not provide the information, a null value is returned.
 */
public interface Ticker {

  /**
   * Returns the last trade price.
   *
   * @return the last trade price if the exchange provides it, null otherwise.
   */
  BigDecimal getLast();

  /**
   * Returns the highest buy order price.
   *
   * @return the highest but order price if the exchange provides it, null otherwise.
   */
  BigDecimal getBid();

  /**
   * Returns the lowest sell order price.
   *
   * @return the lowest sell order price if the exchange provides it, null otherwise.
   */
  BigDecimal getAsk();

  /**
   * Returns the last 24 hours price low.
   *
   * @return the last 24 hours price low if the exchange provides it, null otherwise.
   */
  BigDecimal getLow();

  /**
   * Returns the last 24 hours price high.
   *
   * @return the last 24 hours price high if the exchange provides it, null otherwise.
   */
  BigDecimal getHigh();

  /**
   * Returns the first trade price of the day.
   *
   * @return the first trade price of the day if the exchange provides it, null otherwise.
   */
  BigDecimal getOpen();

  /**
   * Returns the last 24 hours volume.
   *
   * @return the last 24 hours volume if the exchange provides it, null otherwise.
   */
  BigDecimal getVolume();

  /**
   * Returns the last 24 hours volume weighted average - https://en.wikipedia.org/wiki/Volume-weighted_average_price
   *
   * @return the last 24 hours volume weighted average if the exchange provides it, null otherwise.
   */
  BigDecimal getVwap();

  /**
   * Returns the current time on the exchange in UNIX time format.
   *
   * @return the current time on the exchange if provided, null otherwise.
   */
  Long getTimestamp();
}
