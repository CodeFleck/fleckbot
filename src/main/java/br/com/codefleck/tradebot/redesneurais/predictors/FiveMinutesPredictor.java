package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.FiveMinutesStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.FiveMinutesRecurrentNets;
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
public class FiveMinutesPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public List<String> predictFiveMinutes(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        FiveMinutesStockDataSetIterator fiveMinutesStockDataSetIterator = predictionService.getFiveMinutesStockDataSetIterator(simbolo, predictionService.getFileForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = fiveMinutesStockDataSetIterator.getTest();

        log.info("Build lstm networks...");
        FiveMinutesRecurrentNets fiveMinute = FiveMinutesRecurrentNets.getInstance();
        MultiLayerNetwork fiveMinutesNet = fiveMinute.buildFiveMinutesLstmNetworks(fiveMinutesStockDataSetIterator.inputColumns(), fiveMinutesStockDataSetIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (fiveMinutesStockDataSetIterator.hasNext()) fiveMinutesNet.fit(fiveMinutesStockDataSetIterator.next());
            fiveMinutesStockDataSetIterator.reset();
            fiveMinutesNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File("/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period).concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(fiveMinutesNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        fiveMinutesNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(fiveMinutesStockDataSetIterator.getMaxArray());
            INDArray min = Nd4j.create(fiveMinutesStockDataSetIterator.getMinArray());
            predictionService.predictAllCategories(fiveMinutesNet, test, max, min);
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = fiveMinutesStockDataSetIterator.getMaxNum(category);
            double min = fiveMinutesStockDataSetIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(fiveMinutesNet, test, max, min);
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
