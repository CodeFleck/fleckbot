package br.com.codefleck.tradebot.core.util;

import br.com.codefleck.tradebot.models.StockData;
import org.hibernate.mapping.Collection;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Converter {

    public Converter() {
    }

    public TimeSeries transformStockDataIntoTimeSeries(List<StockData> stockDataList){

        TimeSeries series = new BaseTimeSeries();
        Collections.reverse(stockDataList);

        for (StockData stockData : stockDataList) {
            Bar bar = new BaseBar(
                    stockData.getDate(),
                    stockData.getOpen(),
                    stockData.getHigh(),
                    stockData.getLow(),
                    stockData.getClose(),
                    stockData.getVolume()
            );
            series.addBar(bar);
        }
        return series;
    }

    public List<StockData> transformTimeSeriesIntoStockData(BaseTimeSeries timeSeries) {

        List<StockData> stockDataList = new ArrayList<>();

        for (int i=0; i<timeSeries.getEndIndex();i++) {

            StockData stockData = new StockData(
                    timeSeries.getBar(i).getEndTime(),
                    "BTC",
                    timeSeries.getBar(i).getOpenPrice().doubleValue(),
                    timeSeries.getBar(i).getClosePrice().doubleValue(),
                    timeSeries.getBar(i).getMinPrice().doubleValue(),
                    timeSeries.getBar(i).getMaxPrice().doubleValue(),
                    timeSeries.getBar(i).getVolume().doubleValue()
            );
            stockDataList.add(stockData);
        }
        return stockDataList;
    }
}

