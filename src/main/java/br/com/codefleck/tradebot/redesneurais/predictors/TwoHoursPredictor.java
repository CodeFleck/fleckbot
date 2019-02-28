package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.TwoHoursStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.TwoHoursRecurrentNets;
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
public class TwoHoursPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public List<String> predictTwoHours(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        TwoHoursStockDataSetIterator twoHoursIterator = predictionService.getTwoHoursStockDataSetIterator(simbolo, predictionService.getCSVFilePathForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = twoHoursIterator.getTest();

        log.info("Build lstm networks...");
        TwoHoursRecurrentNets twoHours = TwoHoursRecurrentNets.getInstance();
        MultiLayerNetwork twoHoursNet = twoHours.buildTwoHoursLstmNetworks(twoHoursIterator.inputColumns(), twoHoursIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (twoHoursIterator.hasNext()) twoHoursNet.fit(twoHoursIterator.next());
            twoHoursIterator.reset();
            twoHoursNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(twoHoursNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        twoHoursNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(twoHoursIterator.getMaxArray());
            INDArray min = Nd4j.create(twoHoursIterator.getMinArray());
            predictionService.predictAllCategories(twoHoursNet, test, max, min, twoHoursIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = twoHoursIterator.getMaxNum(category);
            double min = twoHoursIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(twoHoursNet, test, max, min, twoHoursIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
