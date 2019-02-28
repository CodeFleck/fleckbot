package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.FourHoursStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.FourHoursRecurrentNets;
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
public class FourHoursPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public List<String> predictFourHours(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        FourHoursStockDataSetIterator fourHoursIterator = predictionService.getFourHoursStockDataSetIterator(simbolo, predictionService.getCSVFilePathForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = fourHoursIterator.getTest();

        log.info("Build lstm networks...");
        FourHoursRecurrentNets fourHours = FourHoursRecurrentNets.getInstance();
        MultiLayerNetwork fourHoursNet = fourHours.buildFourHoursLstmNetworks(fourHoursIterator.inputColumns(), fourHoursIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (fourHoursIterator.hasNext()) fourHoursNet.fit(fourHoursIterator.next());
            fourHoursIterator.reset();
            fourHoursNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(fourHoursNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        fourHoursNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(fourHoursIterator.getMaxArray());
            INDArray min = Nd4j.create(fourHoursIterator.getMinArray());
            predictionService.predictAllCategories(fourHoursNet, test, max, min, fourHoursIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = fourHoursIterator.getMaxNum(category);
            double min = fourHoursIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(fourHoursNet, test, max, min, fourHoursIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
