package br.com.codefleck.tradebot.core.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.nd4j.linalg.io.ClassPathResource;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;

import br.com.codefleck.tradebot.tradingInterfaces.Ticker;

public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "date,symbol,open,close,low, high, volume, 5 day sma";

    public static void writeCsvFile(List<CustomBaseBar> lines, List<SMA> smaList) {

        List<CustomBaseBar> linesList = lines;
        String fileName = System.getProperty("user.home") + "/coinBaseForNeuralNetSMA.csv";

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER).append(NEW_LINE_SEPARATOR);
            int i = 0;
            for (CustomBaseBar baseBar : linesList) {

                System.out.println("Iteração " + i);
                System.out.println("base bar being printed: " + baseBar.toString());
                System.out.println("aaaand the sma: " + smaList.get(i).getValor() + " , " + smaList.get(i).getData());

                if (baseBar == null || smaList.get(i) == null) {
                    break;
                }

                fileWriter.append(baseBar.getDate()).append(COMMA_DELIMITER).append(String.valueOf(baseBar.getSymbol())).append(COMMA_DELIMITER).append(String.valueOf(baseBar.getOpen())).append(COMMA_DELIMITER).append(String.valueOf(baseBar.getClose())).append(COMMA_DELIMITER).append(String.valueOf(baseBar.getLow())).append(COMMA_DELIMITER).append(String.valueOf(baseBar.getHigh())).append(COMMA_DELIMITER).append(String.valueOf(baseBar.getVolume())).append(COMMA_DELIMITER).append(String.valueOf(smaList.get(i).getValor())).append(NEW_LINE_SEPARATOR);

                i++;

            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    public void writeCsvFileForNeuralNets(BaseTimeSeries customTimeSeries, String period) {

        final String FILE_HEADER_FOR_NEURAL_NETS = "date,symbol,open,close,low, high, volume";

        List<Bar> linesList = new ArrayList<>();

        for (int i=0; i<customTimeSeries.getEndIndex();i++){
            linesList.add(customTimeSeries.getBar(i));
        }

        String filePathName = getFilePathNameAccordingToPeriod(period);

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(filePathName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER_FOR_NEURAL_NETS).append(NEW_LINE_SEPARATOR);
            int i = 0;
            for (Bar bar : linesList) {

                if (bar == null) {
                    break;
                }

                fileWriter.append(bar.getSimpleDateName().substring(0,10)).append(COMMA_DELIMITER) //check if it is creating the correct date in csv file for training Nets (need time and minute)
                        .append(("BTC")).append(COMMA_DELIMITER)
                        .append(String.valueOf(bar.getOpenPrice())).append(COMMA_DELIMITER)
                        .append(String.valueOf(bar.getClosePrice())).append(COMMA_DELIMITER)
                        .append(String.valueOf(bar.getMinPrice())).append(COMMA_DELIMITER)
                        .append(String.valueOf(bar.getMaxPrice())).append(COMMA_DELIMITER)
                        .append(String.valueOf(bar.getVolume())).append(NEW_LINE_SEPARATOR);
                i++;
            }

            System.out.println("CSV file for " + period + " created successfully!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter");
                e.printStackTrace();
            }
        }
    }

    private String getFilePathNameAccordingToPeriod(String period) {

        if (period.equals("1 minuto")){
            return CsvFileWriter.class.getResource("/oneminute/OneMinuteDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("5 minutos")) {
            return CsvFileWriter.class.getResource("/fiveminutes/FiveMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("10 minutos")){
            return CsvFileWriter.class.getResource("/tenminutes/TenMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("15 minutos")){
            return CsvFileWriter.class.getResource("/fifteenminutes/FifteenMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("30 minutos")){
            return CsvFileWriter.class.getResource("/thirtyminutes/ThirtyMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 hora")){
            return CsvFileWriter.class.getResource("/onehour/OneHourDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("2 horas")){
            return CsvFileWriter.class.getResource("/twohours/TwoHoursDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("3 horas")){
            return CsvFileWriter.class.getResource("/threehours/ThreeHoursDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("4 horas")){
            return CsvFileWriter.class.getResource("/fourhours/FourHoursDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 dia")){
            return CsvFileWriter.class.getResource("/oneday/OneDayDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 semana")){
            return CsvFileWriter.class.getResource("/oneweek/OneWeekDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 mês")){
            return CsvFileWriter.class.getResource("/onemonth/OneMonthDataForTrainingNeuralNets.csv").getPath();
        }
        return null;
    }
}
