package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.OneHourStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.PriceCategory;
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

import java.io.File;
import java.io.IOException;
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

    public List<String> initializeForecasts(List<StockData> lastStockData, PriceCategory category) throws IOException {

        List<StockData> generationInitialization = lastStockData;		//stock data initialization;
        PriceCategory priceCategory = category;

        log.info("Load model...");
        File FileLocation = new File("/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/5epocas_6meses_1min.zip");
        MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        List<String> results = forecastWithLatestTickers(generationInitialization,net, category);



        return results;
    }

    private List<String> forecastWithLatestTickers(List<StockData> init, MultiLayerNetwork net, PriceCategory category) {

        Lists.reverse(init);
        OneHourStockDataSetIterator oneHourIterator = OneHourStockDataSetIterator.getInstance();
        List<Pair<INDArray, INDArray>> pairList = oneHourIterator.generateTestDataSet(init);
        oneHourIterator.setTest(pairList);

        log.info("Training...");
        net.fit(oneHourIterator.next()) ; // fit model using mini-batch data
        oneHourIterator.reset(); // reset iterator
        net.rnnClearPreviousState(); // clear previous state
        log.info("Forecasting...");
        double max = oneHourIterator.getMaxNum(category);
        double min = oneHourIterator.getMinNum(category);
        List<String> dataPointsList = predictionService.predictPriceOneAhead(net, oneHourIterator.getTest(), max, min, null);
        log.info("Done forecasting...");

        return dataPointsList;
    }
}

