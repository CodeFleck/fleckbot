package br.com.codefleck.tradebot.core.util;

import com.opencsv.CSVReader;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import ta4jexamples.loaders.CsvTradesLoader;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class build a Ta4j time series from a CSV file containing bars.
 */
public class CsvBarsLoader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy,MM,dd HH:mm:ss");

    public static TimeSeries loadCoinBaseSeries(Date beginDate, Date endDate) {

        String resourcePath = System.getProperty("user.home") + "/csv/coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv";
        InputStream stream = null;
        try {
            stream = new FileInputStream(resourcePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Bar> bars = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                Date tempDate = new Date(Long.valueOf(line[0]) * 1000L);

                if (tempDate.after(beginDate) && tempDate.before(endDate)){

                    String dateAsText = new SimpleDateFormat("yyyy,MM,dd HH:mm:ss")
                        .format(tempDate);

                    ZonedDateTime date = ZonedDateTime.of(LocalDate.parse(dateAsText, DATE_FORMAT), LocalTime.parse(dateAsText, DATE_FORMAT), ZoneId.systemDefault());

                    double open = Double.parseDouble(line[1]);
                    double high = Double.parseDouble(line[2]);
                    double low = Double.parseDouble(line[3]);
                    double close = Double.parseDouble(line[4]);
                    double volume = Double.parseDouble(line[5]);

                    bars.add(new BaseBar(date, open, high, low, close, volume));
                }
            }
        } catch (IOException ioe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars from CSV", ioe);
        } catch (NumberFormatException nfe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
        }

        return new BaseTimeSeries("coinbase_bars", bars);
    }

    public BaseTimeSeries createCSVFileForNeuralNets(Date beginDate, Date endDate, String period) {

        String resourcePath = System.getProperty("user.home") + "/csv/coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv";
        InputStream stream = null;
        try {
            stream = new FileInputStream(resourcePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Bar> bars = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                Date tempDate = new Date(Long.valueOf(line[0]) * 1000L);

                if (tempDate.after(beginDate) && tempDate.before(endDate)){

                    String dateAsText = new SimpleDateFormat("yyyy,MM,dd HH:mm:ss")
                        .format(tempDate);

                    ZonedDateTime date = ZonedDateTime.of(LocalDate.parse(dateAsText, DATE_FORMAT), LocalTime.parse(dateAsText, DATE_FORMAT), ZoneId.systemDefault());

                    double open = Double.parseDouble(line[1]);
                    double high = Double.parseDouble(line[2]);
                    double low = Double.parseDouble(line[3]);
                    double close = Double.parseDouble(line[4]);
                    double volume = Double.parseDouble(line[5]);

                    bars.add(new BaseBar(date, open, high, low, close, volume));
                }
            }
        } catch (IOException ioe) {
            java.util.logging.Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars from CSV", ioe);
        } catch (NumberFormatException nfe) {
            java.util.logging.Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
        }
        BaseTimeSeries baseTimeSeries = new BaseTimeSeries(period + "_bars", bars);

        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries(period);

        BaseTimeSeries customTimeSeries = downSamplingTimeSeries.aggregate(baseTimeSeries);

        CsvFileWriter csvFileWriter = new CsvFileWriter();
        try {
            csvFileWriter.writeCsvFileForNeuralNets(customTimeSeries, period);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customTimeSeries;
    }

    public BaseTimeSeries createMockDataForForecast() {

        String period = "4 horas";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date beginDate = null;
        try {
            beginDate = formato.parse("2017-07-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = formato.parse("2018-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String resourcePath = System.getProperty("user.home") + "/csv/coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv";
        InputStream stream = null;
        try {
            stream = new FileInputStream(resourcePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Bar> bars = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                Date tempDate = new Date(Long.valueOf(line[0]) * 1000L);

                if (tempDate.after(beginDate) && tempDate.before(endDate)){

                    String dateAsText = new SimpleDateFormat("yyyy,MM,dd HH:mm:ss")
                            .format(tempDate);

                    ZonedDateTime date = ZonedDateTime.of(LocalDate.parse(dateAsText, DATE_FORMAT), LocalTime.parse(dateAsText, DATE_FORMAT), ZoneId.systemDefault());

                    double open = Double.parseDouble(line[1]);
                    double high = Double.parseDouble(line[2]);
                    double low = Double.parseDouble(line[3]);
                    double close = Double.parseDouble(line[4]);
                    double volume = Double.parseDouble(line[5]);

                    bars.add(new BaseBar(date, open, high, low, close, volume));
                }
            }
        } catch (IOException ioe) {
            java.util.logging.Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars from CSV", ioe);
        } catch (NumberFormatException nfe) {
            java.util.logging.Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
        }
        BaseTimeSeries baseTimeSeries = new BaseTimeSeries(period + "_bars", bars);

        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries(period);

        BaseTimeSeries customTimeSeries = downSamplingTimeSeries.aggregate(baseTimeSeries);

        return customTimeSeries;
    }

    public static TimeSeries loadBitstampSeries() {

        // Reading all lines of the CSV file
        InputStream stream = ta4jexamples.loaders.CsvTradesLoader.class.getClassLoader().getResourceAsStream("BitstampUSD_1-min_data_full.csv");
        CSVReader csvReader = null;
        List<String[]> lines = null;
        try {
            csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',');
            lines = csvReader.readAll();
            lines.remove(0); // Removing header line
        } catch (IOException ioe) {
            Logger.getLogger(CsvTradesLoader.class.getName()).log(Level.SEVERE, "Unable to load trades from CSV", ioe);
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        TimeSeries series = new BaseTimeSeries();
        if ((lines != null) && !lines.isEmpty()) {

            // Getting the first and last trades timestamps
            ZonedDateTime beginTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(lines.get(0)[0]) * 1000), ZoneId.systemDefault());
            ZonedDateTime endTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(lines.get(lines.size() - 1)[0]) * 1000), ZoneId.systemDefault());
            if (beginTime.isAfter(endTime)) {
                Instant beginInstant = beginTime.toInstant();
                Instant endInstant = endTime.toInstant();
                beginTime = ZonedDateTime.ofInstant(endInstant, ZoneId.systemDefault());
                endTime = ZonedDateTime.ofInstant(beginInstant, ZoneId.systemDefault());
                // Since the CSV file has the most recent trades at the top of the file, we'll reverse the list to feed the List<Bar> correctly.
                Collections.reverse(lines);
            }
            // build the list of populated bars
            buildSeries(series,beginTime, endTime, 60, lines);//300 sec is 5 min
        }

        return series;
    }

    /**
     * Builds a list of populated bars from csv data.
     * @param beginTime the begin time of the whole period
     * @param endTime the end time of the whole period
     * @param duration the bar duration (in seconds)
     * @param lines the csv data returned by CSVReader.readAll()
     * @return the list of populated bars
     */
    private static void buildSeries(TimeSeries series, ZonedDateTime beginTime, ZonedDateTime endTime, int duration, List<String[]> lines) {


        Duration barDuration = Duration.ofSeconds(duration);
        ZonedDateTime barEndTime = beginTime;
        // line number of trade data
        int i = 0;
        do {
            // build a bar
            barEndTime = barEndTime.plus(barDuration);
            Bar bar = new BaseBar(barDuration, barEndTime);
            do {
                // get a trade
                String[] tradeLine = lines.get(i);
                ZonedDateTime tradeTimeStamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tradeLine[0]) * 1000), ZoneId.systemDefault());
                // if the trade happened during the bar
                if (bar.inPeriod(tradeTimeStamp)) {
                    // add the trade to the bar
                    double tradePrice = Double.parseDouble(tradeLine[1]);
                    double tradeVolume = Double.parseDouble(tradeLine[2]);
                    bar.addTrade(tradeVolume, tradePrice);
                } else {
                    // the trade happened after the end of the bar
                    // go to the next bar but stay with the same trade (don't increment i)
                    // this break will drop us after the inner "while", skipping the increment
                    break;
                }
                i++;
            } while (i < lines.size());
            // if the bar has any trades add it to the bars list
            // this is where the break drops to
            if (bar.getTrades() > 0) {
                series.addBar(bar);
            }
        } while (barEndTime.isBefore(endTime));
    }

    public List<StockData> transportDataFromCsvIntoDatabase(){
        System.out.println("Loading csv data...");
        BaseTimeSeries baseTimeSeries = loadFullCoinbaseSeries();
        System.out.println("Persisting data into database...");
        Converter converter = new Converter();
        List<StockData> stockDataList = converter.transformTimeSeriesIntoStockData(baseTimeSeries);
        return stockDataList;
    }
}
