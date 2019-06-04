package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.FifteenMinutesStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.FifteenMinutesRecurrentNets;
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
public class FifteenMinutesPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public List<String> predictFifteenMinutes(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        FifteenMinutesStockDataSetIterator fifteenMinutesStockDataSetIterator = predictionService.getFifteenMinutesStockDataSetIterator(simbolo, predictionService.getCSVFilePathForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = fifteenMinutesStockDataSetIterator.getTest();

        log.info("Build lstm networks...");
        FifteenMinutesRecurrentNets fifteenMinutes = FifteenMinutesRecurrentNets.getInstance();
        MultiLayerNetwork fifteenMinutesNet = fifteenMinutes.buildFifteenMinutesLstmNetworks(fifteenMinutesStockDataSetIterator.inputColumns(), fifteenMinutesStockDataSetIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (fifteenMinutesStockDataSetIterator.hasNext()) fifteenMinutesNet.fit(fifteenMinutesStockDataSetIterator.next());
            fifteenMinutesStockDataSetIterator.reset();
            fifteenMinutesNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File(System.getProperty("user.home") + "/models/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(fifteenMinutesNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        fifteenMinutesNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(fifteenMinutesStockDataSetIterator.getMaxArray());
            INDArray min = Nd4j.create(fifteenMinutesStockDataSetIterator.getMinArray());
            predictionService.predictAllCategories(fifteenMinutesNet, test, max, min, fifteenMinutesStockDataSetIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = fifteenMinutesStockDataSetIterator.getMaxNum(category);
            double min = fifteenMinutesStockDataSetIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(fifteenMinutesNet, test, max, min, fifteenMinutesStockDataSetIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
