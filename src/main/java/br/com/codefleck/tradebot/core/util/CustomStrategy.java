package br.com.codefleck.tradebot.core.util;

import org.ta4j.core.Rule;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;

/**
 * A trading strategy.
 * <p></p>
 * A strategy is a pair of complementary {@link Rule rules}. It may recommend to enter or to exit.
 * Recommendations are based respectively on the entry rule or on the exit rule.
 */
public interface CustomStrategy {

    /**
     * @return the name of the strategy
     */
    String getName();

    /**
     * @return the entry rule
     */
    Rule getEntryRule();

    /**
     * @return the exit rule
     */
    Rule getExitRule();

    /**
     * @param strategy the other strategy
     * @return the AND combination of two {@link org.ta4j.core.Strategy strategies}
     */
    org.ta4j.core.Strategy and(org.ta4j.core.Strategy strategy);

    /**
     * @param strategy the other strategy
     * @return the OR combination of two {@link org.ta4j.core.Strategy strategies}
     */
    org.ta4j.core.Strategy or(org.ta4j.core.Strategy strategy);

    /**
     * @param name the name of the strategy
     * @param strategy the other strategy
     * @param unstablePeriod number of bars that will be strip off for this strategy
     * @return the AND combination of two {@link org.ta4j.core.Strategy strategies}
     */
    org.ta4j.core.Strategy and(String name, org.ta4j.core.Strategy strategy, int unstablePeriod);

    /**
     * @param name the name of the strategy
     * @param strategy the other strategy
     * @param unstablePeriod number of bars that will be strip off for this strategy
     * @return the OR combination of two {@link org.ta4j.core.Strategy strategies}
     */
    org.ta4j.core.Strategy or(String name, org.ta4j.core.Strategy strategy, int unstablePeriod);

    /**
     * @return the opposite of the {@link org.ta4j.core.Strategy strategy}
     */
    org.ta4j.core.Strategy opposite();

    /**
     * @param unstablePeriod number of bars that will be strip off for this strategy
     */
    void setUnstablePeriod(int unstablePeriod);

    /**
     * @return unstablePeriod number of bars that will be strip off for this strategy
     */
    int getUnstablePeriod();

    /**
     * @param index a bar index
     * @return true if this strategy is unstable at the provided index, false otherwise (stable)
     */
    boolean isUnstableAt(int index);

    /**
     * @param index the bar index
     * @param tradingRecord the potentially needed trading history
     * @return true to recommend an order, false otherwise (no recommendation)
     */
    default boolean shouldOperate(int index, TradingRecord tradingRecord) {
        Trade trade = tradingRecord.getCurrentTrade();
        if (trade.isNew()) {
            return shouldEnter(index, tradingRecord);
        } else if (trade.isOpened()) {
            return shouldExit(index, tradingRecord);
        }
        return false;
    }

    /**
     * @param index the bar index
     * @return true to recommend to enter, false otherwise
     */
    default boolean shouldEnter(int index) {
        return shouldEnter(index, null);
    }

    /**
     * @param index the bar index
     * @param tradingRecord the potentially needed trading history
     * @return true to recommend to enter, false otherwise
     */
    default boolean shouldEnter(int index, TradingRecord tradingRecord) {
        if (isUnstableAt(index)) {
            return false;
        }
        return getEntryRule().isSatisfied(index, tradingRecord);
    }

    /**
     * @param index the bar index
     * @return true to recommend to exit, false otherwise
     */
    default boolean shouldExit(int index) {
        return shouldExit(index, null);
    }

    /**
     * @param index the bar index
     * @param tradingRecord the potentially needed trading history
     * @return true to recommend to exit, false otherwise
     */
    default boolean shouldExit(int index, TradingRecord tradingRecord) {
        if (isUnstableAt(index)) {
            return false;
        }
        return getExitRule().isSatisfied(index, tradingRecord);
    }
}

