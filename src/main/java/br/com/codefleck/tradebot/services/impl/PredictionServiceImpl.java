package br.com.codefleck.tradebot.services.impl;

import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import br.com.codefleck.tradebot.models.DataPointsModel;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.PlotUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service("predictionService")
@Transactional
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
    private static int exampleLength = 50;

    public List<String> initTraining(int epocas, String simbolo, String categoria, String period, double learningRate) throws IOException {

        int batchSize = 32; // mini-batch size
        double splitRatio = 0.8; // 90% for training, 10% for testing
        int epochs = epocas;
        String chosenCategory = categoria;

        log.info("Creating dataSet iterators...");
        PriceCategory category = verifyCategory(chosenCategory);

        log.info("Loading test datasets...");
        List<String> dataPointsList = new ArrayList<>();

        switch (period) {
            case "1 minuto":
                dataPointsList = oneMinutePredictor.predictOneMinute(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "5 minutos":
                dataPointsList = fiveMinutesPredictor.predictFiveMinutes(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "10 minutos":
                dataPointsList = tenMinutesPredictor.predictTenMinutes(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "15 minutos":
                dataPointsList = fifteenMinutesPredictor.predictFifteenMinutes(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "30 minutos":
                dataPointsList = thirtyMinutesPredictor.predictThirtyMinutes(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "1 hora":
                dataPointsList = oneHourPredictor.predictOneHour(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "2 horas":
                dataPointsList = twoHoursPredictor.predictTwoHours(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "3 horas":
                dataPointsList = threeHoursPredictor.predictThreeHours(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "4 horas":
                dataPointsList = fourHoursPredictor.predictFourHours(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "1 dia":
                dataPointsList = oneDayPredictor.predictOneDay(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "1 semana":
                dataPointsList = oneWeekPredictor.predictOneWeek(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
            case "1 mês":
                dataPointsList = oneMonthPredictor.predictOneMonth(simbolo, period, batchSize, splitRatio, category, epochs, learningRate);
                break;
        }

        return dataPointsList;
    }

    public String getFileForTrainingNeuralNets(String period) throws IOException {

        if (period.equals("1 minuto")) {
            return new ClassPathResource("/oneminute/OneMinuteDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("5 minutos")) {
            return new ClassPathResource("/fiveminutes/FiveMinutesDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("10 minutos")){
            return new ClassPathResource("/tenminutes/TenMinutesDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("15 minutos")){
            return new ClassPathResource("/fifteenminutes/FifteenMinutesDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("30 minutos")){
            return new ClassPathResource("/thirtyminutes/ThirtyMinutesDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("1 hora")){
            return new ClassPathResource("/onehour/OneHourDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("2 horas")){
            return new ClassPathResource("/twohours/TwoHoursDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("3 horas")){
            return new ClassPathResource("/threehours/ThreeHoursDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("4 horas")){
            return new ClassPathResource("/fourhours/FourHoursDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("1 dia")){
            return new ClassPathResource("/oneday/OneDayDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("1 semana")){
            return new ClassPathResource("/oneweek/OneWeekDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        if (period.equals("1 mês")){
            return new ClassPathResource("/onemonth/OneMonthDataForTrainingNeuralNets.csv").getFile().getAbsolutePath();
        }
        return null;
    }

    public OneMinuteStockDataSetIterator getOneMinuteStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        OneMinuteStockDataSetIterator oneMinuteStockDataSetIterator = OneMinuteStockDataSetIterator.getInstance();
        List<StockData> stockDataList = oneMinuteStockDataSetIterator.readStockDataFromFile(file, simbolo);
        oneMinuteStockDataSetIterator.setMiniBatchSize(batchSize);
        oneMinuteStockDataSetIterator.setExampleLength(exampleLength);
        oneMinuteStockDataSetIterator.setCategory(category);
        oneMinuteStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneMinuteStockDataSetIterator.setTrain(stockDataList.subList(0, oneMinuteStockDataSetIterator.getSplit()));
        oneMinuteStockDataSetIterator.setTest(oneMinuteStockDataSetIterator.generateTestDataSet(stockDataList.subList(oneMinuteStockDataSetIterator.getSplit(), stockDataList.size())));
        oneMinuteStockDataSetIterator.initializeOffsets();
        return oneMinuteStockDataSetIterator;
    }

    public FiveMinutesStockDataSetIterator getFiveMinutesStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        FiveMinutesStockDataSetIterator fiveMinutesStockDataSetIterator = FiveMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = fiveMinutesStockDataSetIterator.readStockDataFromFile(file, simbolo);
        fiveMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        fiveMinutesStockDataSetIterator.setExampleLength(exampleLength);
        fiveMinutesStockDataSetIterator.setCategory(category);
        fiveMinutesStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        fiveMinutesStockDataSetIterator.setTrain(stockDataList.subList(0, fiveMinutesStockDataSetIterator.getSplit()));
        fiveMinutesStockDataSetIterator.setTest(fiveMinutesStockDataSetIterator.generateTestDataSet(stockDataList.subList(fiveMinutesStockDataSetIterator.getSplit(), stockDataList.size())));
        fiveMinutesStockDataSetIterator.initializeOffsets();
        return fiveMinutesStockDataSetIterator;
    }

    public TenMinutesStockDataSetIterator getTenMinutesStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        TenMinutesStockDataSetIterator tenMinutesStockDataSetIterator = TenMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = tenMinutesStockDataSetIterator.readStockDataFromFile(file, simbolo);
        tenMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        tenMinutesStockDataSetIterator.setExampleLength(exampleLength);
        tenMinutesStockDataSetIterator.setCategory(category);
        tenMinutesStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        tenMinutesStockDataSetIterator.setTrain(stockDataList.subList(0, tenMinutesStockDataSetIterator.getSplit()));
        tenMinutesStockDataSetIterator.setTest(tenMinutesStockDataSetIterator.generateTestDataSet(stockDataList.subList(tenMinutesStockDataSetIterator.getSplit(), stockDataList.size())));
        tenMinutesStockDataSetIterator.initializeOffsets();
        return tenMinutesStockDataSetIterator;
    }

    public FifteenMinutesStockDataSetIterator getFifteenMinutesStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        FifteenMinutesStockDataSetIterator fifteenMinutesStockDataSetIterator = FifteenMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = fifteenMinutesStockDataSetIterator.readStockDataFromFile(file, simbolo);
        fifteenMinutesStockDataSetIterator.setMiniBatchSize(batchSize);
        fifteenMinutesStockDataSetIterator.setExampleLength(exampleLength);
        fifteenMinutesStockDataSetIterator.setCategory(category);
        fifteenMinutesStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        fifteenMinutesStockDataSetIterator.setTrain(stockDataList.subList(0, fifteenMinutesStockDataSetIterator.getSplit()));
        fifteenMinutesStockDataSetIterator.setTest(fifteenMinutesStockDataSetIterator.generateTestDataSet(stockDataList.subList(fifteenMinutesStockDataSetIterator.getSplit(), stockDataList.size())));
        fifteenMinutesStockDataSetIterator.initializeOffsets();
        return fifteenMinutesStockDataSetIterator;
    }

    public ThirtyMinutesStockDataSetIterator getThirtyMinutesStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        ThirtyMinutesStockDataSetIterator thirtyMinutesIterator = ThirtyMinutesStockDataSetIterator.getInstance();
        List<StockData> stockDataList = thirtyMinutesIterator.readStockDataFromFile(file, simbolo);
        thirtyMinutesIterator.setMiniBatchSize(batchSize);
        thirtyMinutesIterator.setExampleLength(exampleLength);
        thirtyMinutesIterator.setCategory(category);
        thirtyMinutesIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        thirtyMinutesIterator.setTrain(stockDataList.subList(0, thirtyMinutesIterator.getSplit()));
        thirtyMinutesIterator.setTest(thirtyMinutesIterator.generateTestDataSet(stockDataList.subList(thirtyMinutesIterator.getSplit(), stockDataList.size())));
        thirtyMinutesIterator.initializeOffsets();
        return thirtyMinutesIterator;
    }

    public OneHourStockDataSetIterator getOneHourStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        OneHourStockDataSetIterator oneHourIterator = OneHourStockDataSetIterator.getInstance();
        List<StockData> stockDataList = oneHourIterator.readStockDataFromFile(file, simbolo);
        oneHourIterator.setMiniBatchSize(batchSize);
        oneHourIterator.setExampleLength(exampleLength);
        oneHourIterator.setCategory(category);
        oneHourIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneHourIterator.setTrain(stockDataList.subList(0, oneHourIterator.getSplit()));
        oneHourIterator.setTest(oneHourIterator.generateTestDataSet(stockDataList.subList(oneHourIterator.getSplit(), stockDataList.size())));
        oneHourIterator.initializeOffsets();
        return oneHourIterator;
    }

    public TwoHoursStockDataSetIterator getTwoHoursStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        TwoHoursStockDataSetIterator twoHoursIterator = TwoHoursStockDataSetIterator.getInstance();
        List<StockData> stockDataList = twoHoursIterator.readStockDataFromFile(file, simbolo);
        twoHoursIterator.setMiniBatchSize(batchSize);
        twoHoursIterator.setExampleLength(exampleLength);
        twoHoursIterator.setCategory(category);
        twoHoursIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        twoHoursIterator.setTrain(stockDataList.subList(0, twoHoursIterator.getSplit()));
        twoHoursIterator.setTest(twoHoursIterator.generateTestDataSet(stockDataList.subList(twoHoursIterator.getSplit(), stockDataList.size())));
        twoHoursIterator.initializeOffsets();
        return twoHoursIterator;
    }

    public ThreeHoursStockDataSetIterator getThreeHoursStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        ThreeHoursStockDataSetIterator threeHoursStockDataSetIterator = ThreeHoursStockDataSetIterator.getInstance();
        List<StockData> stockDataList = threeHoursStockDataSetIterator.readStockDataFromFile(file, simbolo);
        threeHoursStockDataSetIterator.setMiniBatchSize(batchSize);
        threeHoursStockDataSetIterator.setExampleLength(exampleLength);
        threeHoursStockDataSetIterator.setCategory(category);
        threeHoursStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        threeHoursStockDataSetIterator.setTrain(stockDataList.subList(0, threeHoursStockDataSetIterator.getSplit()));
        threeHoursStockDataSetIterator.setTest(threeHoursStockDataSetIterator.generateTestDataSet(stockDataList.subList(threeHoursStockDataSetIterator.getSplit(), stockDataList.size())));
        threeHoursStockDataSetIterator.initializeOffsets();
        return threeHoursStockDataSetIterator;
    }

    public FourHoursStockDataSetIterator getFourHoursStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        FourHoursStockDataSetIterator fourHoursStockDataSetIterator = FourHoursStockDataSetIterator.getInstance();
        List<StockData> stockDataList = fourHoursStockDataSetIterator.readStockDataFromFile(file, simbolo);
        fourHoursStockDataSetIterator.setMiniBatchSize(batchSize);
        fourHoursStockDataSetIterator.setExampleLength(exampleLength);
        fourHoursStockDataSetIterator.setCategory(category);
        fourHoursStockDataSetIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        fourHoursStockDataSetIterator.setTrain(stockDataList.subList(0, fourHoursStockDataSetIterator.getSplit()));
        fourHoursStockDataSetIterator.setTest(fourHoursStockDataSetIterator.generateTestDataSet(stockDataList.subList(fourHoursStockDataSetIterator.getSplit(), stockDataList.size())));
        fourHoursStockDataSetIterator.initializeOffsets();
        return fourHoursStockDataSetIterator;
    }

    public OneDayStockDataSetIterator getOneDayStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        OneDayStockDataSetIterator oneDayIterator = OneDayStockDataSetIterator.getInstance();
        List<StockData> stockDataList = oneDayIterator.readStockDataFromFile(file, simbolo);
        oneDayIterator.setMiniBatchSize(batchSize);
        oneDayIterator.setExampleLength(exampleLength);
        oneDayIterator.setCategory(category);
        oneDayIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneDayIterator.setTrain(stockDataList.subList(0, oneDayIterator.getSplit()));
        oneDayIterator.setTest(oneDayIterator.generateTestDataSet(stockDataList.subList(oneDayIterator.getSplit(), stockDataList.size())));
        oneDayIterator.initializeOffsets();
        return oneDayIterator;
    }

    public OneWeekStockDataSetIterator getOneWeekStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        OneWeekStockDataSetIterator oneWeekIterator = OneWeekStockDataSetIterator.getInstance();
        List<StockData> stockDataList = oneWeekIterator.readStockDataFromFile(file, simbolo);
        oneWeekIterator.setMiniBatchSize(batchSize);
        oneWeekIterator.setExampleLength(exampleLength);
        oneWeekIterator.setCategory(category);
        oneWeekIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
        oneWeekIterator.setTrain(stockDataList.subList(0, oneWeekIterator.getSplit()));
        oneWeekIterator.setTest(oneWeekIterator.generateTestDataSet(stockDataList.subList(oneWeekIterator.getSplit(), stockDataList.size())));
        oneWeekIterator.initializeOffsets();
        return oneWeekIterator;
    }

    public OneMonthStockDataSetIterator getOneMonthStockDataSetIterator(String simbolo, String file, int batchSize, double splitRatio, PriceCategory category) {
        OneMonthStockDataSetIterator oneMonthIterator = OneMonthStockDataSetIterator.getInstance();
        List<StockData> stockDataList = duplicate(oneMonthIterator.readStockDataFromFile(file, simbolo));
        oneMonthIterator.setMiniBatchSize(8);
        oneMonthIterator.setExampleLength(5);
        oneMonthIterator.setCategory(category);
//        oneMonthIterator.setSplit((int) Math.round(stockDataList.size() * splitRatio));
//        oneMonthIterator.setTrain(stockDataList.subList(0, oneMonthIterator.getSplit()));
        oneMonthIterator.setTrain(stockDataList);
//        oneMonthIterator.setTest(oneMonthIterator.generateTestDataSet(stockDataList.subList(oneMonthIterator.getSplit(), stockDataList.size())));
        oneMonthIterator.setTest(oneMonthIterator.generateTestDataSet(stockDataList));
        oneMonthIterator.initializeOffsets();
        return oneMonthIterator;
    }

    private List<StockData> duplicate(List<StockData> stockDataList) {
        List<StockData> duplicatedStockDataList = new ArrayList<>();
        for (StockData stock : stockDataList) {
            duplicatedStockDataList.add(stock);
        }
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
    public List<String> predictPriceOneAhead (MultiLayerNetwork net, List<Pair<INDArray, INDArray>> testData, double max, double min) {
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

    public void createCSVFileForNeuralNets(Date beginDate, Date endDate, String period) {
        CsvBarsLoader csvBarsLoader = new CsvBarsLoader();
        csvBarsLoader.createCSVFileForNeuralNets(beginDate, endDate, period);
    }

    public DataPointsListResultSet prepareDataPointToBeSaved(List<String> dataPointList, String nomeDoConjunto) {

        //predicts
        String predictsDataPointsArray[] = dataPointList.get(0).replace("[", "").replace("{", "").replace("}", "").replaceAll("\"\"", "").split(",");

        List<String> xPredictToken = new ArrayList<>();
        List<String> yPredictToken = new ArrayList<>();

        for (String token : predictsDataPointsArray) {
            if (token != null){
                if (token.substring(1, 2).equals("x")) {
                    xPredictToken.add(token.replaceAll("\"", "").replace("x:", "").replace("]", ""));
                } else if (token.substring(1, 2).equals("y")) {
                    yPredictToken.add(token.replaceAll("\"", "").replace("y:", "").replace("]", ""));
                }
            } else {
                log.info("Couldn't get any results, probably need more data");
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

        DataPointsListResultSet dataPointsListResultSet = new DataPointsListResultSet();
        if (nomeDoConjunto != null){
            dataPointsListResultSet.setNomeConjunto(nomeDoConjunto);
        }
        dataPointsListResultSet.setActualDataPointsModelList(actualDataPointsModelListXY);
        dataPointsListResultSet.setPredictDataPointsModelList(predictsDataPointsModelListXY);

        return dataPointsListResultSet;
    }

    public Double calculateErrorPercentageAverage(DataPointsListResultSet dataPointsList) {

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

