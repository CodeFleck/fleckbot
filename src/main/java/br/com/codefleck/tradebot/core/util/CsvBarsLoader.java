package br.com.codefleck.tradebot.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;

import com.opencsv.CSVReader;

/**
 * This class build a Ta4j time series from a CSV file containing bars.
 */
public class CsvBarsLoader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy,MM,dd");


    public static TimeSeries loadCoinBaseSeries(Date beginDate, Date endDate) {

        Date fromDate = beginDate;
        Date toDate = endDate;

        InputStream stream = CsvBarsLoader.class.getClassLoader().getResourceAsStream("coinbaseUSD_1-min_data_2014-12-01_to_2018-01-08.csv");

        List<Bar> bars = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',', '"', 1);
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                Date tempDate = new Date(Long.valueOf(line[0]) * 1000L);

                if (tempDate.after(fromDate) && tempDate.before(endDate)){

                String dateAsText = new SimpleDateFormat("yyyy,MM,dd")
                    .format(new Date(Long.valueOf(line[0]) * 1000L));

                ZonedDateTime date = LocalDate.parse(dateAsText, DATE_FORMAT).atStartOfDay(ZoneId.systemDefault());
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

//    public static void main(String[] args) {
//        TimeSeries series = CsvBarsLoader.loadCoinBaseSeriesForMiniTesting();
//
//        System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
//        System.out.println("Number of bars: " + series.getBarCount());
//        System.out.println("First bar: \n"
//            + "\tVolume: " + series.getBar(0).getVolume() + "\n"
//            + "\tOpen price: " + series.getBar(0).getOpenPrice()+ "\n"
//            + "\tClose price: " + series.getBar(0).getClosePrice());
//    }
}
