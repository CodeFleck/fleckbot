package br.com.codefleck.tradebot.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.StockDataSetIterator;
import breeze.linalg.max;
import breeze.linalg.min;
import javafx.util.Pair;

/**
 * @author Daniel Fleck
 */
@Service("forecastService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot"})
public class ForecastServiceImpl {

    @Autowired
    PredictionServiceImpl predictionService;

    public List<String>  initializeForecasts(List<StockData> lastStockData) throws IOException {

        final Logger log = LoggerFactory.getLogger(ForecastServiceImpl.class);

        StockDataSetIterator iterator = new StockDataSetIterator(lastStockData, 8, 1000);

        List<Pair<INDArray, INDArray>> test = iterator.generateTestDataSet(lastStockData);

        String FileLocation = "/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/5epocas_6meses_1d.zip";

        log.info("Load model...");
        MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(FileLocation);

        log.info("Forecasting...");

//        double[] max = iterator.getMaxArray();
//        double[] min = iterator.getMinArray();

        double[] predicts = iterator.getMaxArray();
//        double[] predicts = new double[test.size()];
        for(int i =0; i < test.size(); i++) {
            //predicts[i] = net.rnnTimeStep(test.get(i).getKey()).getDouble(100 - 1).mul(max.sub(min)).add(min);;
            predicts[i] = net.rnnTimeStep(test.get(5).getKey()).getRow(100 - 1).getDouble(5);
        }
        Arrays.stream(predicts).forEach(s -> System.out.println(s));
        log.info("Done...");
        return null;
    }
}

