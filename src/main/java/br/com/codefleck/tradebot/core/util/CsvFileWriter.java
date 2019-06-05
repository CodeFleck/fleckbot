package br.com.codefleck.tradebot.core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTimeSeries;

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

    public void writeCsvFileForNeuralNets(BaseTimeSeries customTimeSeries) {

        final String FILE_HEADER_FOR_NEURAL_NETS = "date,symbol,open,close,low, high, volume";

        List<Bar> linesList = new ArrayList<>();

        for (int i=0; i<customTimeSeries.getEndIndex();i++){
            linesList.add(customTimeSeries.getBar(i));
        }

        String filePathName = System.getProperty("user.home").concat("/csv/").concat("coinBaseDataForTrainingNeuralNets.csv");

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

                fileWriter.append(bar.getSimpleDateName().substring(0,10)).append(COMMA_DELIMITER)
                    .append(String.valueOf("BTC")).append(COMMA_DELIMITER)
                    .append(String.valueOf(bar.getOpenPrice())).append(COMMA_DELIMITER)
                    .append(String.valueOf(bar.getClosePrice())).append(COMMA_DELIMITER)
                    .append(String.valueOf(bar.getMinPrice())).append(COMMA_DELIMITER)
                    .append(String.valueOf(bar.getMaxPrice())).append(COMMA_DELIMITER)
                    .append(String.valueOf(bar.getVolume())).append(NEW_LINE_SEPARATOR);
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
}