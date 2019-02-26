package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.iterators.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iteratorService")
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class IteratorServiceImpl {

    public OneMinuteStockDataSetIterator getOneMinuteStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        OneMinuteStockDataSetIterator oneMinuteStockDataSetIterator = OneMinuteStockDataSetIterator.getInstance();
        List<StockData> oneMinuteStockDataList = stockDataList;
        oneMinuteStockDataSetIterator.setMiniBatchSize(batchSize);
        oneMinuteStockDataSetIterator.setExampleLength(180);
        oneMinuteStockDataSetIterator.setCategory(category);
        oneMinuteStockDataSetIterator.setSplit((int) Math.round(oneMinuteStockDataList.size() * splitRatio));
        oneMinuteStockDataSetIterator.setTrain(oneMinuteStockDataList.subList(0, oneMinuteStockDataSetIterator.getSplit()));
        oneMinuteStockDataSetIterator.setTest(oneMinuteStockDataSetIterator.generateTestDataSet(oneMinuteStockDataList.subList(oneMinuteStockDataSetIterator.getSplit(), oneMinuteStockDataList.size())));
        oneMinuteStockDataSetIterator.initializeOffsets();
        return oneMinuteStockDataSetIterator;
    }

    public FiveMinutesStockDataSetIterator getFiveMinutesStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        FiveMinutesStockDataSetIterator fiveMinutesStockDataSetIterator = FiveMinutesStockDataSetIterator.getInstance();
        List<StockData> fiveMinutesStockDataList = stockDataList;
        fiveMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        fiveMinutesStockDataSetIterator.setExampleLength(80);
        fiveMinutesStockDataSetIterator.setCategory(category);
        fiveMinutesStockDataSetIterator.setSplit((int) Math.round(fiveMinutesStockDataList.size() * splitRatio));
        fiveMinutesStockDataSetIterator.setTrain(fiveMinutesStockDataList.subList(0, fiveMinutesStockDataSetIterator.getSplit()));
        fiveMinutesStockDataSetIterator.setTest(fiveMinutesStockDataSetIterator.generateTestDataSet(fiveMinutesStockDataList.subList(fiveMinutesStockDataSetIterator.getSplit(), fiveMinutesStockDataList.size())));
        fiveMinutesStockDataSetIterator.initializeOffsets();
        return fiveMinutesStockDataSetIterator;
    }

    public TenMinutesStockDataSetIterator getTenMinutesStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        TenMinutesStockDataSetIterator tenMinutesStockDataSetIterator = TenMinutesStockDataSetIterator.getInstance();
        List<StockData> tenMinutesStockDataList = stockDataList;
        tenMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        tenMinutesStockDataSetIterator.setExampleLength(80);
        tenMinutesStockDataSetIterator.setCategory(category);
        tenMinutesStockDataSetIterator.setSplit((int) Math.round(tenMinutesStockDataList.size() * splitRatio));
        tenMinutesStockDataSetIterator.setTrain(tenMinutesStockDataList.subList(0, tenMinutesStockDataSetIterator.getSplit()));
        tenMinutesStockDataSetIterator.setTest(tenMinutesStockDataSetIterator.generateTestDataSet(tenMinutesStockDataList.subList(tenMinutesStockDataSetIterator.getSplit(), tenMinutesStockDataList.size())));
        tenMinutesStockDataSetIterator.initializeOffsets();
        return tenMinutesStockDataSetIterator;
    }

    public FifteenMinutesStockDataSetIterator getFifteenMinutesStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        FifteenMinutesStockDataSetIterator fifteenMinutesStockDataSetIterator = FifteenMinutesStockDataSetIterator.getInstance();
        List<StockData> fifteenMinutesStockDataList = stockDataList;
        fifteenMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        fifteenMinutesStockDataSetIterator.setExampleLength(180);
        fifteenMinutesStockDataSetIterator.setCategory(category);
        fifteenMinutesStockDataSetIterator.setSplit((int) Math.round(fifteenMinutesStockDataList.size() * splitRatio));
        fifteenMinutesStockDataSetIterator.setTrain(fifteenMinutesStockDataList.subList(0, fifteenMinutesStockDataSetIterator.getSplit()));
        fifteenMinutesStockDataSetIterator.setTest(fifteenMinutesStockDataSetIterator.generateTestDataSet(fifteenMinutesStockDataList.subList(fifteenMinutesStockDataSetIterator.getSplit(), fifteenMinutesStockDataList.size())));
        fifteenMinutesStockDataSetIterator.initializeOffsets();
        return fifteenMinutesStockDataSetIterator;
    }

    public ThirtyMinutesStockDataSetIterator getThirtyMinutesStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        ThirtyMinutesStockDataSetIterator thirtyMinutesIterator = ThirtyMinutesStockDataSetIterator.getInstance();
        List<StockData> thirtyMinutesStockDataList = stockDataList;
        thirtyMinutesIterator.setMiniBatchSize(batchSize);
        thirtyMinutesIterator.setExampleLength(280);
        thirtyMinutesIterator.setCategory(category);
        thirtyMinutesIterator.setSplit((int) Math.round(thirtyMinutesStockDataList.size() * splitRatio));
        thirtyMinutesIterator.setTrain(thirtyMinutesStockDataList.subList(0, thirtyMinutesIterator.getSplit()));
        thirtyMinutesIterator.setTest(thirtyMinutesIterator.generateTestDataSet(thirtyMinutesStockDataList.subList(thirtyMinutesIterator.getSplit(), thirtyMinutesStockDataList.size())));
        thirtyMinutesIterator.initializeOffsets();
        return thirtyMinutesIterator;
    }

    public OneHourStockDataSetIterator getOneHourStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        OneHourStockDataSetIterator oneHourIterator = OneHourStockDataSetIterator.getInstance();
        List<StockData> oneHourStockDataList = stockDataList;
        oneHourIterator.setMiniBatchSize(batchSize);
        oneHourIterator.setExampleLength(50);
        oneHourIterator.setCategory(category);
        oneHourIterator.setSplit((int) Math.round(oneHourStockDataList.size() * splitRatio));
        oneHourIterator.setTrain(oneHourStockDataList.subList(0, oneHourIterator.getSplit()));
        oneHourIterator.setTest(oneHourIterator.generateTestDataSet(oneHourStockDataList.subList(oneHourIterator.getSplit(), oneHourStockDataList.size())));
        oneHourIterator.initializeOffsets();
        return oneHourIterator;
    }

    public TwoHoursStockDataSetIterator getTwoHoursStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        TwoHoursStockDataSetIterator twoHoursIterator = TwoHoursStockDataSetIterator.getInstance();
        List<StockData> twoHoursStockDataList = stockDataList;
        twoHoursIterator.setMiniBatchSize(batchSize);
        twoHoursIterator.setExampleLength(80);
        twoHoursIterator.setCategory(category);
        twoHoursIterator.setSplit((int) Math.round(twoHoursStockDataList.size() * splitRatio));
        twoHoursIterator.setTrain(twoHoursStockDataList.subList(0, twoHoursIterator.getSplit()));
        twoHoursIterator.setTest(twoHoursIterator.generateTestDataSet(twoHoursStockDataList.subList(twoHoursIterator.getSplit(), twoHoursStockDataList.size())));
        twoHoursIterator.initializeOffsets();
        return twoHoursIterator;
    }

    public ThreeHoursStockDataSetIterator getThreeHoursStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        ThreeHoursStockDataSetIterator threeHoursStockDataSetIterator = ThreeHoursStockDataSetIterator.getInstance();
        List<StockData> threeHoursStockDataList = stockDataList;
        threeHoursStockDataSetIterator.setMiniBatchSize(batchSize);
        threeHoursStockDataSetIterator.setExampleLength(50);
        threeHoursStockDataSetIterator.setCategory(category);
        threeHoursStockDataSetIterator.setSplit((int) Math.round(threeHoursStockDataList.size() * splitRatio));
        threeHoursStockDataSetIterator.setTrain(threeHoursStockDataList.subList(0, threeHoursStockDataSetIterator.getSplit()));
        threeHoursStockDataSetIterator.setTest(threeHoursStockDataSetIterator.generateTestDataSet(threeHoursStockDataList.subList(threeHoursStockDataSetIterator.getSplit(), threeHoursStockDataList.size())));
        threeHoursStockDataSetIterator.initializeOffsets();
        return threeHoursStockDataSetIterator;
    }

    public FourHoursStockDataSetIterator getFourHoursStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        FourHoursStockDataSetIterator fourHoursStockDataSetIterator = FourHoursStockDataSetIterator.getInstance();
        List<StockData> fourHoursStockDataList = stockDataList;
        fourHoursStockDataSetIterator.setMiniBatchSize(batchSize);
        fourHoursStockDataSetIterator.setExampleLength(40);
        fourHoursStockDataSetIterator.setCategory(category);
        fourHoursStockDataSetIterator.setSplit((int) Math.round(fourHoursStockDataList.size() * splitRatio));
        fourHoursStockDataSetIterator.setTrain(fourHoursStockDataList.subList(0, fourHoursStockDataSetIterator.getSplit()));
        fourHoursStockDataSetIterator.setTest(fourHoursStockDataSetIterator.generateTestDataSet(fourHoursStockDataList.subList(fourHoursStockDataSetIterator.getSplit(), fourHoursStockDataList.size())));
        fourHoursStockDataSetIterator.initializeOffsets();
        return fourHoursStockDataSetIterator;
    }

    public OneDayStockDataSetIterator getOneDayStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        OneDayStockDataSetIterator oneDayIterator = OneDayStockDataSetIterator.getInstance();
        List<StockData> oneDayStockDataList = stockDataList;
        oneDayIterator.setMiniBatchSize(batchSize);
        oneDayIterator.setExampleLength(40);
        oneDayIterator.setCategory(category);
        oneDayIterator.setSplit((int) Math.round(oneDayStockDataList.size() * splitRatio));
        oneDayIterator.setTrain(oneDayStockDataList.subList(0, oneDayIterator.getSplit()));
        oneDayIterator.setTest(oneDayIterator.generateTestDataSet(oneDayStockDataList.subList(oneDayIterator.getSplit(), oneDayStockDataList.size())));
        oneDayIterator.initializeOffsets();
        return oneDayIterator;
    }

    public OneWeekStockDataSetIterator getOneWeekStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        OneWeekStockDataSetIterator oneWeekIterator = OneWeekStockDataSetIterator.getInstance();
        List<StockData> oneWeekStockDataList = stockDataList;
        oneWeekIterator.setMiniBatchSize(batchSize);
        oneWeekIterator.setExampleLength(30);
        oneWeekIterator.setCategory(category);
        oneWeekIterator.setSplit((int) Math.round(oneWeekStockDataList.size() * splitRatio));
        oneWeekIterator.setTrain(oneWeekStockDataList.subList(0, oneWeekIterator.getSplit()));
        oneWeekIterator.setTest(oneWeekIterator.generateTestDataSet(oneWeekStockDataList.subList(oneWeekIterator.getSplit(), oneWeekStockDataList.size())));
        oneWeekIterator.initializeOffsets();
        return oneWeekIterator;
    }

    public OneMonthStockDataSetIterator getOneMonthStockDataSetIterator(List<StockData> stockDataList, int batchSize, double splitRatio, PriceCategory category) {
        OneMonthStockDataSetIterator oneMonthIterator = OneMonthStockDataSetIterator.getInstance();
        List<StockData> oneMonthstockDataList = duplicate(stockDataList);
        oneMonthIterator.setMiniBatchSize(8);
        oneMonthIterator.setExampleLength(30);
        oneMonthIterator.setCategory(category);
        oneMonthIterator.setTrain(oneMonthstockDataList);
        oneMonthIterator.setTest(oneMonthIterator.generateTestDataSet(oneMonthstockDataList));
        oneMonthIterator.initializeOffsets();
        return oneMonthIterator;
    }

    private List<StockData> duplicate(List<StockData> stockDataList) {
        List<StockData> duplicatedStockDataList = new ArrayList<>();
        for (StockData stock : stockDataList) {
            duplicatedStockDataList.add(stock);
        }
        for (StockData stock : stockDataList) {
            duplicatedStockDataList.add(stock);
        }
        for (StockData stock : stockDataList) {
            duplicatedStockDataList.add(stock);
        }
        for (StockData stock : stockDataList) {
            duplicatedStockDataList.add(stock);
        }
        return duplicatedStockDataList;
    }

}
