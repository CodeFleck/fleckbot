package br.com.codefleck.tradebot.core.util;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "date,symbol,open,close,low, high, volume";


    public void writeCsvFileForNeuralNets(BaseTimeSeries customTimeSeries, String period) throws IOException {

        List<Bar> linesList = new ArrayList<>();

        for (int i = 0; i < customTimeSeries.getEndIndex(); i++) {
            linesList.add(customTimeSeries.getBar(i));
        }
        
        String csvFileForTrainingNeuralNets = getFilePathNameAccordingToPeriod(period);

        FileWriter fileWriter = new FileWriter(csvFileForTrainingNeuralNets);
        try {

            //Write the CSV file header
            fileWriter.append(FILE_HEADER).append(NEW_LINE_SEPARATOR);
            int i = 0;
            for (Bar bar : linesList) {

                if (bar == null) {
                    break;
                }

                fileWriter.append(bar.getDateName().substring(0, 10)).append(COMMA_DELIMITER) //check if it is creating the correct date in csv file for training Nets (need time and minute)
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

        if (period.equals("1 minuto")) {
            String filePathName = System.getProperty("user.home") + "/csv/OneMinuteDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("5 minutos")) {
            String filePathName = System.getProperty("user.home") + "/csv/FiveMinutesDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("10 minutos")) {
            String filePathName = System.getProperty("user.home") + "/csv/TenMinutesDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("15 minutos")) {
            String filePathName = System.getProperty("user.home") + "/csv/FifteenMinutesDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("30 minutos")) {
            String filePathName = System.getProperty("user.home") + "/csv/ThirtyMinutesDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("1 hora")) {
            String filePathName = System.getProperty("user.home") + "/csv/OneHourDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("2 horas")) {
            String filePathName = System.getProperty("user.home") + "/csv/TwoHoursDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("3 horas")) {
            String filePathName = System.getProperty("user.home") + "/csv/ThreeHoursDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("4 horas")) {
            String filePathName = System.getProperty("user.home") + "/csv/FourHoursDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("1 dia")) {
            String filePathName = System.getProperty("user.home") + "/csv/OneDayDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("1 semana")) {
            String filePathName = System.getProperty("user.home") + "/csv/OneWeekDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        if (period.equals("1 mês")) {
            String filePathName = System.getProperty("user.home") + "/csv/OneMonthDataForTrainingNeuralNets.csv";
            return filePathName;
        }
        return null;
    }
}
