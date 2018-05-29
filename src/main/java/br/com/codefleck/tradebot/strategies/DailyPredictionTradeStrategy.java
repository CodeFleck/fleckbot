package br.com.codefleck.tradebot.strategies;

import java.util.List;

import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MedianPriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import br.com.codefleck.tradebot.core.util.FuturePricetIndicator;
import br.com.codefleck.tradebot.core.util.SMA;

public class DailyPredictionTradeStrategy {

    public static Strategy buildStrategy(BaseTimeSeries series, List<SMA> smaList) {

        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        FuturePricetIndicator futurePricetIndicator = new FuturePricetIndicator(series, smaList);
        MedianPriceIndicator medianPriceIndicator = new MedianPriceIndicator(series);

        Rule entryRule = new UnderIndicatorRule(medianPriceIndicator, futurePricetIndicator);

        Rule exitRule = new OverIndicatorRule(medianPriceIndicator, futurePricetIndicator);

        return new BaseStrategy(entryRule, exitRule);
    }
}

