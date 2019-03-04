package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.OneDayStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.OneDayRecurrentNets;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import javafx.util.Pair;
import org.apache.commons.lang.time.StopWatch;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class OneDayPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public DataPointsListResultSet predictOneDay(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate, String nomeDoConjunto) throws IOException, InterruptedException {

        DataPointsListResultSet resultSet;

        StopWatch watch = new StopWatch();

        OneDayStockDataSetIterator oneDayIterator = predictionService.getOneDayStockDataSetIterator(simbolo, predictionService.getCSVFilePathForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = oneDayIterator.getTest();

        log.info("Build lstm networks...");
        OneDayRecurrentNets oneDay = OneDayRecurrentNets.getInstance();
        MultiLayerNetwork oneDayNet = oneDay.buildOneDayLstmNetworks(oneDayIterator.inputColumns(), oneDayIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (oneDayIterator.hasNext()) oneDayNet.fit(oneDayIterator.next());
            oneDayIterator.reset();
            oneDayNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File(System.getProperty("user.home") + "/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(oneDayNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        oneDayNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(oneDayIterator.getMaxArray());
            INDArray min = Nd4j.create(oneDayIterator.getMinArray());
            predictionService.predictAllCategories(oneDayNet, test, max, min, oneDayIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + (watch.getTime()/1000) + " seg");
            return null;
        } else {
            double max = oneDayIterator.getMaxNum(category);
            double min = oneDayIterator.getMinNum(category);
            resultSet = predictionService.predictPriceOneAhead(oneDayNet, test, max, min, oneDayIterator.getExampleLength(), nomeDoConjunto, oneDayIterator);
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + (watch.getTime()/1000) + " get");
        }
        return resultSet;
    }
}
