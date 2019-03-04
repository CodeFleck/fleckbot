package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.iterators.*;
import javafx.util.Pair;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.BaseTimeSeries;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Daniel Fleck
 */
@Service("forecastService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot"})
public class ForecastServiceImpl {

    @Autowired
    PredictionServiceImpl predictionService;

    final Logger log = LoggerFactory.getLogger(ForecastServiceImpl.class);

    public HashMap<String, Double> initializeForecasts(List<StockData> lastStockData, PriceCategory category, BaseTimeSeries customBaseTimeSeriesMock) throws IOException {

        PriceCategory priceCategory = category;
        List<StockData> latestStockDataList = lastStockData;    //stock data initialization;
        String nomeDoConjunto = "forecast";

        DataPointsListResultSet oneMinuteResults = forecastOneMinute(latestStockDataList, category);
        DataPointsListResultSet fifteenMinuteResults = forecastFifteenMinutes(latestStockDataList, category);
        DataPointsListResultSet thirtyMinuteResults = forecastThirtyMinutes(latestStockDataList, category);
        DataPointsListResultSet oneHourResults = forecastOneHour(latestStockDataList, category);
        DataPointsListResultSet twoHoursResults = forecastTwoHours(latestStockDataList, category);
        DataPointsListResultSet fourHoursResults = forecastFourHours(latestStockDataList, category);
        DataPointsListResultSet oneDayResults = forecastOneDay(latestStockDataList, category);
        DataPointsListResultSet oneWeekResults = forecastOneWeek(latestStockDataList, category);
        DataPointsListResultSet oneMonthResults = forecastOneMonth(latestStockDataList, category);

        Collections.reverse(oneMinuteResults.getPredictDataPointsList());
        Collections.reverse(fifteenMinuteResults.getPredictDataPointsList());
        Collections.reverse(thirtyMinuteResults.getPredictDataPointsList());
        Collections.reverse(oneHourResults.getPredictDataPointsList());
        Collections.reverse(twoHoursResults.getPredictDataPointsList());
        Collections.reverse(fourHoursResults.getPredictDataPointsList());
        Collections.reverse(oneDayResults.getPredictDataPointsList());
        Collections.reverse(oneWeekResults.getPredictDataPointsList());
        Collections.reverse(oneMonthResults.getPredictDataPointsList());

        NumberFormat formatter = new DecimalFormat("#0.00");

        HashMap<String, Double> predictions = new HashMap<>();

        predictions.put("oneMinute", Double.valueOf(formatter.format(oneMinuteResults.getPredictDataPointsList().get(0).getY())));
        log.info("One minute forecast: " + oneMinuteResults.getPredictDataPointsList().get(0).toString());

        predictions.put("fifteenMinutes", Double.valueOf(formatter.format(fifteenMinuteResults.getPredictDataPointsList().get(0).getY())));
        log.info("Fifteen minutes forecast: " + fifteenMinuteResults.getPredictDataPointsList().get(0).toString());

        predictions.put("thirtyMinutes",  Double.valueOf(formatter.format(thirtyMinuteResults.getPredictDataPointsList().get(0).getY())));
        log.info("Thirty minutes forecast: " + thirtyMinuteResults.getPredictDataPointsList().get(0).toString());

        predictions.put("oneHour", Double.valueOf(formatter.format(oneHourResults.getPredictDataPointsList().get(0).getY())));
        log.info("One hour forecast: " + oneHourResults.getPredictDataPointsList().get(0).toString());

        predictions.put("twoHours", Double.valueOf(formatter.format(twoHoursResults.getPredictDataPointsList().get(0).getY())));
        log.info("Two hour forecast: " + twoHoursResults.getPredictDataPointsList().get(0).toString());

        predictions.put("fourHours", Double.valueOf(formatter.format(fourHoursResults.getPredictDataPointsList().get(0).getY())));
        log.info("Four hours forecast: " + fourHoursResults.getPredictDataPointsList().get(0).toString());

        predictions.put("twentyFourHours", Double.valueOf(formatter.format(oneDayResults.getPredictDataPointsList().get(0).getY())));
        log.info("One Day forecast: " + oneDayResults.getPredictDataPointsList().get(0).toString());

        predictions.put("oneWeek", Double.valueOf(formatter.format(oneWeekResults.getPredictDataPointsList().get(0).getY())));
        log.info("One Week forecast: " + oneWeekResults.getPredictDataPointsList().get(0).toString());

        predictions.put("oneMonth", Double.valueOf(formatter.format(oneMonthResults.getPredictDataPointsList().get(0).getY())));
        log.info("One Week forecast: " + oneMonthResults.getPredictDataPointsList().get(0).toString());

        return predictions;
    }

    private DataPointsListResultSet forecastOneMinute(List<StockData> init, PriceCategory category) throws IOException {
        String period = "1 minuto";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork oneMinutenet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        OneMinuteStockDataSetIterator oneMinuteIterator = OneMinuteStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = oneMinuteIterator.generateTestDataSet(init);
        oneMinuteIterator.setTest(pairList);
        oneMinutenet.fit(oneMinuteIterator.next()) ;
        oneMinuteIterator.reset();
        oneMinutenet.rnnClearPreviousState();
        log.info("Forecasting one minute...");
        double max = oneMinuteIterator.getMaxNum(category);
        double min = oneMinuteIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneMinutenet, oneMinuteIterator.getTest(), max, min, oneMinuteIterator.getExampleLength());
        log.info("Done forecasting one minute");

        return resultSet;
    }

    private DataPointsListResultSet forecastFifteenMinutes(List<StockData> init, PriceCategory category) throws IOException {
        String period = "15 minutos";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork fifteenMinutesNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        FifteenMinutesStockDataSetIterator fifteenMinutesIterator = FifteenMinutesStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = fifteenMinutesIterator.generateTestDataSet(init);
        fifteenMinutesIterator.setTest(pairList);
        fifteenMinutesNet.fit(fifteenMinutesIterator.next()) ;
        fifteenMinutesIterator.reset();
        fifteenMinutesNet.rnnClearPreviousState();
        log.info("Forecasting fifteen minutes...");
        double max = fifteenMinutesIterator.getMaxNum(category);
        double min = fifteenMinutesIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(fifteenMinutesNet, fifteenMinutesIterator.getTest(), max, min, fifteenMinutesIterator.getExampleLength());
        log.info("Done forecasting one minute");

        return resultSet;
    }

    private DataPointsListResultSet forecastThirtyMinutes(List<StockData> init, PriceCategory category) throws IOException {
        String period = "30 minutos";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork thirtyMinutesNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        ThirtyMinutesStockDataSetIterator thirtyMinutesIterator = ThirtyMinutesStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = thirtyMinutesIterator.generateTestDataSet(init);
        thirtyMinutesIterator.setTest(pairList);
        thirtyMinutesNet.fit(thirtyMinutesIterator.next()) ;
        thirtyMinutesIterator.reset();
        thirtyMinutesNet.rnnClearPreviousState();
        log.info("Forecasting fifteen minutes...");
        double max = thirtyMinutesIterator.getMaxNum(category);
        double min = thirtyMinutesIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(thirtyMinutesNet, thirtyMinutesIterator.getTest(), max, min, thirtyMinutesIterator.getExampleLength());
        log.info("Done forecasting one minute");

        return resultSet;
    }

    private DataPointsListResultSet forecastOneHour(List<StockData> init, PriceCategory category) throws IOException {

        String period = "1 hora";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork oneHournet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        OneHourStockDataSetIterator oneHourIterator = OneHourStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = oneHourIterator.generateTestDataSet(init);
        oneHourIterator.setTest(pairList);
        oneHournet.fit(oneHourIterator.next()) ;
        oneHourIterator.reset();
        oneHournet.rnnClearPreviousState();
        log.info("Forecasting one hour...");
        double max = oneHourIterator.getMaxNum(category);
        double min = oneHourIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneHournet, oneHourIterator.getTest(), max, min, oneHourIterator.getExampleLength());
        log.info("Done forecasting one hour");

        return resultSet;
    }

    private DataPointsListResultSet forecastTwoHours(List<StockData> init, PriceCategory category) throws IOException {
        String period = "2 horas";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork twoHoursnet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        TwoHoursStockDataSetIterator twoHoursIterator = TwoHoursStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = twoHoursIterator.generateTestDataSet(init);
        twoHoursIterator.setTest(pairList);
        twoHoursnet.fit(twoHoursIterator.next()) ;
        twoHoursIterator.reset();
        twoHoursnet.rnnClearPreviousState();
        log.info("Forecasting one hour...");
        double max = twoHoursIterator.getMaxNum(category);
        double min = twoHoursIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(twoHoursnet, twoHoursIterator.getTest(), max, min, twoHoursIterator.getExampleLength());
        log.info("Done forecasting one hour");

        return resultSet;
    }


    private DataPointsListResultSet forecastFourHours(List<StockData> init, PriceCategory category) throws IOException {
        String period = "4 horas";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork fourHoursnet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        FourHoursStockDataSetIterator fourHoursIterator = FourHoursStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = fourHoursIterator.generateTestDataSet(init);
        fourHoursIterator.setTest(pairList);
        fourHoursnet.fit(fourHoursIterator.next()) ;
        fourHoursIterator.reset();
        fourHoursnet.rnnClearPreviousState();
        log.info("Forecasting one hour...");
        double max = fourHoursIterator.getMaxNum(category);
        double min = fourHoursIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(fourHoursnet, fourHoursIterator.getTest(), max, min, fourHoursIterator.getExampleLength());
        log.info("Done forecasting one hour");

        return resultSet;
    }

    private DataPointsListResultSet forecastOneDay(List<StockData> init, PriceCategory category) throws IOException {
        String period = "1 dia";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork oneDayNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        OneDayStockDataSetIterator oneDayIterator = OneDayStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = oneDayIterator.generateTestDataSet(init);
        oneDayIterator.setTest(pairList);
        oneDayNet.fit(oneDayIterator.next()) ;
        oneDayIterator.reset();
        oneDayNet.rnnClearPreviousState();
        log.info("Forecasting one day...");
        double max = oneDayIterator.getMaxNum(category);
        double min = oneDayIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneDayNet, oneDayIterator.getTest(), max, min, oneDayIterator.getExampleLength());
        log.info("Done forecasting one day");

        return resultSet;
    }

    private DataPointsListResultSet forecastOneWeek(List<StockData> init, PriceCategory category) throws IOException {
        String period = "1 week";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork oneWeekNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        OneWeekStockDataSetIterator oneWeekIterator = OneWeekStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = oneWeekIterator.generateTestDataSet(init);
        oneWeekIterator.setTest(pairList);
        oneWeekNet.fit(oneWeekIterator.next()) ;
        oneWeekIterator.reset();
        oneWeekNet.rnnClearPreviousState();
        log.info("Forecasting one day...");
        double max = oneWeekIterator.getMaxNum(category);
        double min = oneWeekIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneWeekNet, oneWeekIterator.getTest(), max, min, oneWeekIterator.getExampleLength());
        log.info("Done forecasting one day");

        return resultSet;
    }

    private DataPointsListResultSet forecastOneMonth(List<StockData> init, PriceCategory category) throws IOException {
        String period = "1 month";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        MultiLayerNetwork oneMonthNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Collections.reverse(init);
        OneMonthStockDataSetIterator oneMonthIterator = OneMonthStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = oneMonthIterator.generateTestDataSet(init);
        oneMonthIterator.setTest(pairList);
        oneMonthNet.fit(oneMonthIterator.next()) ;
        oneMonthIterator.reset();
        oneMonthNet.rnnClearPreviousState();
        log.info("Forecasting one month...");
        double max = oneMonthIterator.getMaxNum(category);
        double min = oneMonthIterator.getMinNum(category);
        DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneMonthNet, oneMonthIterator.getTest(), max, min, oneMonthIterator.getExampleLength());
        log.info("Done forecasting one month");

        return resultSet;
    }
}

