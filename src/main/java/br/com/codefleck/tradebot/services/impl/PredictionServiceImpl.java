package br.com.codefleck.tradebot.services.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.redesneurais.*;
import javafx.util.Pair;

@Service("predictionService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class PredictionServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    private static int exampleLength = 178; // time series length for 1 prediction

    public List<String> initTraining(int epocas, String simbolo, String categoria, BaseTimeSeries customTimeSeries) throws IOException {
        String file = new ClassPathResource("coinBaseDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        String symbol = simbolo; // stock name
        int batchSize = 64; // mini-batch size
        double splitRatio = 0.8; // 80% for training, 20% for testing
        int epochs = epocas; // training epochs
        String chosenCategory = categoria;

        log.info("Create dataSet iterator...");
        PriceCategory category = verifyCategory(chosenCategory); // CLOSE: predict close price
        StockDataSetIterator iterator = new StockDataSetIterator(file, symbol, batchSize, exampleLength, splitRatio, category);

        int split = (int) Math.round(customTimeSeries.getBarCount() * splitRatio);
        TimeSeries testTimeSeries = customTimeSeries.getSubSeries(split, customTimeSeries.getEndIndex());

        log.info("Load test dataset...");
        List<Pair<INDArray, INDArray>> test = iterator.getTestDataSet();

        log.info("Build lstm networks...");
        MultiLayerNetwork net = RecurrentNets.buildLstmNetworks(iterator.inputColumns(), iterator.totalOutcomes());

        log.info("Training...");
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (iterator.hasNext()) net.fit(iterator.next()); // fit model using mini-batch data
            iterator.reset(); // reset iterator
            net.rnnClearPreviousState(); // clear previous state
        }

        log.info("Saving model...");
        File locationToSave = new File(System.getProperty("user.home") + "\\projetos\\fleckbot21out\\fleckbot\\src\\main\\resources\\StockPriceLSTM_".concat(String.valueOf(category)).concat(".zip"));
        // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future
        ModelSerializer.writeModel(net, locationToSave, true);

        log.info("Load model...");
        net = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Testing...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(iterator.getMaxArray());
            INDArray min = Nd4j.create(iterator.getMinArray());
            predictAllCategories(net, test, max, min);
            log.info("Done...");
            return null;
        } else {
            double max = iterator.getMaxNum(category);
            double min = iterator.getMinNum(category);
            List<String> dataPointsList = predictPriceOneAhead(net, test, max, min, category, testTimeSeries);
            log.info("Done...");
            return dataPointsList;
        }
    }

    private PriceCategory verifyCategory(String chosenCategory) {

        if (chosenCategory.equalsIgnoreCase("Fechamento")){
            return PriceCategory.CLOSE;
        } else if (chosenCategory.equalsIgnoreCase("Maior")){
            return PriceCategory.HIGH;
        } else if (chosenCategory.equalsIgnoreCase("Menor")){
            return PriceCategory.LOW;
        } else if (chosenCategory.equalsIgnoreCase("Abertura")){
            return PriceCategory.OPEN;
        } else if (chosenCategory.equalsIgnoreCase("Volume")){
            return PriceCategory.VOLUME;
        }
        return PriceCategory.ALL;
    }

    /** Predict one feature of a stock one-day ahead */
    public List<String> predictPriceOneAhead (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, double max, double min, PriceCategory category, TimeSeries testTimeSeries) {
        double[] predicts = new double[testData.size()];
        double[] actuals = new double[testData.size()];
        for (int i = 0; i < testData.size(); i++) {
            predicts[i] = net.rnnTimeStep(testData.get(i).getKey()).getDouble(exampleLength - 1) * (max - min) + min;
            actuals[i] = testData.get(i).getValue().getDouble(0);
        }
        log.info("Print out Predictions and Actual Values...");
        log.info("Predict,Actual");
        for (int i = 0; i < predicts.length; i++) log.info(predicts[i] + "," + actuals[i]);
        log.info("Plot...");
        List<String> dataPointList = PlotUtil.plot(predicts, actuals, String.valueOf(category), testTimeSeries);

        return dataPointList;
    }

    /** Predict all the features (open, close, low, high prices and volume) of a stock one-day ahead */
    private void predictAllCategories (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, INDArray max, INDArray min) {
        INDArray[] predicts = new INDArray[testData.size()];
        INDArray[] actuals = new INDArray[testData.size()];
        for (int i = 0; i < testData.size(); i++) {
            predicts[i] = net.rnnTimeStep(testData.get(i).getKey()).getRow(exampleLength - 1).mul(max.sub(min)).add(min);
            actuals[i] = testData.get(i).getValue();
        }
        log.info("Print out Predictions and Actual Values...");
        log.info("Predict\tActual");
        for (int i = 0; i < predicts.length; i++) log.info(predicts[i] + "\t" + actuals[i]);
        log.info("Plot...");

        for (int n = 0; n < 5; n++) {
            double[] pred = new double[predicts.length];
            double[] actu = new double[actuals.length];

            for (int i = 0; i < predicts.length; i++) {
                pred[i] = predicts[i].getDouble(n);
                actu[i] = actuals[i].getDouble(n);
            }
            String name;
            switch (n) {
                case 0: name = "Stock OPEN Price"; break;
                case 1: name = "Stock CLOSE Price"; break;
                case 2: name = "Stock LOW Price"; break;
                case 3: name = "Stock HIGH Price"; break;
                case 4: name = "Stock VOLUME Amount"; break;
                default: throw new NoSuchElementException();
            }
        }
    }

    public BaseTimeSeries createCSVFileForNeuralNets(Date beginDate, Date endDate, String period) {

        CsvBarsLoader csvBarsLoader = new CsvBarsLoader();

        BaseTimeSeries customTimeSeries = csvBarsLoader.createCSVFileForNeuralNets(beginDate, endDate, period);

        return customTimeSeries;
    }

    public DataPointsListModel prepareDataPointToBeSaved(List<String> dataPointList, String nomeDoConjunto, String categoria) throws IOException {

        //predicts
        String predictsDataPointsArray[] = dataPointList.get(0).replace("[", "").replace("{", "").replace("}", "").replaceAll("\"\"", "").split(",");

        List<String> xPredictToken = new ArrayList<>();
        List<String> yPredictToken = new ArrayList<>();

        for (String token : predictsDataPointsArray) {

            if (token.substring(1, 2).equals("x")) {
                xPredictToken.add(token.replaceAll("\"", "").replace("x:", "").replace("]", ""));
            } else if (token.substring(1, 2).equals("y")) {
                yPredictToken.add(token.replaceAll("\"", "").replace("y:", "").replace("]", ""));
            }
        }

        List<DataPointsModel> predictsDataPointsModelListXY = new ArrayList<>();

        for (int i = 0; i < xPredictToken.size(); i++) {
            DataPointsModel dataPointsModel = new DataPointsModel();
            dataPointsModel.setNomeConjunto(nomeDoConjunto.concat("Prediction-").concat(categoria));

            dataPointsModel.setX(Double.valueOf(xPredictToken.get(i)));
            if (yPredictToken.get(i) != null) {
                dataPointsModel.setY(Double.valueOf(yPredictToken.get(i)));
            }
            predictsDataPointsModelListXY.add(dataPointsModel);
        }

        //actuals
        String actualsDataPointsArray[] = dataPointList.get(1).replace("[", "").replace("{", "").replace("}", "").replaceAll("\"\"", "").split(",");

        List<String> xActualToken = new ArrayList<>();
        List<String> yActualToken = new ArrayList<>();

        for (String token : actualsDataPointsArray) {

            if (token.substring(1, 2).equals("x")) {
                xActualToken.add(token.replaceAll("\"", "").replace("x:", "").replace("]", ""));
            } else if (token.substring(1, 2).equals("y")) {
                yActualToken.add(token.replaceAll("\"", "").replace("y:", "").replace("]", ""));
            }
        }

        List<DataPointsModel> actualDataPointsModelListXY = new ArrayList<>();

        for (int i = 0; i < xActualToken.size(); i++) {
            DataPointsModel dataPointsModel = new DataPointsModel();
            dataPointsModel.setNomeConjunto(nomeDoConjunto.concat("Actual-".concat(categoria)));
            dataPointsModel.setX(Double.valueOf(xActualToken.get(i)));

            if (yActualToken.get(i) != null) {
                dataPointsModel.setY(Double.valueOf(yActualToken.get(i)));
            }
            actualDataPointsModelListXY.add(dataPointsModel);

        }

        DataPointsListModel dataPointsListModel = new DataPointsListModel();
        dataPointsListModel.setName("Prediction&Actuals");
        dataPointsListModel.setActualDataPointsModelList(actualDataPointsModelListXY);
        dataPointsListModel.setPredictDataPointsModelList(predictsDataPointsModelListXY);

        return dataPointsListModel;
    }

    public Double calculateErrorPercentageAverage(DataPointsListModel dataPointsList) {

        List<Double> errorPercentage = new ArrayList<>();
        Double result;

        for (int i=0;i<dataPointsList.getActualDataPointsModelList().size();i++){

            result = ((dataPointsList.getActualDataPointsModelList().get(i).getY()-dataPointsList.getPredictDataPointsModelList().get(i).getY())*100)/dataPointsList.getActualDataPointsModelList().get(i).getY();

            errorPercentage.add(result);

        }

        Double errorPercentageAverage = 0.00;
        for (int i=0; i<errorPercentage.size();i++){
            errorPercentageAverage += errorPercentage.get(i).doubleValue();
        }

        errorPercentageAverage = errorPercentageAverage/errorPercentage.size();

        return errorPercentageAverage;
    }

    public Double calculateErrorPercentageLastDay(DataPointsListModel dataPointsList) {

        List<Double> errorPercentage = new ArrayList<>();
        Double result;

        int lastResult = dataPointsList.getActualDataPointsModelList().size();
        lastResult--;

        result = ((dataPointsList.getActualDataPointsModelList().get(lastResult).getY()-dataPointsList.getPredictDataPointsModelList().get(lastResult).getY())*100)/dataPointsList.getActualDataPointsModelList().get(lastResult).getY();

        errorPercentage.add(result);

        return result;
    }
}

