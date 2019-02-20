package br.com.codefleck.tradebot.core.util;

import br.com.codefleck.tradebot.models.StockData;
import br.com.codefleck.tradebot.services.impl.PredictionServiceImpl;
import com.opencsv.CSVReader;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
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

/**
 * This class build a Ta4j time series from a CSV file containing bars.
 */
public class CsvBarsLoader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy,MM,dd HH:mm:ss");

    public static TimeSeries loadCoinBaseSeries(Date beginDate, Date endDate) {

        InputStream stream = CsvBarsLoader.class.getClassLoader().getResourceAsStream("coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv");

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

    public BaseTimeSeries loadBitfinexDailySeries() {

        InputStream stream = CsvBarsLoader.class.getClassLoader().getResourceAsStream("Bitfinex_BTCUSD_d.csv");

        List<Bar> bars = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                String tempDate = line[0];

                int year = Integer.valueOf(tempDate.substring(0,4));
                int month = Integer.valueOf(tempDate.substring(5,7));
                int day = Integer.valueOf(tempDate.substring(8,10));
                Date tempDate2 = new Date(year, month, day);

                String dateAsText = new SimpleDateFormat("yyyy,MM,dd HH:mm:ss")
                        .format(tempDate2);
                dateAsText.replaceAll("39", "20");

                ZonedDateTime date = ZonedDateTime.of(LocalDate.parse(dateAsText, DATE_FORMAT), LocalTime.parse(dateAsText, DATE_FORMAT), ZoneId.systemDefault());


                double open = Double.parseDouble(line[3]);
                double high = Double.parseDouble(line[4]);
                double low = Double.parseDouble(line[5]);
                double close = Double.parseDouble(line[6]);
                double volume = Double.parseDouble(line[7]);

                bars.add(new BaseBar(date, open, high, low, close, volume));

            }
        } catch (IOException ioe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars from CSV", ioe);
        } catch (NumberFormatException nfe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
        }

        return new BaseTimeSeries("bitfinex_daily", bars);
    }

    public List<StockData> loadBitfinexHourlySeries() {

        InputStream stream = CsvBarsLoader.class.getClassLoader().getResourceAsStream("Bitfinex_BTCUSD_1h.csv");
        List<Bar> bars = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                String tempDate = line[0];

                int year = Integer.valueOf(tempDate.substring(0,4));
                int month = Integer.valueOf(tempDate.substring(5,7));
                int day = Integer.valueOf(tempDate.substring(8,10));
                int hour = Integer.valueOf(tempDate.substring(11,13));
                Date tempDate2 = new Date(year, month, day, hour, 0, 0);

                String dateAsText = new SimpleDateFormat("yyyy,MM,dd HH:mm:ss")
                        .format(tempDate2);
                dateAsText.replaceAll("39", "20");

                ZonedDateTime date = ZonedDateTime.of(LocalDate.parse(dateAsText, DATE_FORMAT), LocalTime.parse(dateAsText, DATE_FORMAT), ZoneId.systemDefault());

                double open = Double.parseDouble(line[3]);
                double high = Double.parseDouble(line[4]);
                double low = Double.parseDouble(line[5]);
                double close = Double.parseDouble(line[6]);
                double volume = Double.parseDouble(line[7]);

                bars.add(new BaseBar(date, open, high, low, close, volume));
            }
        } catch (IOException ioe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars from CSV", ioe);
        } catch (NumberFormatException nfe) {
            Logger.getLogger(CsvBarsLoader.class.getName()).log(Level.SEVERE, "Error while parsing value", nfe);
        }

        BaseTimeSeries timeSeries = new BaseTimeSeries("bitfinex_hourly", bars);
        PredictionServiceImpl predictionService = new PredictionServiceImpl();
        List<StockData> stockDataList = predictionService.transformBarInStockData(timeSeries);

        return stockDataList;
    }

    public void createCSVFileForNeuralNets(Date beginDate, Date endDate, String period) {

        InputStream stream = CsvBarsLoader.class.getClassLoader().getResourceAsStream("coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv");

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
        csvFileWriter.writeCsvFileForNeuralNets(customTimeSeries, period);
    }
}
