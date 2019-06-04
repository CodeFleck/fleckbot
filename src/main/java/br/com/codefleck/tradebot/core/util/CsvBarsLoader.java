package br.com.codefleck.tradebot.core.util;

import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ta4j.core.*;

import com.opencsv.CSVReader;

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

    public static void editCoinBaseSeriesForNeuralNets(List<SMA> smaList) {

        String resourcePath = System.getProperty("user.home") + "/csv/coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv";
        InputStream stream = null;
        try {
            stream = new FileInputStream(resourcePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<CustomBaseBar> customBaseBars = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                String date = line[0];
                String symbol = line[1];
                double open = Double.parseDouble(line[2]);
                double high = Double.parseDouble(line[3]);
                double low = Double.parseDouble(line[4]);
                double close = Double.parseDouble(line[5]);
                double volume = Double.parseDouble(line[6]);

                customBaseBars.add(new CustomBaseBar(date, symbol, open, high, low, close, volume));
            }
        } catch (IOException ioe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars from CSV", ioe);
        } catch (NumberFormatException nfe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
        }

        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries("day");

        List<CustomBaseBar> AgregatedCustomBaseBars = downSamplingTimeSeries.customAggregate(customBaseBars);

        CsvFileWriter csvFileWriter = new CsvFileWriter();
        csvFileWriter.writeCsvFile(AgregatedCustomBaseBars, smaList);

    }

    public BaseTimeSeries createCSVFileForNeuralNets(Date beginDate, Date endDate, String period) {

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
        BaseTimeSeries baseTimeSeries = new BaseTimeSeries("coinbase_bars", bars);

        DownSamplingTimeSeries downSamplingTimeSeries = new DownSamplingTimeSeries(period);

        BaseTimeSeries customTimeSeries = downSamplingTimeSeries.aggregate(baseTimeSeries);

        CsvFileWriter csvFileWriter = new CsvFileWriter();
        csvFileWriter.writeCsvFileForNeuralNets(customTimeSeries);

        return customTimeSeries;
    }

}
