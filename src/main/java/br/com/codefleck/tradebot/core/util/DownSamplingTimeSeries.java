package br.com.codefleck.tradebot.core.util;

import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.ta4j.core.*;

public class DownSamplingTimeSeries {

    private String period;

    public DownSamplingTimeSeries(String period){
        this.period = period;
    }

    public BaseTimeSeries aggregate(TimeSeries series){

        if (series != null){

            if (this.period.equals("1 minuto")){

                List<Bar> aggBars = new ArrayList<Bar>();

                for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++) {
                    Bar currentBar = series.getBar(i);
                    Decimal currentOpen = currentBar.getOpenPrice();
                    Decimal currentMax = currentBar.getMaxPrice();
                    Decimal currentMin = currentBar.getMinPrice();
                    Decimal currentVolume = currentBar.getVolume();
                    ZonedDateTime currentEndTime = currentBar.getEndTime();
                    Decimal currentClose = currentBar.getClosePrice();
                    aggBars.add(new BaseBar(currentEndTime, currentOpen, currentMax, currentMin, currentClose, currentVolume));
                }
                return new BaseTimeSeries("umMinuto", aggBars);
            }
            if (this.period.equals("5 minutos")) {
                return aggregateTimeSeriesToFiveMinutes(series);
            }
            if (this.period.equals("10 minutos")){
                return aggregateTimeSeriesToTenMinutes(series);
            }
            if (this.period.equals("15 minutos")){
                return aggregateTimeSeriesToFifteenMinutes(series);
            }
            if (this.period.equals("15 minutos")){
                return aggregateTimeSeriesToFifteenMinutes(series);
            }
            if (this.period.equals("30 minutos")){
                return aggregateTimeSeriesToThirtyMinutes(series);
            }
            if (this.period.equals("1 hora")){
                return aggregateTimeSeriesToHour(series);
            }
            if (this.period.equals("2 horas")){
                return aggregateTimeSeriesToTwoHours(series);
            }
            if (this.period.equals("3 horas")){
                return aggregateTimeSeriesToThreeHours(series);
            }
            if (this.period.equals("4 horas")){
                return aggregateTimeSeriesToFourHours(series);
            }
            if (this.period.equals("1 dia")){
                return aggregateTimeSeriesToDay(series);
            }
            if (this.period.equals("1 semana")){
                return aggregateTimeSeriesToWeek(series);
            }
            if (this.period.equals("1 mes")){
                return aggregateTimeSeriesToMonth(series);
            }
        }else {
            System.out.println("Series cannot be null");
        }
        return null;
    }


    public BaseTimeSeries aggregateTimeSeriesToMonth(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            while(series.getEndIndex()>i && (getMonth(series.getBar(i+1)) == getMonth(currentBar))){ // while next Bar is in current week
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

    public BaseTimeSeries aggregateTimeSeriesToWeek(TimeSeries series) {
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

    public BaseTimeSeries aggregateTimeSeriesToDay(TimeSeries series) {
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

    public BaseTimeSeries aggregateTimeSeriesToFourHours(TimeSeries series) {
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

    public BaseTimeSeries aggregateTimeSeriesToThreeHours(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextThreeHours = currentBar.getEndTime().plusHours(3).minusMinutes(1);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextThreeHours))){
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

    public BaseTimeSeries aggregateTimeSeriesToTwoHours(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextTwoHours = currentBar.getEndTime().plusHours(2).minusMinutes(1);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextTwoHours))){
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

    public BaseTimeSeries aggregateTimeSeriesToHour(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nexHour = currentBar.getEndTime().plusHours(1).minusMinutes(1);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nexHour))){
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

    public BaseTimeSeries aggregateTimeSeriesToThirtyMinutes(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextThirtyMinutes = currentBar.getEndTime().plusMinutes(29);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextThirtyMinutes))){
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

    public BaseTimeSeries aggregateTimeSeriesToFifteenMinutes(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextThirtyMinutes = currentBar.getEndTime().plusMinutes(14);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextThirtyMinutes))){
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

    public BaseTimeSeries aggregateTimeSeriesToTenMinutes(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for(int i=series.getBeginIndex(); i<=series.getEndIndex(); i++){
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextThirtyMinutes = currentBar.getEndTime().plusMinutes(9);

            while(series.getEndIndex()>i && (series.getBar(i).getEndTime().isBefore(nextThirtyMinutes))){
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

    public BaseTimeSeries aggregateTimeSeriesToFiveMinutes(TimeSeries series) {
        Objects.requireNonNull(series);
        List<Bar> aggBars = new ArrayList<Bar>();
        for (int i = series.getBeginIndex(); i <= series.getEndIndex(); i++) {
            Bar currentBar = series.getBar(i);
            Decimal currentOpen = currentBar.getOpenPrice();
            Decimal currentMax = currentBar.getMaxPrice();
            Decimal currentMin = currentBar.getMinPrice();
            Decimal currentVolume = currentBar.getVolume();

            ZonedDateTime nextThirtyMinutes = currentBar.getEndTime().plusMinutes(4);

            while (series.getEndIndex() > i && (series.getBar(i).getEndTime().isBefore(nextThirtyMinutes))) {
                i++;
                currentBar = series.getBar(i); // nextBar
                currentMax = currentMax.max(currentBar.getMaxPrice());
                currentMin = currentMin.min(currentBar.getMinPrice());
                currentVolume = currentVolume.plus(currentBar.getVolume());
            }
            ZonedDateTime currentEndTime = currentBar.getEndTime();
            Decimal currentClose = currentBar.getClosePrice();
            aggBars.add(new BaseBar(currentEndTime, currentOpen, currentMax, currentMin, currentClose, currentVolume));
        }
        return new BaseTimeSeries(series.getName(), aggBars);
    }

    private int getDay(Bar bar){
        return bar.getEndTime().get(IsoFields.DAY_OF_QUARTER);
    }

    private int getWeek(Bar bar){
        return bar.getEndTime().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private int getMonth(Bar bar){
        return bar.getEndTime().getMonthValue();
    }

    public List<CustomBaseBar> customAggregate(List<CustomBaseBar> customBaseBars) {

        List<CustomBaseBar> aggregatedCustomBaseBars = new ArrayList<CustomBaseBar>();
        int i = 0;
        for( i = i; i < customBaseBars.size(); i++){

            Double currentOpen = customBaseBars.get(i).getOpen();
            Double currentMax =customBaseBars.get(i).getHigh();
            Double currentMin = customBaseBars.get(i).getLow();
            Double currentVolume = customBaseBars.get(i).getVolume();

            while(i < customBaseBars.size()-1 && (customBaseBars.get(i+1).getDate()).equals(customBaseBars.get(i).getDate())){
                i++;
                if (currentMax <= customBaseBars.get(i).getHigh()){
                    currentMax = customBaseBars.get(i).getHigh();
                }
                if (currentMin >= customBaseBars.get(i).getLow()){
                    currentMin = customBaseBars.get(i).getLow();
                }
                currentVolume += customBaseBars.get(i).getVolume();
            }
            String currentEndTime = customBaseBars.get(i).getDate();
            Double currentClose = customBaseBars.get(i).getClose();
            aggregatedCustomBaseBars.add(new CustomBaseBar(currentEndTime, "BTC",currentOpen,currentMax,currentMin,
                currentClose,currentVolume));
        }

        return aggregatedCustomBaseBars;
    }

}
