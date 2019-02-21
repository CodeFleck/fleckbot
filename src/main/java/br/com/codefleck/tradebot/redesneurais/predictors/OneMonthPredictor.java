package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.OneMonthStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.OneMonthRecurrentNets;
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
public class OneMonthPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public List<String> predictOneMonth(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        OneMonthStockDataSetIterator oneMonthIterator = predictionService.getOneMonthStockDataSetIterator(simbolo, predictionService.getFileForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = oneMonthIterator.getTest();

        log.info("Build lstm networks...");
        OneMonthRecurrentNets oneMonth = OneMonthRecurrentNets.getInstance();
        MultiLayerNetwork oneMonthNet = oneMonth.buildOneMonthLstmNetworks(oneMonthIterator.inputColumns(), oneMonthIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (oneMonthIterator.hasNext()) oneMonthNet.fit(oneMonthIterator.next());
            oneMonthIterator.reset();
            oneMonthNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File("/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/onemonth/StockPriceLSTM_".concat(period).concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(oneMonthNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        oneMonthNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(oneMonthIterator.getMaxArray());
            INDArray min = Nd4j.create(oneMonthIterator.getMinArray());
            predictionService.predictAllCategories(oneMonthNet, test, max, min);
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = oneMonthIterator.getMaxNum(category);
            double min = oneMonthIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(oneMonthNet, test, max, min);
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
