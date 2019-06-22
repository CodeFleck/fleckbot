package br.com.codefleck.tradebot.services.impl;


import br.com.codefleck.tradebot.core.util.Converter;
import br.com.codefleck.tradebot.core.util.CsvBarsLoader;
import br.com.codefleck.tradebot.core.util.DownSamplingTimeSeries;
import br.com.codefleck.tradebot.daos.StockDataDao;
import br.com.codefleck.tradebot.models.PriceCategory;
import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.redesneurais.iterators.GeneralStockDataSetIterator;
import br.com.codefleck.tradebot.redesneurais.recurrentnets.RecurrentNets;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("modelService")
@Transactional
@ComponentScan(basePackages = {"br.com.codefleck.tradebot"})
public class ModelServiceImpl {

    @Autowired
    StockDataDao stockDataDao;
    @Autowired
    PredictionServiceImpl predictionService;

    Converter converter = new Converter();

    public void trainModel(String period, String chosenCategory){

        Runnable runnable = () -> {
            try {
                System.out.println("Initializing model creation for " + period);

                int exampleLength = 22; // time series length, assume 22 working days per month
                GeneralStockDataSetIterator iterator;
                String csvFilePath = getCSVFilePathForTrainingNeuralNets(period);
                String symbol = "BTC"; // cryptocurrency name (Just BTC for now)
                int batchSize = 128;
                double splitRatio = 0.8;
                int epochs = 100;

                System.out.println("Creating dataSet iterator...");
                PriceCategory category = predictionService.verifyCategory(chosenCategory);;

                List<StockData> stockDataList = readStockDataFromDataBase(period);

                iterator = new GeneralStockDataSetIterator(stockDataList, batchSize, exampleLength, splitRatio, category);

                System.out.println("Building LSTM networks...");
                MultiLayerNetwork net = RecurrentNets.createAndBuildLstmNetworks(iterator.inputColumns(), iterator.totalOutcomes());

                System.out.println("Training LSTM network...");
                for (int i = 0; i < epochs; i++) {
                    System.out.println("epoch: " + i + " | Period: " + period);
                    while (iterator.hasNext()) net.fit(iterator.next()); // fit model using mini-batch data
                    iterator.reset(); // reset iterator
                    net.rnnClearPreviousState(); // clear previous state
                }

                //Print the  number of parameters in the network (and for each layer)
                Layer[] layers_before_saving = net.getLayers();
                int totalNumParams_before_saving = 0;
                for( int i=0; i<layers_before_saving.length; i++ ){
                    int nParams = Math.toIntExact(layers_before_saving[i].numParams());
                    System.out.println("Number of parameters in layer " + i + ": " + nParams);
                    totalNumParams_before_saving += nParams;
                }
                System.out.println("Total number of network parameters: " + totalNumParams_before_saving);

                System.out.println("Saving model...");
                File locationToSave = new File(System.getProperty("user.home") + "/models/StockPriceLSTM_".concat(period.replace(" ", "")).concat("_").concat(String.valueOf(category)).concat(".zip"));
                // saveUpdater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this to train your network more in the future
                ModelSerializer.writeModel(net, locationToSave, true);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    public List<StockData> readStockDataFromDataBase(String period) {

        List<StockData> stockDataList = new ArrayList<>();
        List<StockData> customizedPerPeriodStockDataList = new ArrayList<>();

        //stock data list size should always be #1762
        if (period.equals("1 minuto")) {
            stockDataList = stockDataDao.ListLatest(1762);
        }
        if (period.equals("5 minutos")) {
            stockDataList = stockDataDao.ListLatest(8810);
        }
        if (period.equals("10 minutos")){
            stockDataList = stockDataDao.ListLatest(17620);
        }
        if (period.equals("15 minutos")){
            stockDataList = stockDataDao.ListLatest(26430);
        }
        if (period.equals("30 minutos")){
            stockDataList = stockDataDao.ListLatest(52860);
        }
        if (period.equals("1 hora")){
            stockDataList = stockDataDao.ListLatest(105720);
        }
        if (period.equals("2 horas")) {
            stockDataList = stockDataDao.ListLatest(211440);
        }
        if (period.equals("3 horas")){
            stockDataList = stockDataDao.ListLatest(317160);
        }
        if (period.equals("4 horas")){
            stockDataList = stockDataDao.ListLatest(422880);
        }
        if (period.equals("1 dia")){
            stockDataList = stockDataDao.ListLatest(2537280);
        }
        if (period.equals("1 semana")){
            stockDataList = stockDataDao.ListLatest(17760960);
        }
        if (period.equals("1 mes")){
            stockDataList = stockDataDao.ListLatest(77175600);
        }
        TimeSeries series = converter.transformStockDataIntoTimeSeries(stockDataList);

        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries(period);
        BaseTimeSeries customTimeSeries = downSamplingTimeSeries.aggregate(series);

        customizedPerPeriodStockDataList = converter.transformTimeSeriesIntoStockData(customTimeSeries);
        return customizedPerPeriodStockDataList;
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

    public List<StockData> transportDataFromCSVIntoDatabase() {

        CsvBarsLoader csvBarsLoader = new CsvBarsLoader();
        return csvBarsLoader.transportDataFromCsvIntoDatabase();
    }
}
