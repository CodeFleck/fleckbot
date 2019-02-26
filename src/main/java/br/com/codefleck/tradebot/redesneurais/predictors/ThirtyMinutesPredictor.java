package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.iterators.ThirtyMinutesStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.ThirtyMinutesRecurrentNets;
import br.com.codefleck.tradebot.services.impl.IteratorServiceImpl;
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
public class ThirtyMinutesPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;
    @Autowired
    IteratorServiceImpl iteratorService;

    public List<String> predictThirtyMinutes(List<StockData> stockDataList, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        ThirtyMinutesStockDataSetIterator thirtyMinutesStockDataSetIterator = iteratorService.getThirtyMinutesStockDataSetIterator(stockDataList, batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = thirtyMinutesStockDataSetIterator.getTest();

        log.info("Build lstm networks...");
        ThirtyMinutesRecurrentNets thirtyMinute = ThirtyMinutesRecurrentNets.getInstance();
        MultiLayerNetwork thirtyMinutesNet = thirtyMinute.buildThirtyMinutesLstmNetworks(thirtyMinutesStockDataSetIterator.inputColumns(), thirtyMinutesStockDataSetIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (thirtyMinutesStockDataSetIterator.hasNext()) thirtyMinutesNet.fit(thirtyMinutesStockDataSetIterator.next());
            thirtyMinutesStockDataSetIterator.reset();
            thirtyMinutesNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File("src/main/resources/StockPriceLSTM_".concat(period).concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(thirtyMinutesNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        thirtyMinutesNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(thirtyMinutesStockDataSetIterator.getMaxArray());
            INDArray min = Nd4j.create(thirtyMinutesStockDataSetIterator.getMinArray());
            predictionService.predictAllCategories(thirtyMinutesNet, test, max, min, thirtyMinutesStockDataSetIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = thirtyMinutesStockDataSetIterator.getMaxNum(category);
            double min = thirtyMinutesStockDataSetIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(thirtyMinutesNet, test, max, min, thirtyMinutesStockDataSetIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
