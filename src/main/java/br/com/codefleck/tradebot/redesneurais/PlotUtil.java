package br.com.codefleck.tradebot.redesneurais;

import com.google.gson.Gson;
import org.ta4j.core.TimeSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlotUtil {

    public static List<String> plot(double[] predicts, double[] actuals, TimeSeries testTimeSeries) {
        double[] index = new double[predicts.length];
        for (int i = 0; i < predicts.length; i++)
            index[i] = i;

        String predictsDataPoints = addSeries(index, predicts, testTimeSeries);
        String actualsDataPoints = addSeries(index, actuals, testTimeSeries);

        List<String> dataPointsList = new ArrayList<>();
        dataPointsList.add(predictsDataPoints);
        dataPointsList.add(actualsDataPoints);

        return dataPointsList;
    }

    private static String addSeries (double[] x, double[] y, TimeSeries testTimeSeries){

        Gson gsonObj = new Gson();

        Map<Object,Object> map;
        List<Map<Object,Object>> list = new ArrayList<>();

        for( int j = 0; j < x.length; j++ ){
            map = new HashMap<>();
            map.put("x", x[j]);  map.put("y", y[j]);
//            System.out.println(x[j] + ", " + y[j] + ", " + testTimeSeries.getBar(j).getSimpleDateName());
            list.add(map);
        }

        String dataPoints = gsonObj.toJson(list);
        return dataPoints;
    }

    private static String addSeriesForecast (double[] x, double[] y, String label){
        Gson gsonObj = new Gson();
        Map<Object,Object> map = null;
        List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

        for( int j = 0; j < x.length; j++ ){
            map = new HashMap<Object,Object>();
            map.put("x", x[j]);  map.put("y", y[j]);
            list.add(map);
        }
        String dataPoints = gsonObj.toJson(list);
        return dataPoints;
    }

    private static int minValue (double[] predicts, double[] actuals) {
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < predicts.length; i++) {
            if (min > predicts[i]) min = predicts[i];
            if (min > actuals[i]) min = actuals[i];
        }
        return (int) (min * 0.98);
    }

    private static int maxValue (double[] predicts, double[] actuals) {
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < predicts.length; i++) {
            if (max < predicts[i]) max = predicts[i];
            if (max < actuals[i]) max = actuals[i];
        }
        return (int) (max * 1.02);
    }

}