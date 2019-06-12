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
    int batchSize = 32; // mini-batch size
    double splitRatio = 0.8; // 80% for training, 10% for testing

    public HashMap<String, Double> initializeForecasts(List<StockData> lastStockData, PriceCategory category) throws IOException, InterruptedException {

        DataPointsListResultSet oneMinuteResults = forecastOneMinute(lastStockData, category);
        DataPointsListResultSet fiveMinutesResults = forecastFiveMinutes(lastStockData, category);
        DataPointsListResultSet tenMinutesResults = forecastTenMinutes(lastStockData, category);
        DataPointsListResultSet fifteenMinuteResults = forecastFifteenMinutes(lastStockData, category);
        DataPointsListResultSet thirtyMinuteResults = forecastThirtyMinutes(lastStockData, category);
        DataPointsListResultSet oneHourResults = forecastOneHour(lastStockData, category);
        DataPointsListResultSet twoHoursResults = forecastTwoHours(lastStockData, category);
        DataPointsListResultSet fourHoursResults = forecastFourHours(lastStockData, category);
        DataPointsListResultSet oneDayResults = forecastOneDay(lastStockData, category);
        DataPointsListResultSet oneWeekResults = forecastOneWeek(lastStockData, category);
        DataPointsListResultSet oneMonthResults = forecastOneMonth(lastStockData, category);

        NumberFormat formatter = new DecimalFormat("#0.00");

        HashMap<String, Double> predictions = new HashMap<>();

        if (oneMinuteResults != null && oneMinuteResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(oneMinuteResults.getPredictDataPointsList());
            predictions.put("oneMinute", Double.valueOf(formatter.format(oneMinuteResults.getPredictDataPointsList().get(0).getY())));
            log.info("One minute forecast: " + oneMinuteResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("oneMinute", 0.0);
        }

        if (fiveMinutesResults != null && fiveMinutesResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(fiveMinutesResults.getPredictDataPointsList());
            predictions.put("fiveMinutes", Double.valueOf(formatter.format(fiveMinutesResults.getPredictDataPointsList().get(0).getY())));
            log.info("Five minutes forecast: " + fiveMinutesResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("fiveMinutes", 0.0);
        }

        if (tenMinutesResults != null && tenMinutesResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(tenMinutesResults.getPredictDataPointsList());
            predictions.put("tenMinutes", Double.valueOf(formatter.format(tenMinutesResults.getPredictDataPointsList().get(0).getY())));
            log.info("Ten minutes forecast: " + tenMinutesResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("tenMinutes", 0.0);
        }

        if (fifteenMinuteResults != null && fifteenMinuteResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(fifteenMinuteResults.getPredictDataPointsList());
            predictions.put("fifteenMinutes", Double.valueOf(formatter.format(fifteenMinuteResults.getPredictDataPointsList().get(0).getY())));
            log.info("Fifteen minutes forecast: " + fifteenMinuteResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("fifteenMinutes", 0.0);
        }

        if (thirtyMinuteResults != null && thirtyMinuteResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(thirtyMinuteResults.getPredictDataPointsList());
            predictions.put("thirtyMinutes", Double.valueOf(formatter.format(thirtyMinuteResults.getPredictDataPointsList().get(0).getY())));
            log.info("Thirty minutes forecast: " + thirtyMinuteResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("thirtyMinutes", 0.0);
        }

        if (oneHourResults != null && oneHourResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(oneHourResults.getPredictDataPointsList());
            predictions.put("oneHour", Double.valueOf(formatter.format(oneHourResults.getPredictDataPointsList().get(0).getY())));
            log.info("One hour forecast: " + oneHourResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("hour", 0.0);
        }

        if (twoHoursResults != null && twoHoursResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(twoHoursResults.getPredictDataPointsList());
            predictions.put("twoHours", Double.valueOf(formatter.format(twoHoursResults.getPredictDataPointsList().get(0).getY())));
            log.info("Two hour forecast: " + twoHoursResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("twoHours", 0.0);
        }

        if (fourHoursResults != null && fourHoursResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(fourHoursResults.getPredictDataPointsList());
            predictions.put("fourHours", Double.valueOf(formatter.format(fourHoursResults.getPredictDataPointsList().get(0).getY())));
            log.info("Four hours forecast: " + fourHoursResults.getPredictDataPointsList().get(0).toString());
        }

        if (oneDayResults != null && oneDayResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(oneDayResults.getPredictDataPointsList());
            predictions.put("twentyFourHours", Double.valueOf(formatter.format(oneDayResults.getPredictDataPointsList().get(0).getY())));
            log.info("One Day forecast: " + oneDayResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("twentyFourHours", 0.0);
        }

        if (oneWeekResults != null && oneWeekResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(oneWeekResults.getPredictDataPointsList());
            predictions.put("oneWeek", Double.valueOf(formatter.format(oneWeekResults.getPredictDataPointsList().get(0).getY())));
            log.info("One Week forecast: " + oneWeekResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("oneWeek", 0.0);
        }

        if (oneMonthResults != null && oneMonthResults.getPredictDataPointsList().get(0).getY() > 0) {
            Collections.reverse(oneMonthResults.getPredictDataPointsList());
            predictions.put("oneMonth", Double.valueOf(formatter.format(oneMonthResults.getPredictDataPointsList().get(0).getY())));
            log.info("One Week forecast: " + oneMonthResults.getPredictDataPointsList().get(0).toString());
        } else {
            predictions.put("oneMonth", 0.0);
        }

        return predictions;
    }

    private DataPointsListResultSet forecastOneMinute(List<StockData> init, PriceCategory category) throws IOException{
        String period = "1 minuto";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        if (FileLocation.exists()) {
            MultiLayerNetwork oneMinutenet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);
            OneMinuteStockDataSetIterator oneMinuteIterator = OneMinuteStockDataSetIterator.getInstance();
            List<StockData> stockDataList =init;
            oneMinuteIterator.setFullStockDataList(stockDataList);
            oneMinuteIterator.setMiniBatchSize(batchSize);
            oneMinuteIterator.setExampleLength(80);
            oneMinuteIterator.setCategory(category);
            oneMinuteIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            oneMinuteIterator.setTrain(stockDataList.subList(0, oneMinuteIterator.getSplit()));
            oneMinuteIterator.setTest(oneMinuteIterator.generateTestDataSet(stockDataList.subList(oneMinuteIterator.getSplit(), stockDataList.size())));
            oneMinuteIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = oneMinuteIterator.getTest();
            int examplelength = oneMinuteIterator.getExampleLength();

            while (oneMinuteIterator.hasNext()) oneMinutenet.fit(oneMinuteIterator.next());
            oneMinuteIterator.reset();
            oneMinutenet.rnnClearPreviousState();

            log.info("Forecasting one minute...");
            double max = oneMinuteIterator.getMaxNum(category);
            double min = oneMinuteIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneMinutenet, test, max, min, examplelength);

            return resultSet;

        }else {
            log.info("1 minute model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastFiveMinutes(List<StockData> init, PriceCategory category) throws IOException{
        String period = "5 minutos";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        if (FileLocation.exists()) {
            MultiLayerNetwork fiveMinutesNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);
            FiveMinutesStockDataSetIterator fiveMinutesIterator = FiveMinutesStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            fiveMinutesIterator.setFullStockDataList(stockDataList);
            fiveMinutesIterator.setMiniBatchSize(batchSize);
            fiveMinutesIterator.setExampleLength(80);
            fiveMinutesIterator.setCategory(category);
            fiveMinutesIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            fiveMinutesIterator.setTrain(stockDataList.subList(0, fiveMinutesIterator.getSplit()));
            fiveMinutesIterator.setTest(fiveMinutesIterator.generateTestDataSet(stockDataList.subList(fiveMinutesIterator.getSplit(), stockDataList.size())));
            fiveMinutesIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = fiveMinutesIterator.getTest();
            int examplelength = fiveMinutesIterator.getExampleLength();

            while (fiveMinutesIterator.hasNext()) fiveMinutesNet.fit(fiveMinutesIterator.next());
            fiveMinutesIterator.reset();
            fiveMinutesNet.rnnClearPreviousState();

            log.info("Forecasting five minutes...");
            double max = fiveMinutesIterator.getMaxNum(category);
            double min = fiveMinutesIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(fiveMinutesNet, test, max, min, examplelength);

            return resultSet;

        }else {
            log.info("5 minutes model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastTenMinutes(List<StockData> init, PriceCategory category) throws IOException{
        String period = "10 minutos";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        if (FileLocation.exists()) {
            MultiLayerNetwork tenMinutesNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);
            TenMinutesStockDataSetIterator tenMinutesIterator = TenMinutesStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            tenMinutesIterator.setFullStockDataList(stockDataList);
            tenMinutesIterator.setMiniBatchSize(batchSize);
            tenMinutesIterator.setExampleLength(80);
            tenMinutesIterator.setCategory(category);
            tenMinutesIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            tenMinutesIterator.setTrain(stockDataList.subList(0, tenMinutesIterator.getSplit()));
            tenMinutesIterator.setTest(tenMinutesIterator.generateTestDataSet(stockDataList.subList(tenMinutesIterator.getSplit(), stockDataList.size())));
            tenMinutesIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = tenMinutesIterator.getTest();
            int examplelength = tenMinutesIterator.getExampleLength();

            while (tenMinutesIterator.hasNext()) tenMinutesNet.fit(tenMinutesIterator.next());
            tenMinutesIterator.reset();
            tenMinutesNet.rnnClearPreviousState();

            log.info("Forecasting ten minutes...");
            double max = tenMinutesIterator.getMaxNum(category);
            double min = tenMinutesIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(tenMinutesNet, test, max, min, examplelength);

            return resultSet;

        }else {
            log.info("10 minutes model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastFifteenMinutes(List<StockData> init, PriceCategory category) throws IOException {
        String period = "15 minutos";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        if (FileLocation.exists()){
            MultiLayerNetwork fifteenMinutesNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);
            FifteenMinutesStockDataSetIterator fifteenMinutesIterator = FifteenMinutesStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            fifteenMinutesIterator.setFullStockDataList(stockDataList);
            fifteenMinutesIterator.setMiniBatchSize(batchSize);
            fifteenMinutesIterator.setExampleLength(80);
            fifteenMinutesIterator.setCategory(category);
            fifteenMinutesIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            fifteenMinutesIterator.setTrain(stockDataList.subList(0, fifteenMinutesIterator.getSplit()));
            fifteenMinutesIterator.setTest(fifteenMinutesIterator.generateTestDataSet(stockDataList.subList(fifteenMinutesIterator.getSplit(), stockDataList.size())));
            fifteenMinutesIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = fifteenMinutesIterator.getTest();
            int examplelength = fifteenMinutesIterator.getExampleLength();

            while (fifteenMinutesIterator.hasNext()) fifteenMinutesNet.fit(fifteenMinutesIterator.next());
            fifteenMinutesIterator.reset();
            fifteenMinutesNet.rnnClearPreviousState();

            log.info("Forecasting fifteen minutes...");
            double max = fifteenMinutesIterator.getMaxNum(category);
            double min = fifteenMinutesIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(fifteenMinutesNet, test, max, min, examplelength);

            return resultSet;

        } else {
            log.info("15 minutes model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastThirtyMinutes(List<StockData> init, PriceCategory category) throws IOException {
        String period = "30 minutos";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        if (FileLocation.exists()) {
            MultiLayerNetwork thirtyMinutesNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);

            ThirtyMinutesStockDataSetIterator thirtyMinutesIterator = ThirtyMinutesStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            thirtyMinutesIterator.setFullStockDataList(stockDataList);
            thirtyMinutesIterator.setMiniBatchSize(batchSize);
            thirtyMinutesIterator.setExampleLength(80);
            thirtyMinutesIterator.setCategory(category);
            thirtyMinutesIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            thirtyMinutesIterator.setTrain(stockDataList.subList(0, thirtyMinutesIterator.getSplit()));
            thirtyMinutesIterator.setTest(thirtyMinutesIterator.generateTestDataSet(stockDataList.subList(thirtyMinutesIterator.getSplit(), stockDataList.size())));
            thirtyMinutesIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = thirtyMinutesIterator.getTest();
            int examplelength = thirtyMinutesIterator.getExampleLength();

            thirtyMinutesNet.fit(thirtyMinutesIterator.next());
            thirtyMinutesIterator.reset();
            thirtyMinutesNet.rnnClearPreviousState();
            log.info("Forecasting thirty minutes...");
            double max = thirtyMinutesIterator.getMaxNum(category);
            double min = thirtyMinutesIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(thirtyMinutesNet, test, max, min, examplelength);

            return resultSet;

        }else {
            log.info("30 minutes model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastOneHour(List<StockData> init, PriceCategory category) throws IOException {

        String period = "1 hora";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/models/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));

        if (FileLocation.exists()) {
            MultiLayerNetwork oneHournet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);

            OneHourStockDataSetIterator oneHourIterator = OneHourStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            oneHourIterator.setFullStockDataList(stockDataList);
            oneHourIterator.setMiniBatchSize(batchSize);
            oneHourIterator.setExampleLength(50);
            oneHourIterator.setCategory(category);
            oneHourIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            oneHourIterator.setTrain(stockDataList.subList(0, oneHourIterator.getSplit()));
            oneHourIterator.setTest(oneHourIterator.generateTestDataSet(stockDataList.subList(oneHourIterator.getSplit(), stockDataList.size())));
            oneHourIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = oneHourIterator.getTest();
            int examplelength = oneHourIterator.getExampleLength();

            oneHournet.fit(oneHourIterator.next());
            oneHourIterator.reset();
            oneHournet.rnnClearPreviousState();

            log.info("Forecasting one hour...");
            double max = oneHourIterator.getMaxNum(category);
            double min = oneHourIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneHournet, test, max, min, examplelength);

            return resultSet;

        } else {
            log.info("1 hour model not found");

            return null;
        }
    }

    private DataPointsListResultSet forecastTwoHours(List<StockData> init, PriceCategory category) throws IOException {
        String period = "2 horas";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));

        if (FileLocation.exists()) {

            MultiLayerNetwork twoHoursnet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);

            TwoHoursStockDataSetIterator twoHoursIterator = TwoHoursStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            twoHoursIterator.setFullStockDataList(stockDataList);
            twoHoursIterator.setMiniBatchSize(batchSize);
            twoHoursIterator.setExampleLength(50);
            twoHoursIterator.setCategory(category);
            twoHoursIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            twoHoursIterator.setTrain(stockDataList.subList(0, twoHoursIterator.getSplit()));
            twoHoursIterator.setTest(twoHoursIterator.generateTestDataSet(stockDataList.subList(twoHoursIterator.getSplit(), stockDataList.size())));
            twoHoursIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = twoHoursIterator.getTest();
            int examplelength = twoHoursIterator.getExampleLength();

            twoHoursnet.fit(twoHoursIterator.next());
            twoHoursIterator.reset();
            twoHoursnet.rnnClearPreviousState();
            log.info("Forecasting two hours...");
            double max = twoHoursIterator.getMaxNum(category);
            double min = twoHoursIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(twoHoursnet, test, max, min, examplelength);

            return resultSet;

        } else {
            log.info("2 hours model not found");

            return null;
        }
    }

    private DataPointsListResultSet forecastFourHours(List<StockData> init, PriceCategory category) throws IOException, InterruptedException  {
        String period = "4 horas";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));

        if (FileLocation.exists()) {
            MultiLayerNetwork fourHoursnet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);

            FourHoursStockDataSetIterator fourHoursIterator = FourHoursStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            fourHoursIterator.setFullStockDataList(stockDataList);
            fourHoursIterator.setMiniBatchSize(batchSize);
            fourHoursIterator.setExampleLength(40);
            fourHoursIterator.setCategory(category);
            fourHoursIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            fourHoursIterator.setTrain(stockDataList.subList(0, fourHoursIterator.getSplit()));
            fourHoursIterator.setTest(fourHoursIterator.generateTestDataSet(stockDataList.subList(fourHoursIterator.getSplit(), stockDataList.size())));
            fourHoursIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = fourHoursIterator.getTest();
            int examplelength = fourHoursIterator.getExampleLength();

            fourHoursnet.fit(fourHoursIterator.next());
            fourHoursIterator.reset();
            fourHoursnet.rnnClearPreviousState();
            log.info("Forecasting four hours...");
            double max = fourHoursIterator.getMaxNum(category);
            double min = fourHoursIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(fourHoursnet, test, max, min, examplelength);

            return resultSet;

        } else {
            log.info("4 hours model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastOneDay(List<StockData> init, PriceCategory category) throws IOException, InterruptedException  {
        String period = "1 dia";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/models/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));

        if (FileLocation.exists()) {
            MultiLayerNetwork oneDayNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);

            OneDayStockDataSetIterator oneDayIterator = OneDayStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            oneDayIterator.setFullStockDataList(stockDataList);
            oneDayIterator.setMiniBatchSize(batchSize);
            oneDayIterator.setExampleLength(40);
            oneDayIterator.setCategory(category);
            oneDayIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            oneDayIterator.setTrain(stockDataList.subList(0, oneDayIterator.getSplit()));
            oneDayIterator.setTest(oneDayIterator.generateTestDataSet(stockDataList.subList(oneDayIterator.getSplit(), stockDataList.size())));
            oneDayIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = oneDayIterator.getTest();
            int examplelength = oneDayIterator.getExampleLength();

            oneDayNet.fit(oneDayIterator.next());
            oneDayIterator.reset();
            oneDayNet.rnnClearPreviousState();
            log.info("Forecasting one day...");
            double max = oneDayIterator.getMaxNum(category);
            double min = oneDayIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneDayNet, test, max, min, examplelength);

            return resultSet;
        } else {
            log.info("1 day model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastOneWeek(List<StockData> init, PriceCategory category) throws IOException {
        String period = "1 semana";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));

        if (FileLocation.exists()) {
            MultiLayerNetwork oneWeekNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);

            OneWeekStockDataSetIterator oneWeekIterator = OneWeekStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            oneWeekIterator.setFullStockDataList(stockDataList);
            oneWeekIterator.setMiniBatchSize(batchSize);
            oneWeekIterator.setExampleLength(30);
            oneWeekIterator.setCategory(category);
            oneWeekIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            oneWeekIterator.setTrain(stockDataList.subList(0, oneWeekIterator.getSplit()));
            oneWeekIterator.setTest(oneWeekIterator.generateTestDataSet(stockDataList.subList(oneWeekIterator.getSplit(), stockDataList.size())));
            oneWeekIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = oneWeekIterator.getTest();
            int examplelength = oneWeekIterator.getExampleLength();

            oneWeekNet.fit(oneWeekIterator.next());
            oneWeekIterator.reset();
            oneWeekNet.rnnClearPreviousState();
            log.info("Forecasting one week...");
            double max = oneWeekIterator.getMaxNum(category);
            double min = oneWeekIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneWeekNet, test, max, min, examplelength);

            return resultSet;
        } else {
            log.info("1 week model not found");
            return null;
        }
    }

    private DataPointsListResultSet forecastOneMonth(List<StockData> init, PriceCategory category) throws IOException  {
        String period = "1 mes";
        log.info("Loading model...");
        File FileLocation = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        if (FileLocation.exists()) {
            MultiLayerNetwork oneMonthNet = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
            Collections.reverse(init);

            OneMonthStockDataSetIterator oneMonthIterator = OneMonthStockDataSetIterator.getInstance();
            List<StockData> stockDataList = init;
            oneMonthIterator.setFullStockDataList(stockDataList);
            oneMonthIterator.setMiniBatchSize(batchSize);
            oneMonthIterator.setExampleLength(30);
            oneMonthIterator.setCategory(category);
            oneMonthIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
            oneMonthIterator.setTrain(stockDataList.subList(0, oneMonthIterator.getSplit()));
            oneMonthIterator.setTest(oneMonthIterator.generateTestDataSet(stockDataList.subList(oneMonthIterator.getSplit(), stockDataList.size())));
            oneMonthIterator.initializeOffsets();
            List<Pair<INDArray, INDArray>> test = oneMonthIterator.getTest();
            int examplelength = oneMonthIterator.getExampleLength();

            oneMonthNet.fit(oneMonthIterator.next());
            oneMonthIterator.reset();
            oneMonthNet.rnnClearPreviousState();
            log.info("Forecasting one month...");
            double max = oneMonthIterator.getMaxNum(category);
            double min = oneMonthIterator.getMinNum(category);
            DataPointsListResultSet resultSet = predictionService.forecastPriceOneAhead(oneMonthNet, test, max, min, examplelength);

            return resultSet;
        } else {
            log.info("1 month model not found");
            return null;
        }
    }
}

