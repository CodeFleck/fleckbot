package br.com.codefleck.tradebot.core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "date,symbol,open,close,low, high, volume";

    public static void writeCsvFile(List<CustomBaseBar> lines) {

        List<CustomBaseBar> linesList = lines;
        String fileName = System.getProperty("user.home")+"/coinBaseForNeuralNet.csv";

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER).append(NEW_LINE_SEPARATOR);

            for (CustomBaseBar baseBar : linesList) {

                fileWriter.append(baseBar.getDate()).append(COMMA_DELIMITER)
                    .append(String.valueOf(baseBar.getSymbol())).append(COMMA_DELIMITER)
                    .append(String.valueOf(baseBar.getOpen())).append(COMMA_DELIMITER)
                    .append(String.valueOf(baseBar.getClose())).append(COMMA_DELIMITER)
                    .append(String.valueOf(baseBar.getLow())).append(COMMA_DELIMITER)
                    .append(String.valueOf(baseBar.getHigh())).append(COMMA_DELIMITER)
                    .append(String.valueOf(baseBar.getVolume()))
                    .append(NEW_LINE_SEPARATOR);

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