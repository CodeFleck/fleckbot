package br.com.codefleck.tradebot.strategies;

import org.ta4j.core.*;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MultiplierIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

public class MyStrategy {

    /**
     * @param series a time series
     * @return a moving momentum strategy
     */
    public static Strategy buildStrategy(BaseTimeSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        // The bias is bullish when the shorter-moving average moves above the longer moving average.
        // The bias is bearish when the shorter-moving average moves below the longer moving average.
        EMAIndicator shortEma = new EMAIndicator(closePrice, 5);
        SMAIndicator longEma = new SMAIndicator(closePrice, 32);

        MultiplierIndicator shortMultiplierIndicator = new MultiplierIndicator(shortEma, Decimal.valueOf(0.99));
        MultiplierIndicator longMultiplierIndicator = new MultiplierIndicator(shortEma, Decimal.valueOf(1.01));

        // Entry rule
        Rule entryRule = new UnderIndicatorRule(shortEma, longEma) // Trend
            .and(new CrossedDownIndicatorRule(closePrice, shortMultiplierIndicator)); // Signal 1

        // Exit rule
        Rule exitRule = new OverIndicatorRule(shortEma, longEma) // Trend
            .and(new CrossedUpIndicatorRule(closePrice, longMultiplierIndicator)); // Signal 1

        return new BaseStrategy(entryRule, exitRule);
    }
}
