package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.OneMinuteStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.OneMinuteRecurrentNets;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import javafx.util.Pair;
import org.apache.commons.lang.time.StopWatch;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class OneMinutePredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public List<String> predictOneMinute(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        OneMinuteStockDataSetIterator oneMinuteIterator = predictionService.getOneMinuteStockDataSetIterator(simbolo, predictionService.getCSVFilePathForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = oneMinuteIterator.getTest();

        log.info("Build lstm networks...");
        OneMinuteRecurrentNets oneMinute = OneMinuteRecurrentNets.getInstance();
        MultiLayerNetwork oneMinuteNet = oneMinute.buildOneMinuteLstmNetworks(oneMinuteIterator.inputColumns(), oneMinuteIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (oneMinuteIterator.hasNext()) oneMinuteNet.fit(oneMinuteIterator.next());
            oneMinuteIterator.reset();
            oneMinuteNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File("src/main/resources/StockPriceLSTM_".concat(period).concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(oneMinuteNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        oneMinuteNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(oneMinuteIterator.getMaxArray());
            INDArray min = Nd4j.create(oneMinuteIterator.getMinArray());
            predictionService.predictAllCategories(oneMinuteNet, test, max, min, oneMinuteIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = oneMinuteIterator.getMaxNum(category);
            double min = oneMinuteIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(oneMinuteNet, test, max, min, oneMinuteIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
