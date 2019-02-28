package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.iterators.OneDayStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.iterators.OneHourStockDataSetIterator;
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
        String nomeDoConjunto = "forecastMockName";
        
        //TODO: create forecast for all time periods
//        List<String> oneHourResults = forecastOneHour(latestStockDataList, category);
        List<String> oneDayResults = forecastOneDay(latestStockDataList, category);

//        DataPointsListResultSet dataPointsList = predictionService.prepareDataPointToBeSaved(oneHourResults, nomeDoConjunto,customBaseTimeSeriesMock);
        DataPointsListResultSet oneDayDataPointsList = predictionService.prepareDataPointToBeSaved(oneDayResults, nomeDoConjunto,customBaseTimeSeriesMock);
        Collections.reverse(oneDayDataPointsList.getPredictDataPointsModelList());

        NumberFormat formatter = new DecimalFormat("#0.00");

        HashMap<String, Double> predictions = new HashMap<>();
        predictions.put("oneMinute", 1.0);
        predictions.put("fifteenMinutes", 15.0);
        predictions.put("thirtyMinutes", 30.0);
        predictions.put("oneHour", 1.0);
//        predictions.put("oneHour", Double.valueOf(formatter.format(dataPointsList.getPredictDataPointsModelList().get(0).getY())));
        predictions.put("twoHours", 2.0);
        predictions.put("fourHours", 4.0);
        predictions.put("twentyFourHours", Double.valueOf(formatter.format(oneDayDataPointsList.getPredictDataPointsModelList().get(0).getY())));

        return predictions;
    }


    private List<String> forecastOneHour(List<StockData> init, PriceCategory category) throws IOException {

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
        List<String> dataPointsList = predictionService.predictPriceOneAhead(oneHournet, oneHourIterator.getTest(), max, min, oneHourIterator.getExampleLength());
        log.info("Done forecasting one hour");

        return dataPointsList;
    }

    private List<String> forecastOneDay(List<StockData> init, PriceCategory category) throws IOException {
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
        List<String> dataPointsList = predictionService.predictPriceOneAhead(oneDayNet, oneDayIterator.getTest(), max, min, oneDayIterator.getExampleLength());
        log.info("Done forecasting one day");

        return dataPointsList;
    }

}

