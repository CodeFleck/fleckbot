package br.com.codefleck.tradebot.core.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.MedianPriceIndicator;

public class SMAUtil {

    public SMAUtil() {
    }

    public List<SMA> getSMAForMedianAveragePrice(BaseTimeSeries series) {

        System.out.println("Number of BaseTimeSeries bars: " + series.getEndIndex() + " or " + series.getBarCount());

        MedianPriceIndicator medianPriceIndicator = new MedianPriceIndicator(series);

        SMAIndicator mediaPriceEma = new SMAIndicator(medianPriceIndicator, 5);

        List<SMA> mediumPrice_SMAResulList = new ArrayList<>();
        for (int i = 0; i < series.getEndIndex(); i++) {
            Double result = Double.valueOf(String.valueOf(mediaPriceEma.getValue(i)));
            SMA sma = new SMA(result, "dia", 5, series.getBar(i).getSimpleDateName());
            System.out.println(sma.toString());
            mediumPrice_SMAResulList.add(sma);

        }
        return mediumPrice_SMAResulList;
    }
}
