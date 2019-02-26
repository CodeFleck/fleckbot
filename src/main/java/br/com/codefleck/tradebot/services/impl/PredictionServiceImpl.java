package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.daos.DataPointsListResultSetlDao;
import br.com.codefleck.tradebot.daos.DataPointsModelDao;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.*;
import br.com.codefleck.tradebot.redesneurais.PlotUtil;
import br.com.codefleck.tradebot.redesneurais.predictors.*;
import br.com.codefleck.tradebot.tradingInterfaces.Ticker;
import javafx.util.Pair;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("predictionService")
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class PredictionServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    @Autowired
    DataPointsModelDao dataPointsModelDao;
    @Autowired
    StockDataDao stockDataDao;
    @Autowired
    DataPointsListResultSetlDao dataPointsListResultSetlDao;
    @Autowired
    OneMinutePredictor oneMinutePredictor;
    @Autowired
    FiveMinutesPredictor fiveMinutesPredictor;
    @Autowired
    TenMinutesPredictor tenMinutesPredictor;
    @Autowired
    FifteenMinutesPredictor fifteenMinutesPredictor;
    @Autowired
    ThirtyMinutesPredictor thirtyMinutesPredictor;
    @Autowired
    OneHourPredictor oneHourPredictor;
    @Autowired
    TwoHoursPredictor twoHoursPredictor;
    @Autowired
    ThreeHoursPredictor threeHoursPredictor;
    @Autowired
    FourHoursPredictor fourHoursPredictor;
    @Autowired
    OneDayPredictor oneDayPredictor;
    @Autowired
    OneWeekPredictor oneWeekPredictor;
    @Autowired
    OneMonthPredictor oneMonthPredictor;

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
    //private static int exampleLength = 180; //1D gerou 36

    public List<String> initTraining(List<StockData> stockDataList, int epocas, String categoria, String period) throws IOException {

        double learningRate = 0.01;
        int batchSize = 32; // mini-batch size
        double splitRatio = 0.8; // 80% for training, 20% for testing
        String chosenCategory = categoria;

        log.info("Creating dataSet iterators...");
        PriceCategory category = verifyCategory(chosenCategory);

        log.info("Loading test datasets...");
        List<String> dataPointsList = new ArrayList<>();

        switch (period) {
            case "1 minuto":
                dataPointsList = oneMinutePredictor.predictOneMinute(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "5 minutos":
                dataPointsList = fiveMinutesPredictor.predictFiveMinutes(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "10 minutos":
                dataPointsList = tenMinutesPredictor.predictTenMinutes(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "15 minutos":
                dataPointsList = fifteenMinutesPredictor.predictFifteenMinutes(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "30 minutos":
                dataPointsList = thirtyMinutesPredictor.predictThirtyMinutes(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "1 hora":
                dataPointsList = oneHourPredictor.predictOneHour(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "2 horas":
                dataPointsList = twoHoursPredictor.predictTwoHours(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "3 horas":
                dataPointsList = threeHoursPredictor.predictThreeHours(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "4 horas":
                dataPointsList = fourHoursPredictor.predictFourHours(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "1 dia":
                dataPointsList = oneDayPredictor.predictOneDay(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "1 semana":
                dataPointsList = oneWeekPredictor.predictOneWeek(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
            case "1 mês":
                dataPointsList = oneMonthPredictor.predictOneMonth(stockDataList, period, batchSize, splitRatio, category, epocas, learningRate);
                break;
        }

        return dataPointsList;
    }

    public PriceCategory verifyCategory(String chosenCategory) {

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
    public List<String> predictPriceOneAhead (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, double max, double min, int exampleLength) {
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
        return PlotUtil.plot(predicts, actuals);
    }

    /** Predict all the features (open, close, low, high prices and volume) of a stock one-day ahead */
    public void predictAllCategories (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, INDArray max, INDArray min, int exampleLength) {
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
        }
    }

    public BaseTimeSeries createCustomBaseTimeSeriesForNeuralNets(Date beginDate, Date endDate, String period) {
        CsvBarsLoader csvBarsLoader = new CsvBarsLoader();
        return csvBarsLoader.createCustomBaseTimeSeriesForTrainingNeuralNets(beginDate, endDate, period);
    }

    public DataPointsListResultSet transformStringResultsIntoDataPointResultSet(List<String> dataPointList){

        if (dataPointList.size() > 0 && dataPointList.get(0) != null) {
            //predicts (dataPointList.get(0))
            String predictsDataPointsArray[] = dataPointList.get(0).replace("[", "").replace("{", "").replace("}", "").replaceAll("\"\"", "").split(",");

            DataPointsListResultSet dataPointsListResultSet = new DataPointsListResultSet();
            dataPointsListResultSet.setNomeConjunto("forecast");
            List<DataPointsModel> predictsDataPointsModelListXY = new ArrayList<>();

            for (String value : predictsDataPointsArray) {

                DataPointsModel dataPoints = new DataPointsModel();
                dataPoints.setNomeConjunto("forecast");

                dataPoints.setLocalDateTime(LocalDateTime.now());
                if (value != null) {
                    if (value.substring(1, 2).equals("x")) {
                        dataPoints.setX(Double.valueOf(value.replaceAll("\"", "").replace("x:", "").replace("]", "")));
                    } else if (value.substring(1, 2).equals("y")) {
                        dataPoints.setY(Double.valueOf(value.replaceAll("\"", "").replace("y:", "").replace("]", "")));
                    }
                    dataPoints.setDataPointModelistType(DataPointModelistType.PREDICTDATAPOINTSMODELLIST);
                    predictsDataPointsModelListXY.add(dataPoints); //verificar se dataPoints tem id!!!!
                } else {
                    log.info("Couldn't get any results, probably need more data");
                }
            }

            dataPointsListResultSet.setPredictDataPointsModelList(predictsDataPointsModelListXY);

            //actuals = (dataPointList.get(1))
            String actualsDataPointsArray[] = dataPointList.get(1).replace("[", "").replace("{", "").replace("}", "").replaceAll("\"\"", "").split(",");

            List<DataPointsModel> actualDataPointsModelListXY = new ArrayList<>();

            for (String value : actualsDataPointsArray) {

                DataPointsModel dataPoints = new DataPointsModel();
                dataPoints.setNomeConjunto("forecast");

                dataPoints.setLocalDateTime(LocalDateTime.now());

                if (value.substring(1, 2).equals("x")) {
                    dataPoints.setX(Double.valueOf(value.replaceAll("\"", "").replace("x:", "").replace("]", "")));
                } else if (value.substring(1, 2).equals("y")) {
                    dataPoints.setY(Double.valueOf(value.replaceAll("\"", "").replace("y:", "").replace("]", "")));
                }
                dataPoints.setDataPointModelistType(DataPointModelistType.ACTUALDATAPOINTSMODELLIST);
                actualDataPointsModelListXY.add(dataPoints);
            }

            dataPointsListResultSet.setActualDataPointsModelList(actualDataPointsModelListXY);

            return dataPointsListResultSet;
        } else {
            log.info("Couldn't find any results...");
            return null;
        }
    }

    public DataPointsListResultSet prepareDataPointToBeSaved(List<String> dataPointList, String nomeDoConjunto, BaseTimeSeries customTimeSeries) {

        if (dataPointList.size() > 0 && dataPointList.get(0) != null) {
            //predicts (dataPointList.get(0))
            String predictsDataPointsArray[] = dataPointList.get(0).replace("[", "").replace("{", "").replace("}", "").replaceAll("\"\"", "").split(",");

            DataPointsListResultSet dataPointsListResultSet = new DataPointsListResultSet();
            dataPointsListResultSet.setNomeConjunto(nomeDoConjunto);
            List<DataPointsModel> predictsDataPointsModelListXY = new ArrayList<>();

            int predictIndex = 0;
            for (String value : predictsDataPointsArray) {

                DataPointsModel dataPoints = new DataPointsModel();
                dataPoints.setNomeConjunto(nomeDoConjunto);

                String simpleDateName = customTimeSeries.getBar(predictIndex).getSimpleDateName();
                simpleDateName.replaceAll("39", "20");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); //Verificar se a data está sendo formatada corretamente no banco
                dataPoints.setLocalDateTime(LocalDateTime.parse(simpleDateName, formatter));
                predictIndex++;
                if (value != null) {
                    if (value.substring(1, 2).equals("x")) {
                        dataPoints.setX(Double.valueOf(value.replaceAll("\"", "").replace("x:", "").replace("]", "")));
                    } else if (value.substring(1, 2).equals("y")) {
                        dataPoints.setY(Double.valueOf(value.replaceAll("\"", "").replace("y:", "").replace("]", "")));
                    }
                    dataPoints.setDataPointModelistType(DataPointModelistType.PREDICTDATAPOINTSMODELLIST);
                    dataPointsModelDao.save(dataPoints);
                    predictsDataPointsModelListXY.add(dataPoints); //verificar se dataPoints tem id!!!!
                } else {
                    log.info("Couldn't get any results, probably need more data");
                }
            }

            dataPointsListResultSet.setPredictDataPointsModelList(predictsDataPointsModelListXY);

            //actuals = (dataPointList.get(1))
            String actualsDataPointsArray[] = dataPointList.get(1).replace("[", "").replace("{", "").replace("}", "").replaceAll("\"\"", "").split(",");

            List<DataPointsModel> actualDataPointsModelListXY = new ArrayList<>();

            int actualIndex = 0;
            for (String value : actualsDataPointsArray) {

                DataPointsModel dataPoints = new DataPointsModel();
                dataPoints.setNomeConjunto(nomeDoConjunto);

                String simpleDateName = customTimeSeries.getBar(actualIndex).getSimpleDateName();
                simpleDateName.replaceAll("39", "20");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //Verificar se a data está sendo formatada corretamente no banco
                dataPoints.setLocalDateTime(LocalDateTime.parse(simpleDateName, formatter));
                actualIndex++;

                if (value.substring(1, 2).equals("x")) {
                    dataPoints.setX(Double.valueOf(value.replaceAll("\"", "").replace("x:", "").replace("]", "")));
                } else if (value.substring(1, 2).equals("y")) {
                    dataPoints.setY(Double.valueOf(value.replaceAll("\"", "").replace("y:", "").replace("]", "")));
                }
                dataPoints.setDataPointModelistType(DataPointModelistType.ACTUALDATAPOINTSMODELLIST);
                dataPointsModelDao.save(dataPoints);
                actualDataPointsModelListXY.add(dataPoints);
            }

            dataPointsListResultSet.setActualDataPointsModelList(actualDataPointsModelListXY);
            dataPointsListResultSetlDao.save(dataPointsListResultSet);

            return dataPointsListResultSet;
        } else {
            log.info("Couldn't find any results...");
            return null;
        }
    }

    public Double calculateErrorPercentageAverage(DataPointsListResultSet dataPointsList) {

        List<Double> errorPercentage = new ArrayList<>();
        Double result;

        for (int i=0;i<dataPointsList.getActualDataPointsModelList().size();i++){

            result = ((dataPointsList.getActualDataPointsModelList().get(i).getY()-dataPointsList.getPredictDataPointsModelList().get(i).getY())*100)/dataPointsList.getActualDataPointsModelList().get(i).getY();

            errorPercentage.add(result);

        }

        double errorPercentageAverage = 0.00;
        for (int i=0; i<errorPercentage.size();i++){
            errorPercentageAverage += errorPercentage.get(i).doubleValue();
        }

        errorPercentageAverage = errorPercentageAverage/errorPercentage.size();

        return errorPercentageAverage;
    }

    public Double calculateErrorPercentageLastDay(DataPointsListResultSet dataPointsListResultSet) {

        List<Double> errorPercentage = new ArrayList<>();
        Double result;

        int lastResult = dataPointsListResultSet.getActualDataPointsModelList().size();
        lastResult--;

        result = ((dataPointsListResultSet.getActualDataPointsModelList().get(lastResult).getY()-dataPointsListResultSet.getPredictDataPointsModelList().get(lastResult).getY())*100)/dataPointsListResultSet.getActualDataPointsModelList().get(lastResult).getY();

        errorPercentage.add(result);

        return result;
    }

    public Double calculateMajorError(DataPointsListResultSet dataPointsListResultSet) {

        Double majorError = ((dataPointsListResultSet.getActualDataPointsModelList().get(0).getY() - dataPointsListResultSet.getPredictDataPointsModelList().get(0).getY()) * 100) / dataPointsListResultSet.getActualDataPointsModelList().get(0).getY();
        Double result;
        for (int i=0;i<dataPointsListResultSet.getActualDataPointsModelList().size();i++) {

            result = ((dataPointsListResultSet.getActualDataPointsModelList().get(i).getY() - dataPointsListResultSet.getPredictDataPointsModelList().get(i).getY()) * 100) / dataPointsListResultSet.getActualDataPointsModelList().get(i).getY();

            if (result > majorError) {
                majorError = result;
            }
        }
        return majorError;
    }

    public Double calculateMinorError(DataPointsListResultSet dataPointsListResultSet) {

        Double minorError = ((dataPointsListResultSet.getActualDataPointsModelList().get(0).getY() - dataPointsListResultSet.getPredictDataPointsModelList().get(0).getY()) * 100) / dataPointsListResultSet.getActualDataPointsModelList().get(0).getY();

        Double result;

        for (int i=0;i<dataPointsListResultSet.getActualDataPointsModelList().size();i++) {

            result = ((dataPointsListResultSet.getActualDataPointsModelList().get(i).getY() - dataPointsListResultSet.getPredictDataPointsModelList().get(i).getY()) * 100) / dataPointsListResultSet.getActualDataPointsModelList().get(i).getY();

            if (result < minorError) {
                minorError = result;
            }
        }

        return minorError;
    }

    public StockData transformTickerInStockData(Ticker ticker) {

        if(!(ticker.getOpen() == null)) {
            ticker.getOpen().doubleValue();
        } else {
            ticker.setOpen(ticker.getBid());
        }

        String simpleDateName = ticker.getTimestamp().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(simpleDateName, formatter);


        StockData stockData = new StockData(
                dateTime,
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

                String simpleDateName = barList.get(i).getDateName().substring(0,19);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(simpleDateName, formatter);

                StockData stockData = new StockData(
                        dateTime,
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

