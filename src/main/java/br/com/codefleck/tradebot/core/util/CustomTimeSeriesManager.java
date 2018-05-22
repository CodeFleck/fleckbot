package br.com.codefleck.tradebot.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;

public class CustomTimeSeriesManager {

    /** The logger */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /** The managed time series */
    private TimeSeries timeSeries;

    /**
     * Constructor.
     */
    public CustomTimeSeriesManager() {
    }

    /**
     * Constructor.
     * @param timeSeries the time series to be managed
     */
    public CustomTimeSeriesManager(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    /**
     * @param timeSeries the time series to be managed
     */
    public void setTimeSeries(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
    }

    /**
     * @return the managed time series
     */
    public TimeSeries getTimeSeries() {
        return timeSeries;
    }

    /**
     * Runs the provided strategy over the managed series.
     * <p>
     * Opens the trades with {@link Order.OrderType} BUY @return the trading record coming from the run
     */
    public TradingRecord run(Strategy strategy) {
        return run(strategy, Order.OrderType.BUY);
    }

    /**
     * Runs the provided strategy over the managed series (from startIndex to finishIndex).
     * <p>
     * Opens the trades with {@link Order.OrderType} BUY orders.
     * @param strategy the trading strategy
     * @param startIndex the start index for the run (included)
     * @param finishIndex the finish index for the run (included)
     * @return the trading record coming from the run
     */
    public TradingRecord run(Strategy strategy, int startIndex, int finishIndex) {
        return run(strategy, Order.OrderType.BUY, Decimal.NaN, startIndex, finishIndex);
    }

    /**
     * Runs the provided strategy over the managed series.
     * <p>
     * Opens the trades with {@link Order.OrderType} BUY orders.
     * @param strategy the trading strategy
     * @param orderType the {@link Order.OrderType} used to open the trades
     * @return the trading record coming from the run
     */
    public TradingRecord run(Strategy strategy, Order.OrderType orderType) {
        return run(strategy, orderType, Decimal.NaN);
    }

    /**
     * Runs the provided strategy over the managed series (from startIndex to finishIndex).
     * <p>
     * Opens the trades with {@link Order.OrderType} BUYorders.
     * @param strategy the trading strategy
     * @param orderType the {@link Order.OrderType} used to open the trades
     * @param startIndex the start index for the run (included)
     * @param finishIndex the finish index for the run (included)
     * @return the trading record coming from the run
     */
    public TradingRecord run(Strategy strategy, Order.OrderType orderType, int startIndex, int finishIndex) {
        return run(strategy, orderType, Decimal.NaN, startIndex, finishIndex);
    }

    /**
     * Runs the provided strategy over the managed series.
     * <p>
     * @param strategy the trading strategy
     * @param orderType the {@link Order.OrderType} used to open the trades
     * @param amount the amount used to open/close the trades
     * @return the trading record coming from the run
     */
    public TradingRecord run(Strategy strategy, Order.OrderType orderType, Decimal amount) {
        return run(strategy, orderType, amount, timeSeries.getBeginIndex(), timeSeries.getEndIndex());
    }

    /**
     * Runs the provided strategy over the managed series (from startIndex to finishIndex).
     * <p>
     * @param strategy the trading strategy
     * @param orderType the {@link Order.OrderType} used to open the trades
     * @param amount the amount used to open/close the trades
     * @param startIndex the start index for the run (included)
     * @param finishIndex the finish index for the run (included)
     * @return the trading record coming from the run
     */
    public TradingRecord run(Strategy strategy, Order.OrderType orderType, Decimal amount, int startIndex, int finishIndex) {

        int runBeginIndex = Math.max(startIndex, timeSeries.getBeginIndex());
        int runEndIndex = Math.min(finishIndex, timeSeries.getEndIndex());

        log.trace("Running strategy (indexes: {} -> {}): {} (starting with {})", runBeginIndex, runEndIndex, strategy, orderType);
        CustomTradingRecord tradingRecord = new CustomBaseTradingRecord(orderType);
        for (int i = runBeginIndex; i <= runEndIndex; i++) {
            // For each bar between both indexes...
            if (strategy.shouldOperate(i, tradingRecord)) {
                tradingRecord.operate(i, timeSeries.getBar(i).getClosePrice(), amount);
            }
        }

        if (!tradingRecord.isClosed()) {
            // If the last trade is still opened, we search out of the run end index.
            // May works if the end index for this run was inferior to the actual number of bars
            int seriesMaxSize = Math.max(timeSeries.getEndIndex() + 1, timeSeries.getBarData().size());
            for (int i = runEndIndex + 1; i < seriesMaxSize; i++) {
                // For each bar after the end index of this run...
                // --> Trying to close the last trade
                if (strategy.shouldOperate(i, tradingRecord)) {
                    tradingRecord.operate(i, timeSeries.getBar(i).getClosePrice(), amount);
                    break;
                }
            }
        }
        return tradingRecord;
    }

}
