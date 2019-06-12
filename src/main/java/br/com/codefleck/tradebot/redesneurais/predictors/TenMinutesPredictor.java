package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.iterators.TenMinutesStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.TenMinutesRecurrentNets;
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
public class TenMinutesPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;

    public DataPointsListResultSet predictTenMinutes(String simbolo, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate, String nomeDoConjunto) throws IOException, InterruptedException {

        DataPointsListResultSet resultSet;

        StopWatch watch = new StopWatch();
        TenMinutesStockDataSetIterator tenMinutesStockDataSetIterator = predictionService.getTenMinutesStockDataSetIterator(simbolo, predictionService.getCSVFilePathForTrainingNeuralNets(period), batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = tenMinutesStockDataSetIterator.getTest();

        log.info("Build lstm networks...");
        TenMinutesRecurrentNets tenMinute = TenMinutesRecurrentNets.getInstance();
        MultiLayerNetwork fiveMinutesNet = tenMinute.buildTenMinutesLstmNetworks(tenMinutesStockDataSetIterator.inputColumns(), tenMinutesStockDataSetIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (tenMinutesStockDataSetIterator.hasNext()) fiveMinutesNet.fit(tenMinutesStockDataSetIterator.next());
            tenMinutesStockDataSetIterator.reset();
            fiveMinutesNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File(System.getProperty("user.home") + "models/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(fiveMinutesNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        fiveMinutesNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(tenMinutesStockDataSetIterator.getMaxArray());
            INDArray min = Nd4j.create(tenMinutesStockDataSetIterator.getMinArray());
            predictionService.predictAllCategories(fiveMinutesNet, test, max, min, tenMinutesStockDataSetIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = tenMinutesStockDataSetIterator.getMaxNum(category);
            double min = tenMinutesStockDataSetIterator.getMinNum(category);
            resultSet = predictionService.predictPriceOneAhead(fiveMinutesNet, test, max, min, tenMinutesStockDataSetIterator.getExampleLength(), nomeDoConjunto, tenMinutesStockDataSetIterator);
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return resultSet;
    }
}
