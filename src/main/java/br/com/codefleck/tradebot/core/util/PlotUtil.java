package br.com.codefleck.tradebot.core.util;

import br.com.codefleck.tradebot.models.DataPoints;
import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PlotUtil {

    public List<String> plot(DataPointsListResultSet resultSet) {
        double[] index = new double[resultSet.getPredictDataPointsList().size()];
        for (int i = 0; i < resultSet.getPredictDataPointsList().size(); i++)
            index[i] = i;

        List<String> dataPointsStringList = new ArrayList<>();

        String predictsDataPoints = addSeries(index, resultSet.getPredictDataPointsList());
        dataPointsStringList.add(predictsDataPoints);

        String actualsDataPoints = addSeries(index, resultSet.getActualDataPointsList());
        dataPointsStringList.add(actualsDataPoints);

        return dataPointsStringList;
    }

    private static String addSeries (double[] x, List<DataPoints> y){

        StringBuilder formattedDataPointsString = new StringBuilder();


        for( int j = 0; j < x.length; j++ ){

            if (j == 0){
                formattedDataPointsString.append("[{");
            } else {
                formattedDataPointsString.append(",{");
            }

            String label = String.valueOf(y.get(j).getLocalDateTime().toLocalDate());
            String value = String.valueOf(y.get(j).getY());

            formattedDataPointsString.append("label: \"").append(label).append("\"");
            formattedDataPointsString.append(", y:").append(value);
            formattedDataPointsString.append("}");

        }

        formattedDataPointsString.append("]");

        return formattedDataPointsString.toString();
    }
}