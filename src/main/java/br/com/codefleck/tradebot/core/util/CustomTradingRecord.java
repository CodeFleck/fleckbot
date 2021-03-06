package br.com.codefleck.tradebot.core.util;

import java.io.Serializable;
import java.util.List;

import org.ta4j.core.*;

    /**
     * A history/record of a trading session.
     * <p></p>
     * Holds the full trading record when running a {@link Strategy strategy}.
     * It is used to:
     * <ul>
     *     <li>check to satisfaction of some trading rules (when running a strategy)
     *     <li>analyze the performance of a trading strategy
     * </ul>
     */
public interface CustomTradingRecord  extends Serializable, TradingRecord {

    /**
     * @return the current trade
     */
    Trade getCurrentTrade();

    /**
     * Operates an order in the trading record.
     * @param index the index to operate the order
     */
    default void operate(int index) {
        operate(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an order in the trading record.
     * @param index the index to operate the order
     * @param price the price of the order
     * @param amount the amount to be ordered
     */
    void operate(int index, Decimal price, Decimal amount);

    /**
     * Operates an entry order in the trading record.
     * @param index the index to operate the entry
     * @return true if the entry has been operated, false otherwise
     */
    default boolean enter(int index) {
        return enter(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an entry order in the trading record.
     * @param index the index to operate the entry
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the entry has been operated, false otherwise
     */
    boolean enter(int index, Decimal price, Decimal amount);

    /**
     * Operates an exit order in the trading record.
     * @param index the index to operate the exit
     * @return true if the exit has been operated, false otherwise
     */
    default boolean exit(int index) {
        return exit(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an exit order in the trading record.
     * @param index the index to operate the exit
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the exit has been operated, false otherwise
     */
    boolean exit(int index, Decimal price, Decimal amount);

    /**
     * @return true if no trade is open, false otherwise
     */
    default boolean isClosed() {
        return !getCurrentTrade().isOpened();
    }

    /**
     * @return the recorded trades
     */
    List<Trade> getTrades();

    /**
     * @return the number of recorded trades
     */
    default int getTradeCount() {
        return getTrades().size();
    }

    /**
     * @return the last trade recorded
     */
    default Trade getLastTrade() {
        List<Trade> trades = getTrades();
        if (!trades.isEmpty()) {
            return trades.get(trades.size() - 1);
        }
        return null;
    }

    /**
     * @return the last order recorded
     */
    Order getLastOrder();

    /**
     * @param orderType the type of the order to get the last of
     * @return the last order (of the provided type) recorded
     */
    Order getLastOrder(Order.OrderType orderType);

    /**
     * @return the last entry order recorded
     */
    Order getLastEntry();

    /**
     * @return the last exit order recorded
     */
    Order getLastExit();
}

