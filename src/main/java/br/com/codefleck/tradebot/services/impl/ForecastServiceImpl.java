package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.iterators.OneHourStockDataSetIterator;
import com.google.common.collect.Lists;
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
import java.util.List;

/**
 * @author Daniel Fleck
 */
@Service("forecastService")
@ComponentScan(basePackages = {"br.com.codefleck.tradebot"})
public class ForecastServiceImpl {

    @Autowired
    PredictionServiceImpl predictionService;

    final Logger log = LoggerFactory.getLogger(ForecastServiceImpl.class);

    public DataPointsListResultSet initializeForecasts(List<StockData> lastStockData, PriceCategory category, BaseTimeSeries customBaseTimeSeriesMock) throws IOException {

        PriceCategory priceCategory = category;
        List<StockData> latestStockDataList = lastStockData;    //stock data initialization;

        List<String> results = forecastOneHour(latestStockDataList, category);
        //TODO: create forecast for all time periods

        String nomeDoConjunto = "forecastMockName";
        DataPointsListResultSet dataPointsList = predictionService.prepareDataPointToBeSaved(results, nomeDoConjunto,customBaseTimeSeriesMock);

        return dataPointsList;
    }

    private List<String> forecastOneHour(List<StockData> init, PriceCategory category) throws IOException {

        log.info("Loading model...");
        File FileLocation = new File("/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_1hora_CLOSE.zip");
        MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        Lists.reverse(init); //PROVAVELMENTE ESSE LISTS REVERSE NÃO ESTA FUNCIONANDO TODO: CHECK IT
        OneHourStockDataSetIterator oneHourIterator = OneHourStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = oneHourIterator.generateTestDataSet(init);
        oneHourIterator.setTest(pairList);
        net.fit(oneHourIterator.next()) ; // fit model using mini-batch data
        oneHourIterator.reset(); // reset iterator
        net.rnnClearPreviousState(); // clear previous state
        log.info("Forecasting one hour...");
        double max = oneHourIterator.getMaxNum(category);
        double min = oneHourIterator.getMinNum(category);
        List<String> dataPointsList = predictionService.predictPriceOneAhead(net, oneHourIterator.getTest(), max, min, oneHourIterator.getExampleLength());
        log.info("Done forecasting one hour");

        return dataPointsList;

    }
}

