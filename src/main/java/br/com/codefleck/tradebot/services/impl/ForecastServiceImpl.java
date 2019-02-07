package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.StockDataSetIterator;
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

    public List<String> gatherForecasts(List<StockData> lastStockData, PriceCategory category) {
        int miniBatchSize = 32;						//Size of mini batch to use when  training
        int exampleLength = 140;					//Length of each training example sequence to use. This could certainly be increased
        int predictionLength = 1;
        List<StockData> generationInitialization = lastStockData;		//Optional stock data initialization; a random character is used if null
        PriceCategory priceCategory = category;

        StockDataSetIterator iter = new StockDataSetIterator(lastStockData, miniBatchSize, exampleLength, priceCategory);

        String FileLocation = "/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/5epocas_6meses_1d.zip";
        log.info("Load model...");
        MultiLayerNetwork net = null;
        try {
            net = ModelSerializer.restoreMultiLayerNetwork(FileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> results = forecastTickersFromNetwork(generationInitialization,net,iter, exampleLength, predictionLength);
        return results;
    }

    private List<String> forecastTickersFromNetwork(List<StockData> init, MultiLayerNetwork net, StockDataSetIterator iter, int exampleLength, int predictionLength) {

        PriceCategory priceCategory = iter.getCategory();

        List<Pair<INDArray, INDArray>> pairList = iter.generateForecastDataSet(init, exampleLength, predictionLength, priceCategory);
        iter.setTest(pairList);

        log.info("Training...");
        net.fit(iter.next()) ; // fit model using mini-batch data
        iter.reset(); // reset iterator
        net.rnnClearPreviousState(); // clear previous state
        log.info("Forecasting...");
        double max = iter.getMaxNum(priceCategory);
        double min = iter.getMinNum(priceCategory);
        List<String> dataPointsList = predictionService.predictPriceOneAhead(net, iter.getTest(), max, min,PriceCategory.CLOSE, null);
        log.info("Done forecasting...");

        return dataPointsList;
    }
}

