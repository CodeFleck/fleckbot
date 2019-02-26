package br.com.codefleck.tradebot.redesneurais.predictors;

import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.iterators.OneHourStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.OneHourRecurrentNets;
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
public class OneHourPredictor {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    PredictionServiceImpl predictionService;
    @Autowired
    IteratorServiceImpl iteratorService;

    public List<String> predictOneHour(List<StockData> stockDataList, String period, int batchSize, double splitRatio, PriceCategory category, int epochs, double learningRate) throws IOException {

        List<String> dataPointsList;

        StopWatch watch = new StopWatch();
        OneHourStockDataSetIterator oneHourIterator = iteratorService.getOneHourStockDataSetIterator(stockDataList, batchSize, splitRatio, category);
        List<Pair<INDArray, INDArray>> test = oneHourIterator.getTest();

        log.info("Build lstm networks...");
        OneHourRecurrentNets oneHour = OneHourRecurrentNets.getInstance();
        MultiLayerNetwork oneHourNet = oneHour.buildOneHourLstmNetworks(oneHourIterator.inputColumns(), oneHourIterator.totalOutcomes(), learningRate);

        log.info("Training...");
        watch.start();
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (oneHourIterator.hasNext()) oneHourNet.fit(oneHourIterator.next());
            oneHourIterator.reset();
            oneHourNet.rnnClearPreviousState();
        }
        watch.stop();
        log.info("Saving model...");
        File locationToSave = new File("src/main/resources/StockPriceLSTM_".concat(period.replace(" ", "")).concat(String.valueOf(category)).concat(".zip"));
        ModelSerializer.writeModel(oneHourNet, locationToSave, true); // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future

        log.info("Loading model...");
        oneHourNet = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Performing tests...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(oneHourIterator.getMaxArray());
            INDArray min = Nd4j.create(oneHourIterator.getMinArray());
            predictionService.predictAllCategories(oneHourNet, test, max, min, oneHourIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return null;
        } else {
            double max = oneHourIterator.getMaxNum(category);
            double min = oneHourIterator.getMinNum(category);
            dataPointsList = predictionService.predictPriceOneAhead(oneHourNet, test, max, min, oneHourIterator.getExampleLength());
            log.info(period + " done testing...");
            System.out.println("Time Elapsed: " + watch.getTime());
        }
        return dataPointsList;
    }
}
