package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.models.DataPointsListModel;
import br.com.codefleck.tradebot.models.DataPointsModel;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.*;
import br.com.codefleck.tradebot.tradingInterfaces.Ticker;
import javafx.util.Pair;
import org.apache.commons.lang.time.StopWatch;
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
import org.ta4j.core.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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
    private static int exampleLength = 50;

    public List<String> initTraining(int epocas, String simbolo, String categoria, BaseTimeSeries customTimeSeries, String period) throws IOException {
        String file = new ClassPathResource("DataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        int batchSize = 32; // mini-batch size
        double splitRatio = 0.8; // 80% for training, 20% for testing
        int epochs = epocas; // training epochs
        String chosenCategory = categoria;

        log.info("Create dataSet iterator...");
        PriceCategory category = verifyCategory(chosenCategory); // CLOSE: predict close price
        OneHourStockDataSetIterator oneHourIterator = getIterator(period);
        List<StockData> stockDataList = oneHourIterator.readStockDataFromFile(file, simbolo);
        oneHourIterator.setMiniBatchSize(batchSize);
        oneHourIterator.setExampleLength(exampleLength);
        oneHourIterator.setCategory(category);
        oneHourIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneHourIterator.setTrain(stockDataList.subList(0, oneHourIterator.getSplit()));
        oneHourIterator.setTest(oneHourIterator.generateTestDataSet(stockDataList.subList(oneHourIterator.getSplit(), stockDataList.size())));
        oneHourIterator.initializeOffsets();

        int split = (int) Math.round(customTimeSeries.getBarCount() * splitRatio);
        TimeSeries testTimeSeries = customTimeSeries.getSubSeries(split, customTimeSeries.getEndIndex());

        log.info("Load test dataset...");
        List<Pair<INDArray, INDArray>> test = oneHourIterator.getTestDataSet();

        log.info("Build lstm networks...");
        MultiLayerNetwork net = RecurrentNets.buildLstmNetworks(oneHourIterator.inputColumns(), oneHourIterator.totalOutcomes());

        log.info("Training...");
        StopWatch watch = new StopWatch();
        watch.start();

        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            while (oneHourIterator.hasNext()) net.fit(oneHourIterator.next()); // fit model using mini-batch data
            oneHourIterator.reset(); // reset iterator
            net.rnnClearPreviousState(); // clear previous state
        }
        watch.stop();

        log.info("Saving model...");
        File locationToSave = new File("/Users/dfleck/projects/tcc/fleckbot-11-09-2017/fleckbot/src/main/resources/StockPriceLSTM_".concat(String.valueOf(category)).concat(".zip"));
        // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future
        ModelSerializer.writeModel(net, locationToSave, true);

        log.info("Load model...");
        net = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("Testing...");
        if (category.equals(PriceCategory.ALL)) {
            INDArray max = Nd4j.create(oneHourIterator.getMaxArray());
            INDArray min = Nd4j.create(oneHourIterator.getMinArray());
            predictAllCategories(net, test, max, min);
            log.info("Done...");
            System.out.println("Time Elapsed: " + (watch.getTime()));
            return null;
        } else {
            double max = oneHourIterator.getMaxNum(category);
            double min = oneHourIterator.getMinNum(category);
            List<String> dataPointsList = predictPriceOneAhead(net, test, max, min, testTimeSeries);
            log.info("Done...");
            System.out.println("Time Elapsed: " + watch.getTime());
            return dataPointsList;
        }
    }

    private OneHourStockDataSetIterator getIterator(String period) {

        if (period.equals("1 minuto")){
            return null;
        }
        if (period.equals("5 minutos")) {
            return null;
        }
        if (period.equals("10 minutos")){
            return null;
        }
        if (period.equals("15 minutos")){
            return null;
        }
        if (period.equals("30 minutos")){
            return null;
        }
        if (period.equals("1 hora")){
            return OneHourStockDataSetIterator.getInstance();
        }
        if (period.equals("2 horas")){
            return null;
        }
        if (period.equals("3 horas")){
            return null;
        }
        if (period.equals("4 horas")){
            return null;
        }
        if (period.equals("1 dia")){
            return null;
        }
        if (period.equals("1 semana")){
            return null;
        }
        if (period.equals("1 mês")){
            return null;
        }
        return null;
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
    public List<String> predictPriceOneAhead (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, double max, double min, TimeSeries testTimeSeries) {
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
        List<String> dataPointList = PlotUtil.plot(predicts, actuals, testTimeSeries);

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

    public DataPointsListModel prepareDataPointToBeSaved(List<String> dataPointList, String nomeDoConjunto) {

        if (dataPointList.size() > 0 && dataPointList.get(0) != null) {
            
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
                if (nomeDoConjunto != null){
                    dataPointsModel.setNomeConjunto(nomeDoConjunto);
                }

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
                if (nomeDoConjunto != null){
                    dataPointsModel.setNomeConjunto(nomeDoConjunto);
                }
                dataPointsModel.setX(Double.valueOf(xActualToken.get(i)));

                if (yActualToken.get(i) != null) {
                    dataPointsModel.setY(Double.valueOf(yActualToken.get(i)));
                }
                actualDataPointsModelListXY.add(dataPointsModel);

            }

            DataPointsListModel dataPointsListModel = new DataPointsListModel();
            if (nomeDoConjunto != null){
                dataPointsListModel.setName(nomeDoConjunto);
            }
            dataPointsListModel.setActualDataPointsModelList(actualDataPointsModelListXY);
            dataPointsListModel.setPredictDataPointsModelList(predictsDataPointsModelListXY);
            return dataPointsListModel;
        }else {
            System.out.println("Unable to get any results... dataPointList.size()=" + dataPointList.size());
        }
        return null;
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

