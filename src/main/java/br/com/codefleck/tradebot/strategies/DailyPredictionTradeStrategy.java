package br.com.codefleck.tradebot.strategies;

import br.com.codefleck.tradebot.core.util.FuturePricetIndicator;
import br.com.codefleck.tradebot.core.util.SMA;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.helpers.MedianPriceIndicator;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import java.util.List;

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

