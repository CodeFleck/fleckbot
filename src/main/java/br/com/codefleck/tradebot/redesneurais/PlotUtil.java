package br.com.codefleck.tradebot.redesneurais;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlotUtil {

    public static List<String> plot(double[] predicts, double[] actuals) {
        double[] index = new double[predicts.length];
        for (int i = 0; i < predicts.length; i++)
            index[i] = i;

        String predictsDataPoints = addSeries(index, predicts);
        String actualsDataPoints = addSeries(index, actuals);

        List<String> dataPointsList = new ArrayList<>();
        dataPointsList.add(predictsDataPoints);
        dataPointsList.add(actualsDataPoints);

        return dataPointsList;
    }

    private static String addSeries (double[] x, double[] y){

        Gson gsonObj = new Gson();

        Map<Object,Object> map;
        List<Map<Object,Object>> list = new ArrayList<>();

        for( int j = 0; j < x.length; j++ ){
            map = new HashMap<>();
            map.put("x", x[j]);  map.put("y", y[j]);
            list.add(map);
        }

        String dataPoints = gsonObj.toJson(list);
        return dataPoints;
    }
}