package br.com.codefleck.tradebot.core.util;

import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.ta4j.core.*;

public class DownSamplingTimeSeries {

    public TimeSeries aggregateTimeSeriesDtoW(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolumen = currentBar.getVolume();

            while(series.getEndIndex()>i && (getWeek(series.getBar(i+1)) == getWeek(currentBar))){ // while next Bar is in current week
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolumen = currentVolumen.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime,currentOpen,currentMax,currentMin,
                currentClose,currentVolumen));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }

    private int getWeek(Bar Bar){
        return Bar.getEndTime().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }
}
