package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.daos.DataPointsDao;
import br.com.codefleck.tradebot.models.*;
import br.com.codefleck.tradebot.redesneurais.iterators.*;
import br.com.codefleck.tradebot.redesneurais.predictors.*;
import br.com.codefleck.tradebot.tradingInterfaces.Ticker;
import javafx.util.Pair;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("predictionService")
@ComponentScan(basePackages = {"br.com.codefleck.tradebot.repository"})
public class PredictionServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

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
    @Autowired
    DataPointsDao dataPointsDao;

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

    public DataPointsListResultSet initTraining(int epocas, String simbolo, String categoria, String period, String nomeDoConjunto) throws IOException, InterruptedException {

        double learningRate = 0.5;
        int batchSize = 32; // mini-batch size
        double splitRatio = 0.8; // 80% for training, 10% for testing
        String chosenCategory = categoria;

        log.info("Creating dataSet iterators...");
        PriceCategory category = verifyCategory(chosenCategory);

        log.info("Loading test datasets...");
        DataPointsListResultSet resultSet = new DataPointsListResultSet();

        switch (period) {
            case "1 minuto":
                resultSet = oneMinutePredictor.predictOneMinute(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "5 minutos":
                resultSet = fiveMinutesPredictor.predictFiveMinutes(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "10 minutos":
                resultSet = tenMinutesPredictor.predictTenMinutes(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "15 minutos":
                resultSet = fifteenMinutesPredictor.predictFifteenMinutes(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "30 minutos":
                resultSet = thirtyMinutesPredictor.predictThirtyMinutes(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "1 hora":
                resultSet = oneHourPredictor.predictOneHour(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "2 horas":
                resultSet = twoHoursPredictor.predictTwoHours(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "3 horas":
                resultSet = threeHoursPredictor.predictThreeHours(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "4 horas":
                resultSet = fourHoursPredictor.predictFourHours(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "1 dia":
                resultSet = oneDayPredictor.predictOneDay(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "1 semana":
                resultSet = oneWeekPredictor.predictOneWeek(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
            case "1 mes":
                resultSet = oneMonthPredictor.predictOneMonth(simbolo, period, batchSize, splitRatio, category, epocas, learningRate, nomeDoConjunto);
                break;
        }

        return resultSet;
    }

    public String getCSVFilePathForTrainingNeuralNets(String period) throws IOException, InterruptedException {

        if (period.equals("1 minuto")) {
            return System.getProperty("user.home") + "/csv/OneMinuteDataForTrainingNeuralNets.csv";
        }
        if (period.equals("5 minutos")) {
            return System.getProperty("user.home") + "/csv/FiveMinutesDataForTrainingNeuralNets.csv";
        }
        if (period.equals("10 minutos")){
            return System.getProperty("user.home") + "/csv/TenMinutesDataForTrainingNeuralNets.csv";
        }
        if (period.equals("15 minutos")){
            return System.getProperty("user.home") + "/csv/FifteenMinutesDataForTrainingNeuralNets.csv";
        }
        if (period.equals("30 minutos")){
            return System.getProperty("user.home") + "/csv/ThirtyMinutesDataForTrainingNeuralNets.csv";
        }
        if (period.equals("1 hora")){
            return System.getProperty("user.home") + "/csv/OneHourDataForTrainingNeuralNets.csv";
        }
        if (period.equals("2 horas")) {
            return System.getProperty("user.home") + "/csv/TwoHoursDataForTrainingNeuralNets.csv";
        }
        if (period.equals("3 horas")){
            return System.getProperty("user.home") + "/csv/ThreeHoursDataForTrainingNeuralNets.csv";
        }
        if (period.equals("4 horas")){
            return System.getProperty("user.home") + "/csv/FourHoursDataForTrainingNeuralNets.csv";
        }
        if (period.equals("1 dia")){
            return System.getProperty("user.home") + "/csv/OneDayDataForTrainingNeuralNets.csv";
        }
        if (period.equals("1 semana")){
            return System.getProperty("user.home") + "/csv/OneWeekDataForTrainingNeuralNets.csv";
        }
        if (period.equals("1 mes")){
            return System.getProperty("user.home") + "/csv/OneMonthDataForTrainingNeuralNets.csv";
        }
        return null;
    }

    public OneMinuteStockDataSetIterator getOneMinuteStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        OneMinuteStockDataSetIterator oneMinuteStockDataSetIterator = OneMinuteStockDataSetIterator.getInstance();
        List<StockData> stockDataList = oneMinuteStockDataSetIterator.readStockDataFromFile(filePath, simbolo);
        oneMinuteStockDataSetIterator.setFullStockDataList(stockDataList);
        oneMinuteStockDataSetIterator.setMiniBatchSize(batchSize);
        oneMinuteStockDataSetIterator.setExampleLength(80);
        oneMinuteStockDataSetIterator.setCategory(category);
        oneMinuteStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneMinuteStockDataSetIterator.setTrain(stockDataList.subList(0, oneMinuteStockDataSetIterator.getSplit()));
        oneMinuteStockDataSetIterator.setTest(oneMinuteStockDataSetIterator.generateTestDataSet(stockDataList.subList(oneMinuteStockDataSetIterator.getSplit(), stockDataList.size())));
        oneMinuteStockDataSetIterator.initializeOffsets();
        return oneMinuteStockDataSetIterator;
    }

    public FiveMinutesStockDataSetIterator getFiveMinutesStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        FiveMinutesStockDataSetIterator fiveMinutesStockDataSetIterator = FiveMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = fiveMinutesStockDataSetIterator.readStockDataFromFile(filePath, simbolo);
        fiveMinutesStockDataSetIterator.setFullStockDataList(stockDataList);
        fiveMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        fiveMinutesStockDataSetIterator.setExampleLength(80);
        fiveMinutesStockDataSetIterator.setCategory(category);
        fiveMinutesStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        fiveMinutesStockDataSetIterator.setTrain(stockDataList.subList(0, fiveMinutesStockDataSetIterator.getSplit()));
        fiveMinutesStockDataSetIterator.setTest(fiveMinutesStockDataSetIterator.generateTestDataSet(stockDataList.subList(fiveMinutesStockDataSetIterator.getSplit(), stockDataList.size())));
        fiveMinutesStockDataSetIterator.initializeOffsets();
        return fiveMinutesStockDataSetIterator;
    }

    public TenMinutesStockDataSetIterator getTenMinutesStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        TenMinutesStockDataSetIterator tenMinutesStockDataSetIterator = TenMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = tenMinutesStockDataSetIterator.readStockDataFromFile(filePath, simbolo);
        tenMinutesStockDataSetIterator.setFullStockDataList(stockDataList);
        tenMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        tenMinutesStockDataSetIterator.setExampleLength(80);
        tenMinutesStockDataSetIterator.setCategory(category);
        tenMinutesStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        tenMinutesStockDataSetIterator.setTrain(stockDataList.subList(0, tenMinutesStockDataSetIterator.getSplit()));
        tenMinutesStockDataSetIterator.setTest(tenMinutesStockDataSetIterator.generateTestDataSet(stockDataList.subList(tenMinutesStockDataSetIterator.getSplit(), stockDataList.size())));
        tenMinutesStockDataSetIterator.initializeOffsets();
        return tenMinutesStockDataSetIterator;
    }

    public FifteenMinutesStockDataSetIterator getFifteenMinutesStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        FifteenMinutesStockDataSetIterator fifteenMinutesStockDataSetIterator = FifteenMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = fifteenMinutesStockDataSetIterator.readStockDataFromFile(filePath, simbolo);
        fifteenMinutesStockDataSetIterator.setFullStockDataList(stockDataList);
        fifteenMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        fifteenMinutesStockDataSetIterator.setExampleLength(80);
        fifteenMinutesStockDataSetIterator.setCategory(category);
        fifteenMinutesStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        fifteenMinutesStockDataSetIterator.setTrain(stockDataList.subList(0, fifteenMinutesStockDataSetIterator.getSplit()));
        fifteenMinutesStockDataSetIterator.setTest(fifteenMinutesStockDataSetIterator.generateTestDataSet(stockDataList.subList(fifteenMinutesStockDataSetIterator.getSplit(), stockDataList.size())));
        fifteenMinutesStockDataSetIterator.initializeOffsets();
        return fifteenMinutesStockDataSetIterator;
    }

    public ThirtyMinutesStockDataSetIterator getThirtyMinutesStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        ThirtyMinutesStockDataSetIterator thirtyMinutesIterator = ThirtyMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = thirtyMinutesIterator.readStockDataFromFile(filePath, simbolo);
        thirtyMinutesIterator.setFullStockDataList(stockDataList);
        thirtyMinutesIterator.setMiniBatchSize(batchSize);
        thirtyMinutesIterator.setExampleLength(80);
        thirtyMinutesIterator.setCategory(category);
        thirtyMinutesIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        thirtyMinutesIterator.setTrain(stockDataList.subList(0, thirtyMinutesIterator.getSplit()));
        thirtyMinutesIterator.setTest(thirtyMinutesIterator.generateTestDataSet(stockDataList.subList(thirtyMinutesIterator.getSplit(), stockDataList.size())));
        thirtyMinutesIterator.initializeOffsets();
        return thirtyMinutesIterator;
    }

    public OneHourStockDataSetIterator getOneHourStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        OneHourStockDataSetIterator oneHourIterator = OneHourStockDataSetIterator.getInstance();
        List<StockData> stockDataList = oneHourIterator.readStockDataFromFile(filePath, simbolo);
        oneHourIterator.setFullStockDataList(stockDataList);
        oneHourIterator.setMiniBatchSize(batchSize);
        oneHourIterator.setExampleLength(50);
        oneHourIterator.setCategory(category);
        oneHourIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneHourIterator.setTrain(stockDataList.subList(0, oneHourIterator.getSplit()));
        oneHourIterator.setTest(oneHourIterator.generateTestDataSet(stockDataList.subList(oneHourIterator.getSplit(), stockDataList.size())));
        oneHourIterator.initializeOffsets();
        return oneHourIterator;
    }

    public TwoHoursStockDataSetIterator getTwoHoursStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        TwoHoursStockDataSetIterator twoHoursIterator = TwoHoursStockDataSetIterator.getInstance();
        List<StockData> stockDataList = twoHoursIterator.readStockDataFromFile(filePath, simbolo);
        twoHoursIterator.setFullStockDataList(stockDataList);
        twoHoursIterator.setMiniBatchSize(batchSize);
        twoHoursIterator.setExampleLength(50);
        twoHoursIterator.setCategory(category);
        twoHoursIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        twoHoursIterator.setTrain(stockDataList.subList(0, twoHoursIterator.getSplit()));
        twoHoursIterator.setTest(twoHoursIterator.generateTestDataSet(stockDataList.subList(twoHoursIterator.getSplit(), stockDataList.size())));
        twoHoursIterator.initializeOffsets();
        return twoHoursIterator;
    }

    public ThreeHoursStockDataSetIterator getThreeHoursStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        ThreeHoursStockDataSetIterator threeHoursIterator = ThreeHoursStockDataSetIterator.getInstance();
        List<StockData> stockDataList = threeHoursIterator.readStockDataFromFile(filePath, simbolo);
        threeHoursIterator.setFullStockDataList(stockDataList);
        threeHoursIterator.setMiniBatchSize(batchSize);
        threeHoursIterator.setExampleLength(50);
        threeHoursIterator.setCategory(category);
        threeHoursIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        threeHoursIterator.setTrain(stockDataList.subList(0, threeHoursIterator.getSplit()));
        threeHoursIterator.setTest(threeHoursIterator.generateTestDataSet(stockDataList.subList(threeHoursIterator.getSplit(), stockDataList.size())));
        threeHoursIterator.initializeOffsets();
        return threeHoursIterator;
    }

    public FourHoursStockDataSetIterator getFourHoursStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        FourHoursStockDataSetIterator fourHoursIterator = FourHoursStockDataSetIterator.getInstance();
        List<StockData> stockDataList = fourHoursIterator.readStockDataFromFile(filePath, simbolo);
        fourHoursIterator.setFullStockDataList(stockDataList);
        fourHoursIterator.setMiniBatchSize(batchSize);
        fourHoursIterator.setExampleLength(40);
        fourHoursIterator.setCategory(category);
        fourHoursIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        fourHoursIterator.setTrain(stockDataList.subList(0, fourHoursIterator.getSplit()));
        fourHoursIterator.setTest(fourHoursIterator.generateTestDataSet(stockDataList.subList(fourHoursIterator.getSplit(), stockDataList.size())));
        fourHoursIterator.initializeOffsets();
        return fourHoursIterator;
    }

    public OneDayStockDataSetIterator getOneDayStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        OneDayStockDataSetIterator oneDayIterator = OneDayStockDataSetIterator.getInstance();
        List<StockData> stockDataList = oneDayIterator.readStockDataFromFile(filePath, simbolo);
        oneDayIterator.setFullStockDataList(stockDataList);
        oneDayIterator.setMiniBatchSize(batchSize);
        oneDayIterator.setExampleLength(40);
        oneDayIterator.setCategory(category);
        oneDayIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneDayIterator.setTrain(stockDataList.subList(0, oneDayIterator.getSplit()));
        oneDayIterator.setTest(oneDayIterator.generateTestDataSet(stockDataList.subList(oneDayIterator.getSplit(), stockDataList.size())));
        oneDayIterator.initializeOffsets();
        return oneDayIterator;
    }

    public OneWeekStockDataSetIterator getOneWeekStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        OneWeekStockDataSetIterator oneWeekIterator = OneWeekStockDataSetIterator.getInstance();
        List<StockData> stockDataList = duplicate(oneWeekIterator.readStockDataFromFile(filePath, simbolo));
        oneWeekIterator.setMiniBatchSize(batchSize);
        oneWeekIterator.setExampleLength(30);
        oneWeekIterator.setCategory(category);
        oneWeekIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneWeekIterator.setTrain(stockDataList.subList(0, oneWeekIterator.getSplit()));
        oneWeekIterator.setTest(oneWeekIterator.generateTestDataSet(stockDataList.subList(oneWeekIterator.getSplit(), stockDataList.size())));
        oneWeekIterator.initializeOffsets();
        return oneWeekIterator;
    }

    public OneMonthStockDataSetIterator getOneMonthStockDataSetIterator(String simbolo, String filePath, int batchSize, double splitRatio, PriceCategory category) {
        OneMonthStockDataSetIterator oneMonthIterator = OneMonthStockDataSetIterator.getInstance();
        List<StockData> stockDataList = duplicate(oneMonthIterator.readStockDataFromFile(filePath, simbolo));
        oneMonthIterator.setMiniBatchSize(batchSize);
        oneMonthIterator.setExampleLength(30);
        oneMonthIterator.setCategory(category);
        oneMonthIterator.setTrain(stockDataList);
        oneMonthIterator.setTest(oneMonthIterator.generateTestDataSet(stockDataList));
        oneMonthIterator.initializeOffsets();
        return oneMonthIterator;
    }

    private List<StockData> duplicate(List<StockData> stockDataList) {
        List<StockData> duplicatedStockDataList = new ArrayList<>();
        duplicatedStockDataList.addAll(stockDataList);
        duplicatedStockDataList.addAll(stockDataList);
        duplicatedStockDataList.addAll(stockDataList);
        duplicatedStockDataList.addAll(stockDataList);
        duplicatedStockDataList.addAll(stockDataList);
        return duplicatedStockDataList;
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
    public DataPointsListResultSet predictPriceOneAhead (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, double max, double min, int exampleLength, String nomeDoConjunto, GeneralStockDataSetIterator iterator) {
        double[] predicts = new double[testData.size()];
        double[] actuals = new double[testData.size()];

        DataPointsListResultSet resultSet = new DataPointsListResultSet();
        List<DataPoints> predictDataPointsList = new ArrayList<>();
        List<DataPoints> actualDataPointsList = new ArrayList<>();
        resultSet.setPredictDataPointsList(predictDataPointsList);
        resultSet.setActualDataPointsList(actualDataPointsList);
        resultSet.setNomeConjunto(nomeDoConjunto);

        for (int i = 0; i < testData.size(); i++) {
            predicts[i] = net.rnnTimeStep(testData.get(i).getKey()).getDouble(exampleLength - 1) * (max - min) + min;
            actuals[i] = testData.get(i).getValue().getDouble(0);

            DataPoints predictDataPoint = new DataPoints();
            predictDataPoint.setNomeConjunto(nomeDoConjunto);
            predictDataPoint.setDataPointsListType(DataPointsListType.PREDICTDATAPOINTSLIST);
            predictDataPoint.setLocalDateTime(iterator.getFullStockDataList().get(i).getDate().toLocalDateTime());
            predictDataPoint.setY(predicts[i]);
            predictDataPoint.setX(i);
            dataPointsDao.save(predictDataPoint);
            predictDataPointsList.add(predictDataPoint);

            DataPoints actuaDataPoint = new DataPoints();
            actuaDataPoint.setNomeConjunto(nomeDoConjunto);
            actuaDataPoint.setDataPointsListType(DataPointsListType.ACTUALDATAPOINTSLIST);
            actuaDataPoint.setLocalDateTime(iterator.getFullStockDataList().get(i).getDate().toLocalDateTime());
            actuaDataPoint.setY(actuals[i]);
            actuaDataPoint.setX(i);
            dataPointsDao.save(actuaDataPoint);
            actualDataPointsList.add(actuaDataPoint);
        }

        return resultSet;
    }

    public DataPointsListResultSet forecastPriceOneAhead (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, double max, double min, int exampleLength) {
        double[] predicts = new double[testData.size()];
        double[] actuals = new double[testData.size()];
        String nomeDoConjunto = "forecast";

        DataPointsListResultSet resultSet = new DataPointsListResultSet();
        resultSet.setNomeConjunto(nomeDoConjunto);

        List<DataPoints> predictDataPointsList = new ArrayList<>();
        List<DataPoints> actualDataPointsList = new ArrayList<>();

        for (int i = 0; i < testData.size(); i++) {
            predicts[i] = net.rnnTimeStep(testData.get(i).getKey()).getDouble(exampleLength - 1) * (max - min) + min;
            actuals[i] = testData.get(i).getValue().getDouble(0);

            DataPoints predictDataPoint = new DataPoints();
            predictDataPoint.setNomeConjunto(nomeDoConjunto);
            predictDataPoint.setDataPointsListType(DataPointsListType.PREDICTDATAPOINTSLIST);
            predictDataPoint.setLocalDateTime(LocalDateTime.now());
            predictDataPoint.setY(predicts[i]);
            predictDataPoint.setX(i);
            predictDataPointsList.add(predictDataPoint);

            DataPoints actualDataPoint = new DataPoints();
            actualDataPoint.setNomeConjunto(nomeDoConjunto);
            actualDataPoint.setDataPointsListType(DataPointsListType.ACTUALDATAPOINTSLIST);
            actualDataPoint.setLocalDateTime(LocalDateTime.now());
            actualDataPoint.setY(actuals[i]);
            actualDataPoint.setX(i);
            actualDataPointsList.add(actualDataPoint);
        }

        resultSet.setPredictDataPointsList(predictDataPointsList);
        resultSet.setActualDataPointsList(actualDataPointsList);

        return resultSet;
    }

    /** Predict all the features (open, close, low, high prices and volume) of a stock one-day ahead */
    public void predictAllCategories (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, INDArray max, INDArray min, int exampleLength) {
        INDArray[] predicts = new INDArray[testData.size()];
        INDArray[] actuals = new INDArray[testData.size()];
        for (int i = 0; i < testData.size(); i++) {
            predicts[i] = net.rnnTimeStep(testData.get(i).getKey()).getRow(exampleLength - 1).mul(max.sub(min)).add(min);
            actuals[i] = testData.get(i).getValue();
        }

        for (int n = 0; n < 5; n++) {
            double[] pred = new double[predicts.length];
            double[] actu = new double[actuals.length];

            for (int i = 0; i < predicts.length; i++) {
                pred[i] = predicts[i].getDouble(n);
                actu[i] = actuals[i].getDouble(n);
            }
        }
    }

    public BaseTimeSeries createCSVFileForNeuralNets(Date beginDate, Date endDate, String period) {
        CsvBarsLoader csvBarsLoader = new CsvBarsLoader();
        return csvBarsLoader.createCSVFileForNeuralNets(beginDate, endDate, period);
    }

    public Double calculateErrorPercentageAverage(DataPointsListResultSet dataPointsList) {

        List<Double> errorPercentage = new ArrayList<>();
        Double result;

        for (int i = 0; i<dataPointsList.getActualDataPointsList().size(); i++){
            result = ((dataPointsList.getActualDataPointsList().get(i).getY()-dataPointsList.getPredictDataPointsList().get(i).getY())*100)/dataPointsList.getActualDataPointsList().get(i).getY();
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

        int lastResult = dataPointsListResultSet.getActualDataPointsList().size();
        lastResult--;

        result = ((dataPointsListResultSet.getActualDataPointsList().get(lastResult).getY()-dataPointsListResultSet.getPredictDataPointsList().get(lastResult).getY())*100)/dataPointsListResultSet.getActualDataPointsList().get(lastResult).getY();

        errorPercentage.add(result);

        return result;
    }

    public Double calculateMajorError(DataPointsListResultSet dataPointsListResultSet) {

        Double majorError = ((dataPointsListResultSet.getActualDataPointsList().get(0).getY() - dataPointsListResultSet.getPredictDataPointsList().get(0).getY()) * 100) / dataPointsListResultSet.getActualDataPointsList().get(0).getY();
        Double result;
        for (int i = 0; i<dataPointsListResultSet.getActualDataPointsList().size(); i++) {

            result = ((dataPointsListResultSet.getActualDataPointsList().get(i).getY() - dataPointsListResultSet.getPredictDataPointsList().get(i).getY()) * 100) / dataPointsListResultSet.getActualDataPointsList().get(i).getY();

            if (result > majorError) {
                majorError = result;
            }
        }
        return majorError;
    }

    public Double calculateMinorError(DataPointsListResultSet dataPointsListResultSet) {

        Double minorError = ((dataPointsListResultSet.getActualDataPointsList().get(0).getY() - dataPointsListResultSet.getPredictDataPointsList().get(0).getY()) * 100) / dataPointsListResultSet.getActualDataPointsList().get(0).getY();

        Double result;

        for (int i = 0; i<dataPointsListResultSet.getActualDataPointsList().size(); i++) {

            result = ((dataPointsListResultSet.getActualDataPointsList().get(i).getY() - dataPointsListResultSet.getPredictDataPointsList().get(i).getY()) * 100) / dataPointsListResultSet.getActualDataPointsList().get(i).getY();

            if (result < minorError) {
                minorError = result;
            }
        }

        return minorError;
    }

    public StockData transformTickerInStockData(Ticker ticker) {


        ZonedDateTime today = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Instant.now().toEpochMilli()), ZoneId.systemDefault());;

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

                StockData stockData = new StockData(
                        barList.get(i).getEndTime(),
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

