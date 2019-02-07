package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.models.DataPointsListModel;
import br.com.codefleck.tradebot.models.DataPointsModel;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.PlotUtil;
import br.com.codefleck.tradebot.redesneurais.PriceCategory;
import br.com.codefleck.tradebot.redesneurais.RecurrentNets;
import br.com.codefleck.tradebot.redesneurais.StockDataSetIterator;
import br.com.codefleck.tradebot.tradingInterfaces.Ticker;
import javafx.util.Pair;
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
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service("predictionService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class PredictionServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    //private static int exampleLength = 30; //1D - gerou 66 resultados
    //private static int exampleLength = 40; //4h - gerou 396 ( 2017-04-18 a 2017-06-23)
    //private static int exampleLength = 40; //2h - gerou 834 (2017-04-18 a 2017-06-26) - média de erro: 25.28%
    //private static int exampleLength = 80; //2h - gerou de (2017-04-18 a 2017-06-23) -  média de erro: 16.96%
    //private static int exampleLength = 140; //2h - gerou de (2017-04-18 a 2017-06-18) -  média de erro: 16.78%
    //private static int exampleLength = 140; //1h - gerou de (2017-04-18 a 2017-06-24) -   média de erro: 16.16%%
    //private static int exampleLength = 50; //1h - gerou de (2017-04-18 a 2017-06-27) - media de erro: 18.16%
    //private static int exampleLength = 30; //1h - gerou de (2017-04-18 a 2017-06-28) -  média de erro: 21.54%
    //private static int exampleLength = 280; //30min - gerou de ( 2017-04-18 a 2017-05-18) - Porcentagem média de erro: 14.63%
    //private static int exampleLength = 380; //30min - gerou de (2017-04-18 a 2017-06-22) - media de erro: 25.26%
    //private static int exampleLength = 180; //1min  2017-04-18 to 2017-04-20 - 10.82%
    //private static int exampleLength = 80; //1min  2017-04-18 to 2017-06-29 - média de erro: 17.96%
    //private static int exampleLength = 180; //15min fucking 2017-04-18 to 2017-05-03 - media erro: -0,23%!!!!!
    //private static int exampleLength = 80; //15min 2017-04-18 to 2017-05-29 - media erro: 15.22%!!!!!

//    private static int exampleLength = 180; //1D gerou 36
    private static int exampleLength = 140;

    public List<String> initTraining(int epocas, String simbolo, String categoria, BaseTimeSeries customTimeSeries) throws IOException {
        String file = new ClassPathResource("coinBaseDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        String symbol = simbolo; // stock name
        int batchSize = 32; // mini-batch size
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
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime = localDateTime.toLocalTime();
        DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        File locationToSave = new File("/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(String.valueOf(category)).concat(localTime.format(DATE_FORMAT)).concat(".zip"));
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
    public void predictAllCategories (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, INDArray max, INDArray min) {
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

    public Double calculateMajorError(DataPointsListModel dataPointsList) {

        Double majorError = ((dataPointsList.getActualDataPointsModelList().get(0).getY() - dataPointsList.getPredictDataPointsModelList().get(0).getY()) * 100) / dataPointsList.getActualDataPointsModelList().get(0).getY();
        Double result;
        for (int i=0;i<dataPointsList.getActualDataPointsModelList().size();i++) {

            result = ((dataPointsList.getActualDataPointsModelList().get(i).getY() - dataPointsList.getPredictDataPointsModelList().get(i).getY()) * 100) / dataPointsList.getActualDataPointsModelList().get(i).getY();

            if (result > majorError) {
                majorError = result;
            }
        }
        return majorError;
    }

    public Double calculateMinorError(DataPointsListModel dataPointsList) {

        Double minorError = ((dataPointsList.getActualDataPointsModelList().get(0).getY() - dataPointsList.getPredictDataPointsModelList().get(0).getY()) * 100) / dataPointsList.getActualDataPointsModelList().get(0).getY();

        Double result;

        for (int i=0;i<dataPointsList.getActualDataPointsModelList().size();i++) {

            result = ((dataPointsList.getActualDataPointsModelList().get(i).getY() - dataPointsList.getPredictDataPointsModelList().get(i).getY()) * 100) / dataPointsList.getActualDataPointsModelList().get(i).getY();

            if (result < minorError) {
                minorError = result;
            }
        }

        return minorError;
    }

    public StockData transformTickerInStockData(Ticker ticker) {

        String today = LocalDateTime.now().toString();

        if(!(ticker.getOpen() == null)) {
            ticker.getOpen().doubleValue();
        } else {
            ticker.setOpen(ticker.getBid());
        }
        StockData stockData = new StockData(
            today,
            "BTC",
            ticker.getOpen().doubleValue(),
            ticker.getLast().doubleValue(),
            ticker.getLow().doubleValue(),
            ticker.getHigh().doubleValue(),
            ticker.getVolume().doubleValue()
        );
        return stockData;
    }

    public List<StockData> transformBarInStockData(BaseTimeSeries baseTimeSeries) {

        List<Bar> barList = baseTimeSeries.getBarData();
        List<StockData> stockDataList = new ArrayList<>();

        double openPrice;

        for (int i=0; i < barList.size(); i++) {

            if (barList.get(i) != null) {
                if (!(barList.get(i).getOpenPrice() == null)) {
                    openPrice = barList.get(i).getOpenPrice().doubleValue();
                } else {
                    openPrice = barList.get(i).getMinPrice().doubleValue();
                }

                String simpleDateName = barList.get(i).getSimpleDateName();
                simpleDateName.replaceAll("39", "20");

                StockData stockData = new StockData(
                        simpleDateName,
                        "BTCUSD",
                        openPrice,
                        barList.get(i).getClosePrice().doubleValue(),
                        barList.get(i).getMinPrice().doubleValue(),
                        barList.get(i).getMaxPrice().doubleValue(),
                        barList.get(i).getVolume().doubleValue()
                );

                stockDataList.add(stockData);
            }
        }
        return stockDataList;
    }

}

