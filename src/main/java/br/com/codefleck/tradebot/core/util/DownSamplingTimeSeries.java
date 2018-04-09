package br.com.codefleck.tradebot.core.util;

import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.ta4j.core.*;

public class DownSamplingTimeSeries {

    public TimeSeries aggregateTimeSeriesToWeek(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            while(series.getEndIndex()>i && (getWeek(series.getBar(i+1)) == getWeek(currentBar))){ // while next Bar is in current week
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolume = currentVolume.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime,currentOpen,currentMax,currentMin,
                currentClose,currentVolume));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }

    public TimeSeries aggregateTimeSeriesToDay(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            while(series.getEndIndex()>i && (getDay(series.getBar(i+1)) == getDay(currentBar))){ // while next Bar is in current day
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolume = currentVolume.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime,currentOpen,currentMax,currentMin,
                currentClose,currentVolume));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }

    public TimeSeries aggregateTimeSeriesToFourHours(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextFourHours = currentBar.getEndTime().plusHours(4).minusMinutes(1);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextFourHours))){
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolume = currentVolume.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime,currentOpen,currentMax,currentMin,
                currentClose,currentVolume));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }

    public TimeSeries aggregateTimeSeriesToThreeHours(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextFourHours = currentBar.getEndTime().plusHours(3).minusMinutes(1);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextFourHours))){
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolume = currentVolume.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime,currentOpen,currentMax,currentMin,
                currentClose,currentVolume));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }

    public TimeSeries aggregateTimeSeriesToTwoHours(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextFourHours = currentBar.getEndTime().plusHours(2).minusMinutes(1);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextFourHours))){
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolume = currentVolume.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime,currentOpen,currentMax,currentMin,
                currentClose,currentVolume));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }

    public TimeSeries aggregateTimeSeriesToHour(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextFourHours = currentBar.getEndTime().plusHours(1).minusMinutes(1);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextFourHours))){
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolume = currentVolume.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime,currentOpen,currentMax,currentMin,
                currentClose,currentVolume));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }


    private int getDay(Bar bar){
        return bar.getEndTime().get(IsoFields.DAY_OF_QUARTER);
    }

    private int getWeek(Bar bar){
        return bar.getEndTime().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }
}
