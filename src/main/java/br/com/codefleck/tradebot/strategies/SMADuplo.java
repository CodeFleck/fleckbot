package br.com.codefleck.tradebot.strategies;

import java.util.List;

import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MultiplierIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import br.com.codefleck.tradebot.core.util.SMA;

public class SMADuplo {

    public static Strategy buildStrategy(BaseTimeSeries series, List<SMA> smaList) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        // The bias is bullish when the shorter-moving average moves above the longer moving average.
        // The bias is bearish when the shorter-moving average moves below the longer moving average.
        SMAIndicator shortSma = new SMAIndicator(closePrice, 10);
        SMAIndicator longSma = new SMAIndicator(closePrice, 32);

        MultiplierIndicator shortMultiplierIndicator = new MultiplierIndicator(shortSma, Decimal.valueOf(0.98));
        MultiplierIndicator longMultiplierIndicator = new MultiplierIndicator(shortSma, Decimal.valueOf(1.02));


        // Entry rule
        Rule entryRule = new UnderIndicatorRule(shortSma, longSma) // Trend
            .and(new CrossedDownIndicatorRule(closePrice, shortMultiplierIndicator)); // Signal 1

        // Exit rule
        Rule exitRule = new OverIndicatorRule(shortSma, longSma) // Trend
            .and(new CrossedUpIndicatorRule(closePrice, longMultiplierIndicator)); // Signal 1

        return new BaseStrategy(entryRule, exitRule);
    }
}

