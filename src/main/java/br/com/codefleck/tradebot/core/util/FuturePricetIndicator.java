package br.com.codefleck.tradebot.core.util;

import java.util.List;

import org.ta4j.core.Decimal;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.CachedIndicator;

/**
 * Amount indicator.
 * <p></p>
 */
public class FuturePricetIndicator extends CachedIndicator<Decimal> {

    private TimeSeries series;
    private List<SMA> smaList;

    public FuturePricetIndicator(TimeSeries series, List<SMA> smaList) {
        super(series);
        this.series = series;
        this.smaList = smaList;
    }

    @Override
    protected Decimal calculate(int index) {

        Double futurePriceAmount = 0.0;
        String seriesDate = series.getBar(index).getSimpleDateName().substring(0,10);
        Integer smaListIndex = 0;

        for (int i=0; i<smaList.size();i++){

            String smaListDate = smaList.get(i).getData().substring(0,10);

            if (smaListDate.equals(seriesDate)){
                smaListIndex = i+1;
            }
        }



        if (smaList.get(smaListIndex) != null && smaList.get(smaListIndex).getValor() != null ){
            futurePriceAmount = smaList.get(smaListIndex).getValor();
        } else {
            System.out.println("No future value to predict: 0.00" );
        }

        return Decimal.valueOf(futurePriceAmount);
    }
}