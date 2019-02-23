package br.com.codefleck.tradebot.core.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;

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

                fileWriter.append(bar.getSimpleDateName().substring(0, 10)).append(COMMA_DELIMITER) //check if it is creating the correct date in csv file for training Nets (need time and minute)
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
            return new File("OneMinuteDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("5 minutos")) {
            return new File("FiveMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("10 minutos")) {
            return new File("TenMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("15 minutos")) {
            return new File("FifteenMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("30 minutos")) {
            return new File("ThirtyMinutesDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 hora")) {
            return new File("OneHourDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("2 horas")) {
            return new File("TwoHoursDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("3 horas")) {
            return new File("ThreeHoursDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("4 horas")) {
            return new File("FourHoursDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 dia")) {
            return new File("OneDayDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 semana")) {
            return new File("OneWeekDataForTrainingNeuralNets.csv").getPath();
        }
        if (period.equals("1 mês")) {
            return new File("OneMonthDataForTrainingNeuralNets.csv").getPath();
        }
        return null;
    }
}
